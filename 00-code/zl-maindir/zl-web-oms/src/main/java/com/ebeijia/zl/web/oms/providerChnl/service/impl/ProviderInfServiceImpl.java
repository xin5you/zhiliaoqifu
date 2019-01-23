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
import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Service("providerInfService")
public class ProviderInfServiceImpl implements ProviderInfService {
	Logger logger = LoggerFactory.getLogger(getClass());

//	public static void main(String[] args) {
//		BigDecimal b1 = new BigDecimal("1000");
//		BigDecimal b2 = new BigDecimal("1.06");
//		//ROUND_DOWN
//		//ROUND_DOWN
//		BigDecimal b3 = b1.divide(b2, 0, BigDecimal.ROUND_DOWN);
//		System.out.println(b3);
//
//	}

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
	public Map<String, Object> addProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
		String companyCode = StringUtil.nullToString(req.getParameter("companyCode"));
		/*String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));*/
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
		String formula = StringUtil.nullToString(req.getParameter("formula"));

		if (StringUtil.isNullOrEmpty(formula)) {
			logger.error("## 金额计算方式为空");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加上账信息失败，金额计算方式不能为空");
			return resultMap;
		}

		Map<String, String> bMap = new HashMap<>();
		bMap.put("A00", A00);
		bMap.put("B01", B01);
		bMap.put("B02", B02);
		bMap.put("B03", B03);
		bMap.put("B04", B04);
		bMap.put("B05", B05);
		bMap.put("B06", B06);
		bMap.put("B07", B07);
		bMap.put("B08", B08);

