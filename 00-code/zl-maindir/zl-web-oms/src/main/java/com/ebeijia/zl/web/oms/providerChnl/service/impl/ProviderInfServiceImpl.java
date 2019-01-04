package com.ebeijia.zl.web.oms.providerChnl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.req.AccountTransferReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.model.FTPImageVo;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.common.util.OrderConstants;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import com.ebeijia.zl.web.oms.providerChnl.service.ProviderInfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Service("providerInfService")
public class ProviderInfServiceImpl implements ProviderInfService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${FILE_UPLAOD_PATH}")
	private String FILE_UPLAOD_PATH;

	@Value("${FILE_UPLAOD_SEPARATOR}")
	private String FILE_UPLAOD_SEPARATOR;

	@Value("${IMG_SERVER}")
	private String IMG_SERVER;

	@Value("${FILE_NEW_PATH}")
	private String FILE_NEW_PATH;

	@Autowired
	private ProviderInfFacade providerInfFacade;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

	@Autowired
	private CompanyInfFacade companyInfFacade;

	@Autowired
	private AccountTransactionFacade accountTransactionFacade;

	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;
	
	@Override
	public int providerOpenAccount(HttpServletRequest req) {
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		ProviderInf providerInf = null;
		try {
			providerInf = providerInfFacade.getProviderInfById(providerId);
			if (StringUtil.isNullOrEmpty(providerInf)) {
				logger.error("## 查询供应商信息失败，providerId--->{}", providerId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 查询供应商{}信息失败", providerId);
			return 0;
		}
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		BatchOrderList orderList = new BatchOrderList();
		orderList.setUserName(providerInf.getProviderName());
		orderList.setAccountType(UserType.TYPE300.getCode());
		
		LinkedList<BatchOrderList> batchOrderList = new LinkedList<>();
		batchOrderList.add(orderList);
		
		int orderResult = batchOrderService.addBatchOrderAndOrderList(req, batchOrderList, TransCode.MB80.getCode(), UserType.TYPE300.getCode());
		if (orderResult < 0) {
			logger.error("## 新增供应商开户订单信息失败");
			return 0;
		}
		
		String orderId = jedisClusterUtils.get(OrderConstants.providerOrderIdSession);
		int i = batchOrderService.batchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
		if (i < 1) {
			logger.error("## 调用开户接口失败");
			return 0;
		}
		jedisClusterUtils.del(OrderConstants.providerOrderIdSession);
		
		providerInf.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
		try {
			if (!providerInfFacade.updateProviderInf(providerInf)) {
				logger.error("## 更新供应商{}开户成功状态失败", providerId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 更新供应商{}开户状态失败", providerId);
			return 0;
		}
		return 1;
	}

	@Override
	public int addProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
		String companyCode = StringUtil.nullToString(req.getParameter("companyCode"));
		String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
		String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));
		String A00 = StringUtil.nullToString(req.getParameter("A00"));
		String B01 = StringUtil.nullToString(req.getParameter("B01"));
		String B02 = StringUtil.nullToString(req.getParameter("B02"));
		String B03 = StringUtil.nullToString(req.getParameter("B03"));
		String B04 = StringUtil.nullToString(req.getParameter("B04"));
		String B05 = StringUtil.nullToString(req.getParameter("B05"));
		String B06 = StringUtil.nullToString(req.getParameter("B06"));
		String B07 = StringUtil.nullToString(req.getParameter("B07"));
		String B08 = StringUtil.nullToString(req.getParameter("B08"));
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));

		CompanyInf company = companyInfFacade.getCompanyInfByLawCode(companyCode);

		String platformFee = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, RedisConstants.PLATFORM_FEE);
		if (StringUtil.isNullOrEmpty(platformFee)) {
            logger.error("## 新增供应商{}上账，获取平台费率失败", providerId);
			return 0;
		}

		BigDecimal platformFeeSum = new BigDecimal(platformFee).add(new BigDecimal(1));
		BigDecimal companyInAmtSum = new BigDecimal(inaccountAmt).divide(platformFeeSum, 0);

		InaccountOrder order = new InaccountOrder();
		order.setOrderId(IdUtil.getNextId());
		order.setTfrPlatformOrderId(IdUtil.getNextId());
		order.setTfrCompanyOrderId(IdUtil.getNextId());
		order.setOrderType(UserType.TYPE300.getCode());
		order.setCheckStat(CheckStatEnum.CHECK_FALSE.getCode());
		order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));
		order.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setPlatformInSumAmt(order.getInaccountAmt());
		order.setCompanyInSumAmt(companyInAmtSum);
		order.setProviderId(providerId);
		order.setCompanyId(company.getCompanyId());
		order.setRemitCheck(RemitCheckEnum.REMIT_TRUE.getCode());
		order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
		order.setTransferCheck(TransferCheckEnum.INACCOUNT_FALSE.getCode());
		order.setPlatformReceiverCheck(ReceiverEnum.RECEIVER_FALSE.getCode());
		order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_FALSE.getCode());
		order.setRemarks(remarks);
		order.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		order.setCreateUser(user.getId());
		order.setUpdateUser(user.getId());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateTime(System.currentTimeMillis());
		order.setLockVersion(0);

		if (evidenceUrlFile == null || evidenceUrlFile.isEmpty()) {
			logger.error("## 上传图片为空");
			return 0;
		}
		FTPImageVo imgVo = new FTPImageVo();
		imgVo.setImgId(order.getOrderId());
		imgVo.setService(IMG_SERVER);
		imgVo.setNewPath(FILE_NEW_PATH);
		imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
		imgVo.setUploadPath(FILE_UPLAOD_PATH);
		imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_03.getValue());
		Map<String, Object> resultMap = new HashMap<>();
		try {
			resultMap = commonService.uploadImangeName(imgVo, evidenceUrlFile);
			if (String.valueOf(resultMap.get("status").toString()).equals("true")) {
				order.setEvidenceUrl(resultMap.get("msg").toString());
			} else {
				logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 图片上传异常，msg--->{}", resultMap.get("msg"));
			return 0;
		}

		/*Map<String, Object> resultMap = commonService.saveFile(evidenceUrlFile, req, order.getOrderId());
		if (resultMap.get("status").equals(Boolean.FALSE)) {
			logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
			return 0;
		}
		order.setEvidenceUrl(resultMap.get("msg").toString());*/

		List<InaccountOrderDetail> orderDetailList = new ArrayList<InaccountOrderDetail>();
		if (!StringUtil.isNullOrEmpty(A00)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
			orderDetail.setBId("A00");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B01)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
			orderDetail.setBId("B01");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B02)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
			orderDetail.setBId("B02");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B03)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
			orderDetail.setBId("B03");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B04)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
			orderDetail.setBId("B04");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B05)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
			orderDetail.setBId("B05");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B06)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
			orderDetail.setBId("B06");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B07)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
			orderDetail.setBId("B07");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}
		if (!StringUtil.isNullOrEmpty(B08)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
			orderDetail.setBId("B08");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			BigDecimal companyInAmt = orderDetail.getTransAmt().divide(platformFeeSum, 0);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}

		if (!inaccountOrderService.save(order)) {
			logger.error("## 新增入账订单信息失败");
			return 0;
		}

		if (!inaccountOrderDetailService.saveBatch(orderDetailList)) {
			logger.error("## 新增入账订单明细失败");
			return 0;
		}
		return 1;
	}

	@Override
	public int addProviderTransferCommit(HttpServletRequest req) {
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));

		InaccountOrder order = inaccountOrderService.getById(orderId);
		List<InaccountOrderDetail> orderDetail = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);

		if (order == null || orderDetail == null || orderDetail.size() < 1) {
			logger.error("## 查询供应商{}上账订单{}信息为空", providerId, orderId);
			return 0;
		}

		List<AccountTxnVo> transList = new ArrayList<>();
		Set<String> bIds = new TreeSet<>();
		for (InaccountOrderDetail d : orderDetail) {
			AccountTxnVo txnVo = new AccountTxnVo();
			txnVo.setTxnBId(d.getBId());
			txnVo.setTxnAmt(d.getTransAmt());
			txnVo.setUpLoadAmt(d.getTransAmt());
			transList.add(txnVo);
			bIds.add(d.getBId());
		}

		AccountRechargeReqVo reqVo = new AccountRechargeReqVo();
		reqVo.setFromCompanyId(providerId);
		reqVo.setTransAmt(order.getInaccountAmt());
		reqVo.setUploadAmt(order.getInaccountAmt());
		reqVo.setTransList(transList);
		reqVo.setTransId(TransCode.MB20.getCode());
		reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
		reqVo.setUserId(providerId);
		reqVo.setbIds(bIds);
		reqVo.setUserType(UserType.TYPE300.getCode());
		reqVo.setDmsRelatedKey(orderId);
		reqVo.setUserChnlId(providerId);
		reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		reqVo.setTransDesc(order.getRemarks());
		reqVo.setTransNumber(1);

		BaseResult result = new BaseResult();
		try {
			logger.info("供应商充值，远程调用充值接口请求参数--->{}", JSONArray.toJSONString(reqVo));
			result = accountTransactionFacade.executeRecharge(reqVo);
			logger.info("供应商充值，远程调用充值接口返回参数--->{}", JSONArray.toJSONString(result));
		} catch (Exception e) {
			logger.error("## 远程调用充值接口异常", e);
		}
		try {
			if (StringUtil.isNullOrEmpty(result.getCode())) {
				logger.info("供应商充值，远程调用查询接口请求参数--->dmsRelatedKey{},--->transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
			}
		} catch (Exception e) {
			logger.error("## 远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
		}
		if (result == null) {
			logger.error("## 远程调用充值接口失败，返回参数为空，orderId--->{},providerId--->{}", orderId, providerId);
			return 0;
		}

		if (result.getCode().equals(Constants.SUCCESS_CODE)) {
			order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_TRUE.getCode());
		} else {
			order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
			order.setRemarks(result.getMsg());
		}

		if (!inaccountOrderService.saveOrUpdate(order)) {
			logger.error("## 远程调用充值接口，更新订单失败orderId--->{},providerId--->{}", orderId, providerId);
			return 0;
		}
		return 1;
	}

	@Override
	public Map<String, Object> updateProviderRemitStatCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));

		InaccountOrder order = inaccountOrderService.getById(orderId);
		List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);

		if (order == null || orderDetailList == null || orderDetailList.size() < 1) {
			logger.error("## 查询供应商{}订单{}信息为空", providerId, orderId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("status", "暂无可打款订单，请重新查看订单信息");
			return resultMap;
		}

		CompanyInf companyInf = companyInfFacade.getCompanyInfByIsPlatform(IsPlatformEnum.IsPlatformEnum_1.getCode());
		if (companyInf == null) {
			logger.error("## 查询平台企业账户信息为空");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("status", "平台账户不存在，请联系管理员");
			return resultMap;
		}

		AccountTransferReqVo reqVo = new AccountTransferReqVo();
		reqVo.setTransAmt(order.getInaccountAmt());
		reqVo.setUploadAmt(order.getInaccountAmt());
		reqVo.setTfrInUserId(companyInf.getCompanyId());
		reqVo.setTfrOutUserId(providerId);

		List<AccountTxnVo> transList = new ArrayList<>();
		Set<String> bIds = new TreeSet<>();
		for (InaccountOrderDetail orderDetail :orderDetailList ) {
			AccountTxnVo txnVo = new AccountTxnVo();
			txnVo.setTxnBId(orderDetail.getBId());
			txnVo.setTxnAmt(orderDetail.getPlatformInAmt());
			txnVo.setUpLoadAmt(orderDetail.getPlatformInAmt());
			transList.add(txnVo);
			bIds.add(orderDetail.getBId());
		}

		reqVo.setTransList(transList);
		reqVo.setTransId(TransCode.MB40.getCode());
		reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
		reqVo.setUserId(providerId);
		reqVo.setbIds(bIds);
		reqVo.setUserType(UserType.TYPE300.getCode());
		reqVo.setDmsRelatedKey(order.getTfrPlatformOrderId());
		reqVo.setUserChnlId(providerId);
		reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		reqVo.setTransDesc(order.getRemarks());
		reqVo.setTransNumber(1);
		logger.error("远程调用转账接口请求参数--->{}", JSONArray.toJSONString(reqVo));
		BaseResult result = new BaseResult();
		try {
			result = accountTransactionFacade.executeTransfer(reqVo);
		} catch (Exception e) {
			logger.error("## 远程调用转账接口异常", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
		}
		try {
			if (StringUtil.isNullOrEmpty(result.getCode())) {
				result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
			}
		} catch (Exception e) {
			logger.error("## 远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
		}
		logger.error("远程调用转账接口返回参数--->{}", JSONArray.toJSONString(result));
		if (result != null && Constants.SUCCESS_CODE.toString().equals(result.getCode())) {
			order.setTransferCheck(TransferCheckEnum.INACCOUNT_TRUE.getCode());
			order.setPlatformReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
		}

		if (!inaccountOrderService.saveOrUpdate(order)) {
			logger.error("## 更新供应商{}转账至平台订单{}状态{}失败", providerId, orderId, order.getTransferCheck());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统异常，请联系管理员");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public int editProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
		String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
		String companyCode = StringUtil.nullToString(req.getParameter("companyCode"));
		String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));
		String A00 = StringUtil.nullToString(req.getParameter("A00"));
		String B01 = StringUtil.nullToString(req.getParameter("B01"));
		String B02 = StringUtil.nullToString(req.getParameter("B02"));
		String B03 = StringUtil.nullToString(req.getParameter("B03"));
		String B04 = StringUtil.nullToString(req.getParameter("B04"));
		String B05 = StringUtil.nullToString(req.getParameter("B05"));
		String B06 = StringUtil.nullToString(req.getParameter("B06"));
		String B07 = StringUtil.nullToString(req.getParameter("B07"));
		String B08 = StringUtil.nullToString(req.getParameter("B08"));
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));

		CompanyInf company = companyInfFacade.getCompanyInfByLawCode(companyCode);
		if (company == null) {
			logger.error("## 根据企业代码{}查询企业信息为空", companyCode);
			return 0;
		}

		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
		if (order == null) {
			logger.error("## 根据上账订单号{}查询订单信息为空", orderId);
			return 0;
		}

		String platformFee = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, RedisConstants.PLATFORM_FEE);
		if (StringUtil.isNullOrEmpty(platformFee)) {
		    logger.error("## 编辑供应商{}上账，获取平台费率失败", providerId);
			return 0;
		}
		BigDecimal platformFeeSum = new BigDecimal(platformFee).add(new BigDecimal(1));
		BigDecimal companyInAmtSum = new BigDecimal(inaccountAmt).divide(platformFeeSum, 0);

		order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));
		order.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setPlatformInSumAmt(order.getInaccountAmt());
		order.setCompanyInSumAmt(companyInAmtSum);
		order.setCompanyId(company.getCompanyId());
		order.setRemarks(remarks);
		order.setUpdateUser(user.getId());
		order.setUpdateTime(System.currentTimeMillis());
		order.setLockVersion(order.getLockVersion() + 1);

		if (evidenceUrlFile != null && !evidenceUrlFile.isEmpty()) {
			FTPImageVo imgVo = new FTPImageVo();
			imgVo.setImgId(order.getOrderId());
			imgVo.setService(IMG_SERVER);
			imgVo.setNewPath(FILE_NEW_PATH);
			imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
			imgVo.setUploadPath(FILE_UPLAOD_PATH);
			imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_03.getValue());
			Map<String, Object> resultMap = new HashMap<>();
			try {
				resultMap = commonService.uploadImangeName(imgVo, evidenceUrlFile);
				if (String.valueOf(resultMap.get("status").toString()).equals("true")) {
					order.setEvidenceUrl(resultMap.get("msg").toString());
				} else {
					logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
					return 0;
				}
			} catch (Exception e) {
				logger.error("## 图片上传异常，msg--->{}", resultMap.get("msg"));
				return 0;
			}
		}

		/*if (StringUtils.isNotBlank(evidenceUrlFile.getOriginalFilename())) {
			Map<String, Object> resultMap = commonService.saveFile(evidenceUrlFile, req, order.getOrderId());
			if (resultMap.get("status").equals(Boolean.FALSE)) {
				logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
				return 0;
			}
			order.setEvidenceUrl(resultMap.get("msg").toString());
		}*/

		List<InaccountOrderDetail> editOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> addOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> delOrderDetailList = new ArrayList<InaccountOrderDetail>();
		InaccountOrderDetail detailA00 = new InaccountOrderDetail();
		detailA00.setBId("A00");
		detailA00.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailA00 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailA00);
		if (orderDetailA00 == null) {
			if (!StringUtil.isNullOrEmpty(A00)) {
				orderDetailA00 = new InaccountOrderDetail();
				orderDetailA00.setOrderListId(IdUtil.getNextId());
				orderDetailA00.setOrderId(order.getOrderId());
				orderDetailA00.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailA00.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
				orderDetailA00.setBId("A00");
				orderDetailA00.setPlatformInAmt(orderDetailA00.getTransAmt());
				BigDecimal companyInAmt = orderDetailA00.getTransAmt().divide(platformFeeSum, 0);
				orderDetailA00.setCompanyInAmt(companyInAmt);
				orderDetailA00.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailA00.setCreateUser(user.getId());
				orderDetailA00.setUpdateUser(user.getId());
				orderDetailA00.setCreateTime(System.currentTimeMillis());
				orderDetailA00.setUpdateTime(System.currentTimeMillis());
				orderDetailA00.setLockVersion(0);
				addOrderDetailList.add(orderDetailA00);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(A00)) {
				orderDetailA00.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
				orderDetailA00.setPlatformInAmt(orderDetailA00.getTransAmt());
				BigDecimal companyInAmt = orderDetailA00.getTransAmt().divide(platformFeeSum, 0);
				orderDetailA00.setCompanyInAmt(companyInAmt);
				orderDetailA00.setUpdateUser(user.getId());
				orderDetailA00.setUpdateTime(System.currentTimeMillis());
				orderDetailA00.setLockVersion(orderDetailA00.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailA00);
			} else {
				delOrderDetailList.add(orderDetailA00);
			}
		}
		InaccountOrderDetail detailB01 = new InaccountOrderDetail();
		detailB01.setBId("B01");
		detailB01.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB01 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB01);
		if (orderDetailB01 == null) {
			if (!StringUtil.isNullOrEmpty(B01)) {
				orderDetailB01 = new InaccountOrderDetail();
				orderDetailB01.setOrderListId(IdUtil.getNextId());
				orderDetailB01.setOrderId(order.getOrderId());
				orderDetailB01.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB01.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
				orderDetailB01.setBId("B01");
				orderDetailB01.setPlatformInAmt(orderDetailB01.getTransAmt());
				BigDecimal companyInAmt = orderDetailB01.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB01.setCompanyInAmt(companyInAmt);
				orderDetailB01.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB01.setCreateUser(user.getId());
				orderDetailB01.setUpdateUser(user.getId());
				orderDetailB01.setCreateTime(System.currentTimeMillis());
				orderDetailB01.setUpdateTime(System.currentTimeMillis());
				orderDetailB01.setLockVersion(0);
				addOrderDetailList.add(orderDetailB01);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B01)) {
				orderDetailB01.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
				orderDetailB01.setPlatformInAmt(orderDetailB01.getTransAmt());
				BigDecimal companyInAmt = orderDetailB01.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB01.setCompanyInAmt(companyInAmt);
				orderDetailB01.setUpdateUser(user.getId());
				orderDetailB01.setUpdateTime(System.currentTimeMillis());
				orderDetailB01.setLockVersion(orderDetailB01.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB01);
			} else {
				delOrderDetailList.add(orderDetailB01);
			}
		}
		InaccountOrderDetail detailB02 = new InaccountOrderDetail();
		detailB02.setBId("B02");
		detailB02.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB02 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB02);
		if (orderDetailB02 == null) {
			if (!StringUtil.isNullOrEmpty(B02)) {
				orderDetailB02 = new InaccountOrderDetail();
				orderDetailB02.setOrderListId(IdUtil.getNextId());
				orderDetailB02.setOrderId(order.getOrderId());
				orderDetailB02.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB02.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
				orderDetailB02.setBId("B02");
				orderDetailB02.setPlatformInAmt(orderDetailB02.getTransAmt());
				BigDecimal companyInAmt = orderDetailB02.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB02.setCompanyInAmt(companyInAmt);
				orderDetailB02.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB02.setCreateUser(user.getId());
				orderDetailB02.setUpdateUser(user.getId());
				orderDetailB02.setCreateTime(System.currentTimeMillis());
				orderDetailB02.setUpdateTime(System.currentTimeMillis());
				orderDetailB02.setLockVersion(0);
				addOrderDetailList.add(orderDetailB02);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B02)) {
				orderDetailB02.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
				orderDetailB02.setPlatformInAmt(orderDetailB02.getTransAmt());
				BigDecimal companyInAmt = orderDetailB02.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB02.setCompanyInAmt(companyInAmt);
				orderDetailB02.setUpdateUser(user.getId());
				orderDetailB02.setUpdateTime(System.currentTimeMillis());
				orderDetailB02.setLockVersion(orderDetailB02.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB02);
			} else {
				delOrderDetailList.add(orderDetailB02);
			}
		}
		InaccountOrderDetail detailB03 = new InaccountOrderDetail();
		detailB03.setBId("B03");
		detailB03.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB03 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB03);
		if (orderDetailB03 == null) {
			if (!StringUtil.isNullOrEmpty(B03)) {
				orderDetailB03 = new InaccountOrderDetail();
				orderDetailB03.setOrderListId(IdUtil.getNextId());
				orderDetailB03.setOrderId(order.getOrderId());
				orderDetailB03.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB03.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
				orderDetailB03.setBId("B03");
				orderDetailB03.setPlatformInAmt(orderDetailB03.getTransAmt());
				BigDecimal companyInAmt = orderDetailB03.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB03.setCompanyInAmt(companyInAmt);
				orderDetailB03.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB03.setCreateUser(user.getId());
				orderDetailB03.setUpdateUser(user.getId());
				orderDetailB03.setCreateTime(System.currentTimeMillis());
				orderDetailB03.setUpdateTime(System.currentTimeMillis());
				orderDetailB03.setLockVersion(0);
				addOrderDetailList.add(orderDetailB03);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B03)) {
				orderDetailB03.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
				orderDetailB03.setPlatformInAmt(orderDetailB03.getTransAmt());
				BigDecimal companyInAmt = orderDetailB03.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB03.setCompanyInAmt(companyInAmt);
				orderDetailB03.setUpdateUser(user.getId());
				orderDetailB03.setUpdateTime(System.currentTimeMillis());
				orderDetailB03.setLockVersion(orderDetailB03.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB03);
			} else {
				delOrderDetailList.add(orderDetailB03);
			}
		}
		InaccountOrderDetail detailB04 = new InaccountOrderDetail();
		detailB04.setBId("B04");
		detailB04.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB04 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB04);
		if (orderDetailB04 == null) {
			if (!StringUtil.isNullOrEmpty(B04)) {
				orderDetailB04 = new InaccountOrderDetail();
				orderDetailB04.setOrderListId(IdUtil.getNextId());
				orderDetailB04.setOrderId(order.getOrderId());
				orderDetailB04.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB04.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
				orderDetailB04.setBId("B04");
				orderDetailB04.setPlatformInAmt(orderDetailB04.getTransAmt());
				BigDecimal companyInAmt = orderDetailB04.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB04.setCompanyInAmt(companyInAmt);
				orderDetailB04.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB04.setCreateUser(user.getId());
				orderDetailB04.setUpdateUser(user.getId());
				orderDetailB04.setCreateTime(System.currentTimeMillis());
				orderDetailB04.setUpdateTime(System.currentTimeMillis());
				orderDetailB04.setLockVersion(0);
				addOrderDetailList.add(orderDetailB04);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B04)) {
				orderDetailB04.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
				orderDetailB04.setPlatformInAmt(orderDetailB04.getTransAmt());
				BigDecimal companyInAmt = orderDetailB04.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB04.setCompanyInAmt(companyInAmt);
				orderDetailB04.setUpdateUser(user.getId());
				orderDetailB04.setUpdateTime(System.currentTimeMillis());
				orderDetailB04.setLockVersion(orderDetailB04.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB04);
			} else {
				delOrderDetailList.add(orderDetailB04);
			}
		}
		InaccountOrderDetail detailB05 = new InaccountOrderDetail();
		detailB05.setBId("B05");
		detailB05.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB05 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB05);
		if (orderDetailB05 == null) {
			if (!StringUtil.isNullOrEmpty(B05)) {
				orderDetailB05 = new InaccountOrderDetail();
				orderDetailB05.setOrderListId(IdUtil.getNextId());
				orderDetailB05.setOrderId(order.getOrderId());
				orderDetailB05.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB05.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
				orderDetailB05.setBId("B05");
				orderDetailB05.setPlatformInAmt(orderDetailB05.getTransAmt());
				BigDecimal companyInAmt = orderDetailB05.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB05.setCompanyInAmt(companyInAmt);
				orderDetailB05.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB05.setCreateUser(user.getId());
				orderDetailB05.setUpdateUser(user.getId());
				orderDetailB05.setCreateTime(System.currentTimeMillis());
				orderDetailB05.setUpdateTime(System.currentTimeMillis());
				orderDetailB05.setLockVersion(0);
				addOrderDetailList.add(orderDetailB05);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B05)) {
				orderDetailB05.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
				orderDetailB05.setPlatformInAmt(orderDetailB05.getTransAmt());
				BigDecimal companyInAmt = orderDetailB05.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB05.setCompanyInAmt(companyInAmt);
				orderDetailB05.setUpdateUser(user.getId());
				orderDetailB05.setUpdateTime(System.currentTimeMillis());
				orderDetailB05.setLockVersion(orderDetailB05.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB05);
			} else {
				delOrderDetailList.add(orderDetailB05);
			}
		}
		InaccountOrderDetail detailB06 = new InaccountOrderDetail();
		detailB06.setBId("B06");
		detailB06.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB06 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB06);
		if (orderDetailB06 == null) {
			if (!StringUtil.isNullOrEmpty(B06)) {
				orderDetailB06 = new InaccountOrderDetail();
				orderDetailB06.setOrderListId(IdUtil.getNextId());
				orderDetailB06.setOrderId(order.getOrderId());
				orderDetailB06.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB06.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
				orderDetailB06.setBId("B06");
				orderDetailB06.setPlatformInAmt(orderDetailB06.getTransAmt());
				BigDecimal companyInAmt = orderDetailB06.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB06.setCompanyInAmt(companyInAmt);
				orderDetailB06.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB06.setCreateUser(user.getId());
				orderDetailB06.setUpdateUser(user.getId());
				orderDetailB06.setCreateTime(System.currentTimeMillis());
				orderDetailB06.setUpdateTime(System.currentTimeMillis());
				orderDetailB06.setLockVersion(0);
				addOrderDetailList.add(orderDetailB06);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B06)) {
				orderDetailB06.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
				orderDetailB06.setPlatformInAmt(orderDetailB06.getTransAmt());
				BigDecimal companyInAmt = orderDetailB06.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB06.setCompanyInAmt(companyInAmt);
				orderDetailB06.setUpdateUser(user.getId());
				orderDetailB06.setUpdateTime(System.currentTimeMillis());
				orderDetailB06.setLockVersion(orderDetailB06.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB06);
			} else {
				delOrderDetailList.add(orderDetailB06);
			}
		}
		InaccountOrderDetail detailB07 = new InaccountOrderDetail();
		detailB07.setBId("B07");
		detailB07.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB07 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB07);
		if (orderDetailB07 == null) {
			if (!StringUtil.isNullOrEmpty(B07)) {
				orderDetailB07 = new InaccountOrderDetail();
				orderDetailB07.setOrderListId(IdUtil.getNextId());
				orderDetailB07.setOrderId(order.getOrderId());
				orderDetailB07.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB07.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
				orderDetailB07.setBId("B07");
				orderDetailB07.setPlatformInAmt(orderDetailB07.getTransAmt());
				BigDecimal companyInAmt = orderDetailB07.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB07.setCompanyInAmt(companyInAmt);
				orderDetailB07.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB07.setCreateUser(user.getId());
				orderDetailB07.setUpdateUser(user.getId());
				orderDetailB07.setCreateTime(System.currentTimeMillis());
				orderDetailB07.setUpdateTime(System.currentTimeMillis());
				orderDetailB07.setLockVersion(0);
				addOrderDetailList.add(orderDetailB07);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B07)) {
				orderDetailB07.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
				orderDetailB07.setPlatformInAmt(orderDetailB07.getTransAmt());
				BigDecimal companyInAmt = orderDetailB07.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB07.setCompanyInAmt(companyInAmt);
				orderDetailB07.setUpdateUser(user.getId());
				orderDetailB07.setUpdateTime(System.currentTimeMillis());
				orderDetailB07.setLockVersion(orderDetailB07.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB07);
			} else {
				delOrderDetailList.add(orderDetailB07);
			}
		}
		InaccountOrderDetail detailB08 = new InaccountOrderDetail();
		detailB08.setBId("B08");
		detailB08.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailB08 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB08);
		if (orderDetailB08 == null) {
			if (!StringUtil.isNullOrEmpty(B08)) {
				orderDetailB08 = new InaccountOrderDetail();
				orderDetailB08.setOrderListId(IdUtil.getNextId());
				orderDetailB08.setOrderId(order.getOrderId());
				orderDetailB08.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB08.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
				orderDetailB08.setBId("B08");
				orderDetailB08.setPlatformInAmt(orderDetailB08.getTransAmt());
				BigDecimal companyInAmt = orderDetailB08.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB08.setCompanyInAmt(companyInAmt);
				orderDetailB08.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetailB08.setCreateUser(user.getId());
				orderDetailB08.setUpdateUser(user.getId());
				orderDetailB08.setCreateTime(System.currentTimeMillis());
				orderDetailB08.setUpdateTime(System.currentTimeMillis());
				orderDetailB08.setLockVersion(0);
				addOrderDetailList.add(orderDetailB08);
			}
		} else {
			if (!StringUtil.isNullOrEmpty(B08)) {
				orderDetailB08.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
				orderDetailB08.setPlatformInAmt(orderDetailB08.getTransAmt());
				BigDecimal companyInAmt = orderDetailB08.getTransAmt().divide(platformFeeSum, 0);
				orderDetailB08.setCompanyInAmt(companyInAmt);
				orderDetailB08.setUpdateUser(user.getId());
				orderDetailB08.setUpdateTime(System.currentTimeMillis());
				orderDetailB08.setLockVersion(orderDetailB08.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB08);
			} else {
				delOrderDetailList.add(orderDetailB08);
			}
		}

		if (!inaccountOrderService.saveOrUpdate(order)) {
			logger.error("## 更新入账订单信息失败");
			return 0;
		}

		if (addOrderDetailList != null && addOrderDetailList.size() >= 1) {
			if (!inaccountOrderDetailService.saveBatch(addOrderDetailList)) {
				logger.error("## 新增入账订单明细失败");
				return 0;
			}
		}

		if (editOrderDetailList != null && editOrderDetailList.size() >= 1) {
			if (!inaccountOrderDetailService.saveOrUpdateBatch(editOrderDetailList)) {
				logger.error("## 编辑入账订单明细失败");
				return 0;
			}
		}

		if (delOrderDetailList != null && delOrderDetailList.size() >= 1) {
			for (InaccountOrderDetail d : delOrderDetailList) {
				if (!inaccountOrderDetailService.removeById(d)) {
					logger.error("## 删除入账订单明细失败");
					return 0;
				}
			}
		}
		return 1;
	}

    @Override
    public Map<String, Object> deleteProviderTransfer(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String orderId = StringUtil.nullToString(req.getParameter("orderId"));

        InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
        List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);

        if (order == null || orderDetailList == null || orderDetailList.size() < 1) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "数据不存在，请重新查看订单信息");
            return resultMap;
        }

        order.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
        order.setUpdateTime(System.currentTimeMillis());
        order.setUpdateUser(user.getId());
        order.setLockVersion(order.getLockVersion() + 1);
        for (InaccountOrderDetail orderDetail : orderDetailList) {
            orderDetail.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);
        }
        if (!inaccountOrderService.saveOrUpdate(order)) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "删除失败，请稍后再试");
            return resultMap;
        }
        if (!inaccountOrderDetailService.saveOrUpdateBatch(orderDetailList)) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "删除失败，请稍后再试");
            return resultMap;
        }

	    return resultMap;
    }

}