		BigDecimal inaccountAmtSum = new BigDecimal(0);
		for(Map.Entry<String, String> entry : bMap.entrySet()) {
			if (!StringUtil.isNullOrEmpty(entry.getValue())) {
				inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(entry.getValue()));
			}
		}

		if (inaccountAmtSum.compareTo(new BigDecimal(inaccountAmt)) != 0) {
			logger.error("## 供应商{}上账金额不正确，应上账总额{}", providerId, inaccountAmtSum);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加上账信息失败，供应商上账金额不正确");
			return resultMap;
		}

		CompanyInf company = companyInfFacade.getCompanyInfById(companyCode);
		if (company == null || company.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加上账信息失败，收款企业不存在或未开户");
			return resultMap;
		}

		InaccountOrder order = new InaccountOrder();
		order.setOrderId(IdUtil.getNextId());
		order.setTfrPlatformOrderId(IdUtil.getNextId());
		order.setTfrCompanyOrderId(IdUtil.getNextId());
		order.setOrderType(UserType.TYPE300.getCode());
		order.setCheckStat(CheckStatEnum.CHECK_FALSE.getCode());
		//order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		//order.setPlatformInSumAmt(order.getInaccountSumAmt());
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
		order.setFormula(formula);

		if (evidenceUrlFile == null || evidenceUrlFile.isEmpty()) {
			logger.error("## 上传图片为空");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "上传凭证图片为空");
			return resultMap;
		}

		resultMap = commonService.uploadImage(evidenceUrlFile, req, ImageTypeEnum.ImageTypeEnum_03.getValue(), order.getOrderId());
		if (!String.valueOf(resultMap.get("status").toString()).equals("true")) {
			logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
			order.setEvidenceUrl("");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "凭证图片上传失败");
			return resultMap;
		}
		order.setEvidenceUrl(resultMap.get("msg").toString());

		BigDecimal companyFee = new BigDecimal(0);
		BigDecimal providerFee = new BigDecimal(0);
		BigDecimal companyInAmt = new BigDecimal(0);
		BigDecimal transAmt = new BigDecimal(0);
		List<InaccountOrderDetail> orderDetailList = new ArrayList<InaccountOrderDetail>();

		for(Map.Entry<String, String> entry : bMap.entrySet()) {
			if (!StringUtil.isNullOrEmpty(entry.getValue())) {
				ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
				pbt.setProviderId(providerId);
				pbt.setBId(entry.getKey());
				ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
				if (providerBillingTypeInf == null) {
					logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
					return resultMap;
				}

				CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
				cbt.setCompanyId(company.getCompanyId());
				cbt.setBId(entry.getKey());
				CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
				if (companyBillingTypeInf == null) {
					logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
					return resultMap;
				}
				providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));
				transAmt = new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())).multiply(providerFee).setScale(0, BigDecimal.ROUND_DOWN);

				if (InaccountFormulaEnum.InaccountFormulaEnum_1.getCode().equals(formula)) {
					companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(providerFee);
					companyInAmt = transAmt.divide(companyFee, 0, BigDecimal.ROUND_DOWN);
				} else {
					providerFee = new BigDecimal(providerBillingTypeInf.getFee());
					companyFee = new BigDecimal(companyBillingTypeInf.getFee());
					companyFee = new BigDecimal(1).subtract(providerFee).subtract(companyFee);
					companyInAmt = transAmt.multiply(companyFee);
				}

				InaccountOrderDetail orderDetail = new InaccountOrderDetail();
				orderDetail.setOrderListId(IdUtil.getNextId());
				orderDetail.setOrderId(order.getOrderId());
				orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetail.setTransAmt(transAmt);
				orderDetail.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
				orderDetail.setBId(entry.getKey());
				orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
				orderDetail.setCompanyInAmt(companyInAmt);
				if (orderDetail.getCompanyInAmt().compareTo(orderDetail.getPlatformInAmt()) == 1) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "企业费率不合法，请重新调整");
					return resultMap;
				}
				orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetail.setCreateUser(user.getId());
				orderDetail.setUpdateUser(user.getId());
				orderDetail.setCreateTime(System.currentTimeMillis());
				orderDetail.setUpdateTime(System.currentTimeMillis());
				orderDetail.setLockVersion(0);
				orderDetailList.add(orderDetail);
			}
		}

		BigDecimal remitInAmtSum = orderDetailList.stream().map(InaccountOrderDetail::getTransAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal companyInAmtSum = orderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
		order.setRemitAmt(remitInAmtSum);
		order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmtSum.toString())));
		order.setPlatformInSumAmt(order.getInaccountSumAmt());
		order.setCompanyInSumAmt(companyInAmtSum);
		if (order.getCompanyInSumAmt().compareTo(order.getPlatformInSumAmt()) == 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "企业费率不合法，请重新调整");
			return resultMap;
		}
		if (!inaccountOrderService.save(order)) {
			logger.error("## 新增入账订单信息失败");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "上账订单信息新增失败");
			return resultMap;
		}

		if (!inaccountOrderDetailService.saveBatch(orderDetailList)) {
			logger.error("## 新增入账订单明细失败");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "上账订单明细信息新增失败");
			return resultMap;
		}
		resultMap.put("status", Boolean.TRUE);
		return resultMap;
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

		/*List<AccountTxnVo> transList = new ArrayList<>();
		Set<String> bIds = new TreeSet<>();
		for (InaccountOrderDetail d : orderDetail) {
			AccountTxnVo txnVo = new AccountTxnVo();
			txnVo.setTxnBId(d.getBId());
			txnVo.setTxnAmt(d.getInaccountAmt());
			txnVo.setUpLoadAmt(d.getInaccountAmt());
			transList.add(txnVo);
			bIds.add(d.getBId());
		}

		AccountRechargeReqVo reqVo = new AccountRechargeReqVo();
		reqVo.setFromCompanyId(providerId);
		reqVo.setTransAmt(order.getInaccountSumAmt());
		reqVo.setUploadAmt(order.getInaccountSumAmt());
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
			logger.error("## 供应商充值，远程调用充值接口异常", e);
		}
		try {
			if (StringUtil.isNullOrEmpty(result.getCode())) {
				logger.info("供应商充值，远程调用查询接口请求参数--->dmsRelatedKey{},--->transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				logger.info("供应商充值，远程调用查询接口返回参数--->{}", JSONArray.toJSONString(result));
			}
		} catch (Exception e) {
			logger.error("## 供应商充值，远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
		}
		if (result == null) {
			logger.error("## 供应商充值，远程调用充值接口失败，返回参数为空，orderId--->{},providerId--->{}", orderId, providerId);
			return 0;
		}

		if (result.getCode().equals(Constants.SUCCESS_CODE)) {
			order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_TRUE.getCode());
		} else {
			order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
			order.setRemarks(result.getMsg());
		}*/
		order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_TRUE.getCode());
		if (!inaccountOrderService.updateById(order)) {
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
		reqVo.setTransAmt(order.getInaccountSumAmt());
		reqVo.setUploadAmt(order.getInaccountSumAmt());
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
		logger.info("供应商转账，远程调用转账接口请求参数--->{}", JSONArray.toJSONString(reqVo));
		BaseResult result = new BaseResult();
		try {
			result = accountTransactionFacade.executeTransfer(reqVo);
		} catch (Exception e) {
			logger.error("## 供应商转账，远程调用转账接口异常", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
		}
		logger.info("供应商转账，远程调用转账接口返回参数--->{}", JSONArray.toJSONString(result));
		try {
			if (StringUtil.isNullOrEmpty(result.getCode())) {
				logger.info("供应商转账，远程调用查询接口请求参数--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				logger.info("供应商转账，远程调用查询接口请求参数--->{}", JSONArray.toJSONString(result));
			}
		} catch (Exception e) {
			logger.error("## 供应商转账，远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
		}
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
	public Map<String, Object> editProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		/*String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));*/
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
		String formula = StringUtil.nullToString(req.getParameter("formula"));
		if (StringUtil.isNullOrEmpty(formula)) {
			logger.error("## 金额计算方式为空");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，金额计算方式不能为空");
			return resultMap;
		}

		Map<String, String> bMap = new HashMap<>();
		bMap.put("A00", A00);
		bMap.put("B01", B01);
		bMap.put("B02", B02);
		bMap.put("B03", B03);
		bMap.put("B04", B04);
		bMap.put("B05", B05);
		bMap.put("B06", B06);
		bMap.put("B07", B07);
		bMap.put("B08", B08);

		BigDecimal inaccountAmtSum = new BigDecimal(0);
		for(Map.Entry<String, String> entry : bMap.entrySet()) {
			if (!StringUtil.isNullOrEmpty(entry.getValue())) {
				inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(entry.getValue()));
			}
		}

		if (inaccountAmtSum.compareTo(new BigDecimal(inaccountAmt)) != 0) {
			logger.error("## 供应商{}上账金额不正确，应上账总额{}", providerId, inaccountAmtSum);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，供应商上账金额不正确");
			return resultMap;
		}

		CompanyInf company = companyInfFacade.getCompanyInfById(companyCode);
		if (company == null || company.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，收款企业不存在或未开户");
			return resultMap;
		}

		InaccountOrder orderInf = new InaccountOrder();
		orderInf.setOrderId(orderId);
		orderInf.setOrderType(UserType.TYPE300.getCode());
		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderInf);
		if (order == null) {
			logger.error("## 根据上账订单号{}查询订单信息为空", orderId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，查询供应商上账订单信息异常");
			return resultMap;
		}

		/*order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));*/
		//order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		//order.setPlatformInSumAmt(order.getInaccountSumAmt());
		//order.setCompanyInSumAmt(companyInAmtSum);
		order.setCompanyId(company.getCompanyId());
		order.setRemarks(remarks);
		order.setUpdateUser(user.getId());
		order.setUpdateTime(System.currentTimeMillis());
		order.setLockVersion(order.getLockVersion() + 1);
		order.setFormula(formula);

		if (evidenceUrlFile != null && !evidenceUrlFile.isEmpty()) {
			resultMap = commonService.uploadImage(evidenceUrlFile, req, ImageTypeEnum.ImageTypeEnum_03.getValue(), order.getOrderId());
			if (!String.valueOf(resultMap.get("status").toString()).equals("true")) {
				logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
				order.setEvidenceUrl("");
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "上传凭证图片为空");
				return resultMap;
			} else {
				order.setEvidenceUrl(resultMap.get("msg").toString());
			}
		}

		BigDecimal companyFee = new BigDecimal(0);
		BigDecimal providerFee = new BigDecimal(0);
		BigDecimal companyInAmt = new BigDecimal(0);
		BigDecimal transAmt = new BigDecimal(0);

		List<InaccountOrderDetail> editOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> addOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> delOrderDetailList = new ArrayList<InaccountOrderDetail>();

		for(Map.Entry<String, String> entry : bMap.entrySet()) {
			//企业专项类型费率
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId(entry.getKey());
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			//供应商专项类型费率
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId(entry.getKey());
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (!StringUtil.isNullOrEmpty(entry.getValue())) {
				if (companyBillingTypeInf == null) {
					logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
					return resultMap;
				}
				if (providerBillingTypeInf == null) {
					logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
					return resultMap;
				}

				providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));
				transAmt = new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())).multiply(providerFee).setScale(0, BigDecimal.ROUND_DOWN);

				if (InaccountFormulaEnum.InaccountFormulaEnum_1.getCode().equals(formula)) {
					companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(providerFee);
					companyInAmt = transAmt.divide(companyFee, 0, BigDecimal.ROUND_DOWN);
				} else {
					providerFee = new BigDecimal(providerBillingTypeInf.getFee());
					companyFee = new BigDecimal(companyBillingTypeInf.getFee());
					companyFee = new BigDecimal(1).subtract(providerFee).subtract(companyFee);
					companyInAmt = transAmt.multiply(companyFee);
				}
			}

			InaccountOrderDetail detail = new InaccountOrderDetail();
			detail.setBId(entry.getKey());
			detail.setOrderId(order.getOrderId());
			InaccountOrderDetail orderDetail = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detail);

			if (orderDetail == null) {
				if (!StringUtil.isNullOrEmpty(entry.getValue())) {
					orderDetail = new InaccountOrderDetail();
					orderDetail.setOrderListId(IdUtil.getNextId());
					orderDetail.setOrderId(order.getOrderId());
					orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
					orderDetail.setTransAmt(transAmt);
					orderDetail.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
					orderDetail.setBId(entry.getKey());
					orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
					orderDetail.setCompanyInAmt(companyInAmt);
					if (orderDetail.getCompanyInAmt().compareTo(orderDetail.getPlatformInAmt()) == 1) {
						resultMap.put("status", Boolean.FALSE);
						resultMap.put("msg", "企业费率不合法，请重新输入");
						return resultMap;
					}
					orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
					orderDetail.setCreateUser(user.getId());
					orderDetail.setUpdateUser(user.getId());
					orderDetail.setCreateTime(System.currentTimeMillis());
					orderDetail.setUpdateTime(System.currentTimeMillis());
					orderDetail.setLockVersion(0);
					addOrderDetailList.add(orderDetail);
				}
			} else {
				if (!StringUtil.isNullOrEmpty(entry.getValue())) {
					orderDetail.setTransAmt(transAmt);
					orderDetail.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
					orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
					orderDetail.setCompanyInAmt(companyInAmt);
					if (orderDetail.getCompanyInAmt().compareTo(orderDetail.getPlatformInAmt()) == 1) {
						resultMap.put("status", Boolean.FALSE);
						resultMap.put("msg", "企业费率不合法，请重新输入");
						return resultMap;
					}
					orderDetail.setUpdateUser(user.getId());
					orderDetail.setUpdateTime(System.currentTimeMillis());
					orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);
					editOrderDetailList.add(orderDetail);
				} else {
					delOrderDetailList.add(orderDetail);
				}
			}
		}

		BigDecimal companyInAmtSum = new BigDecimal(0);
		BigDecimal remitInAmtSum = new BigDecimal(0);
		if (addOrderDetailList != null && addOrderDetailList.size() >= 1) {
			BigDecimal addOrderDetailCompanyInAmtSum = addOrderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			companyInAmtSum = addOrderDetailCompanyInAmtSum;
			BigDecimal addOrderDetailRemitInAmtSum = addOrderDetailList.stream().map(InaccountOrderDetail::getTransAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			remitInAmtSum = addOrderDetailRemitInAmtSum;
			if (!inaccountOrderDetailService.saveBatch(addOrderDetailList)) {
				logger.error("## 编辑上账订单明细失败");
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账订单明细信息失败");
				return resultMap;
			}
		}

		if (editOrderDetailList != null && editOrderDetailList.size() >= 1) {
			BigDecimal editOrderDetailCompanyInAmtSum = editOrderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			companyInAmtSum = companyInAmtSum.add(editOrderDetailCompanyInAmtSum);
			BigDecimal editOrderDetailRemitInAmtSum = editOrderDetailList.stream().map(InaccountOrderDetail::getTransAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			remitInAmtSum = remitInAmtSum.add(editOrderDetailRemitInAmtSum);
			if (!inaccountOrderDetailService.saveOrUpdateBatch(editOrderDetailList)) {
				logger.error("## 编辑上账订单明细失败");
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账订单明细信息失败");
				return resultMap;
			}
		}

		if (delOrderDetailList != null && delOrderDetailList.size() >= 1) {
			for (InaccountOrderDetail d : delOrderDetailList) {
				if (!inaccountOrderDetailService.removeById(d)) {
					logger.error("## 删除上账订单明细失败");
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "编辑上账订单明细信息失败");
					return resultMap;
				}
			}
		}

		order.setRemitAmt(remitInAmtSum);
		order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmtSum.toString())));
		order.setPlatformInSumAmt(order.getInaccountSumAmt());
		order.setCompanyInSumAmt(companyInAmtSum);
		if (order.getCompanyInSumAmt().compareTo(order.getPlatformInSumAmt()) == 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "企业费率不合法，请重新输入");
			return resultMap;
		}
		if (!inaccountOrderService.updateById(order)) {
			logger.error("## 更新上账订单信息失败");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账订单信息失败");
			return resultMap;
		}

		resultMap.put("status", Boolean.TRUE);
		return resultMap;
	}

    @Override
    public Map<String, Object> deleteProviderTransfer(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String orderId = StringUtil.nullToString(req.getParameter("orderId"));

		InaccountOrder orderInf = new InaccountOrder();
		orderInf.setOrderId(orderId);
		orderInf.setOrderType(UserType.TYPE300.getCode());
        InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderInf);
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

	@Override
	public ModelMap addProvider(ProviderInf providerInf) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);

		try {
			ProviderInf providerLawCod = providerInfFacade.getProviderInfByLawCode(providerInf.getLawCode());
			if (providerLawCod != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "供应商代码已存在，请重新输入");
				return resultMap;
			}
			ProviderInf providerOperSolr = providerInfFacade.getProviderInfByOperSolr(providerInf.getOperSolr());
			if (providerOperSolr != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "操作顺序已存在，请重新输入");
				return resultMap;
			}
			ProviderInf providerName = providerInfFacade.getProviderInfByProviderName(providerInf.getProviderName());
			if (providerName != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "供应商名称已存在，请重新输入");
				return resultMap;
			}
			if(!providerInfFacade.saveProviderInf(providerInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增供应商失败，请稍后再试");
			}
		} catch (Exception e) {
			logger.error("## 新增供应商信息异常", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增供应商异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public ModelMap editProvider(ProviderInf providerInf) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);

		try {
			ProviderInf providerLawCode = providerInfFacade.getProviderInfByLawCode(providerInf.getLawCode());
			if (providerLawCode != null && !providerLawCode.getProviderId().equals(providerInf.getProviderId())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "供应商代码已存在，请重新输入");
				return resultMap;
			}
			ProviderInf providerOperSolr = providerInfFacade.getProviderInfByOperSolr(providerInf.getOperSolr());
			if (providerOperSolr != null && !providerOperSolr.getProviderId().equals(providerInf.getProviderId())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "操作顺序已存在，请重新输入");
				return resultMap;
			}
			ProviderInf providerName = providerInfFacade.getProviderInfByProviderName(providerInf.getProviderName());
			if (providerName != null && !providerName.getProviderId().equals(providerInf.getProviderId())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "供应商名称已存在，请重新输入");
				return resultMap;
			}
			if (!providerInfFacade.updateProviderInf(providerInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑供应商失败，请联系管理员");
			}
		} catch (Exception e) {
			logger.error("## 编辑供应商信息异常", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑供应商异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

}
