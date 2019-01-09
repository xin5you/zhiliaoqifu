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
	public Map<String, Object> addProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
		String companyCode = StringUtil.nullToString(req.getParameter("companyCode"));
		String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
		/*String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));*/
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

		BigDecimal inaccountAmtSum = new BigDecimal(0);
		if (!StringUtil.isNullOrEmpty(A00)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(A00));
		}
		if (!StringUtil.isNullOrEmpty(B01)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B01));
		}
		if (!StringUtil.isNullOrEmpty(B02)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B02));
		}
		if (!StringUtil.isNullOrEmpty(B03)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B03));
		}
		if (!StringUtil.isNullOrEmpty(B04)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B04));
		}
		if (!StringUtil.isNullOrEmpty(B05)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B05));
		}
		if (!StringUtil.isNullOrEmpty(B06)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B06));
		}
		if (!StringUtil.isNullOrEmpty(B07)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B07));
		}
		if (!StringUtil.isNullOrEmpty(B08)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B08));
		}

		if (inaccountAmtSum.compareTo(new BigDecimal(remitAmt)) != 0) {
			logger.error("## 供应商{}上账金额不正确，应上账总额{}", providerId, inaccountAmtSum);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加上账信息失败，供应商上账金额不正确");
			return resultMap;
		}

		CompanyInf company = companyInfFacade.getCompanyInfByLawCode(companyCode);

		/*String platformFee = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, RedisConstants.PLATFORM_FEE);
		if (StringUtil.isNullOrEmpty(platformFee)) {
            logger.error("## 新增供应商{}上账，获取平台费率失败", providerId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加上账信息失败，平台费率为空");
			return resultMap;
		}*/

		//BigDecimal platformFeeSum = new BigDecimal(platformFee).add(new BigDecimal(1));
		//BigDecimal companyInAmtSum = new BigDecimal(inaccountAmt).divide(platformFeeSum, 0);

		InaccountOrder order = new InaccountOrder();
		order.setOrderId(IdUtil.getNextId());
		order.setTfrPlatformOrderId(IdUtil.getNextId());
		order.setTfrCompanyOrderId(IdUtil.getNextId());
		order.setOrderType(UserType.TYPE300.getCode());
		order.setCheckStat(CheckStatEnum.CHECK_FALSE.getCode());
		order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));
		/*order.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setPlatformInSumAmt(order.getInaccountAmt());*/
		//order.setCompanyInSumAmt(companyInAmtSum);
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
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "上传凭证图片为空");
			return resultMap;
		}
		/*FTPImageVo imgVo = new FTPImageVo();
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
		}*/

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
		List<InaccountOrderDetail> orderDetailList = new ArrayList<InaccountOrderDetail>();
		if (!StringUtil.isNullOrEmpty(A00)) {
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("A00");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			}
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("A00");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("A00");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B01");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B01");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B01");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B02");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B02");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B02");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B03");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B03");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B03");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B04");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B04");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B04");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B05");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B05");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B05");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B06");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B06");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B06");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B07");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B07");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B07");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
			CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
			cbt.setCompanyId(company.getCompanyId());
			cbt.setBId("B08");
			CompanyBillingTypeInf companyBillingTypeInf = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
			if (companyBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInf.getFee()).add(new BigDecimal(1));
			/*if (companyBillingTypeInf == null) {
				companyFee = new BigDecimal(platformFee);
			} else {
				companyFee = new BigDecimal(companyBillingTypeInf.getFee());
			}*/
			ProviderBillingTypeInf pbt = new ProviderBillingTypeInf();
			pbt.setProviderId(providerId);
			pbt.setBId("B08");
			ProviderBillingTypeInf providerBillingTypeInf = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbt);
			if (providerBillingTypeInf == null) {
				logger.error("## 新增供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			providerFee = new BigDecimal(providerBillingTypeInf.getFee()).add(new BigDecimal(1));

			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
			orderDetail.setInaccountAmt(orderDetail.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
			orderDetail.setBId("B08");
			orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
			BigDecimal companyInAmt = orderDetail.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
			orderDetail.setCompanyInAmt(companyInAmt);
			orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			orderDetail.setCreateUser(user.getId());
			orderDetail.setUpdateUser(user.getId());
			orderDetail.setCreateTime(System.currentTimeMillis());
			orderDetail.setUpdateTime(System.currentTimeMillis());
			orderDetail.setLockVersion(0);
			orderDetailList.add(orderDetail);
		}

		BigDecimal inaccountInAmtSum = orderDetailList.stream().map(InaccountOrderDetail::getInaccountAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
		BigDecimal companyInAmtSum = orderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
		order.setInaccountSumAmt(inaccountInAmtSum);
		order.setPlatformInSumAmt(inaccountInAmtSum);
		order.setCompanyInSumAmt(companyInAmtSum);
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

		List<AccountTxnVo> transList = new ArrayList<>();
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
		String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
		String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
		String companyCode = StringUtil.nullToString(req.getParameter("companyCode"));
		/*String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));*/
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

		BigDecimal inaccountAmtSum = new BigDecimal(0);
		if (!StringUtil.isNullOrEmpty(A00)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(A00));
		}
		if (!StringUtil.isNullOrEmpty(B01)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B01));
		}
		if (!StringUtil.isNullOrEmpty(B02)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B02));
		}
		if (!StringUtil.isNullOrEmpty(B03)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B03));
		}
		if (!StringUtil.isNullOrEmpty(B04)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B04));
		}
		if (!StringUtil.isNullOrEmpty(B05)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B05));
		}
		if (!StringUtil.isNullOrEmpty(B06)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B06));
		}
		if (!StringUtil.isNullOrEmpty(B07)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B07));
		}
		if (!StringUtil.isNullOrEmpty(B08)) {
			inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(B08));
		}

		if (inaccountAmtSum.compareTo(new BigDecimal(remitAmt)) != 0) {
			logger.error("## 供应商{}上账金额不正确，应上账总额{}", providerId, inaccountAmtSum);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，供应商上账金额不正确");
			return resultMap;
		}

		CompanyInf company = companyInfFacade.getCompanyInfByLawCode(companyCode);
		if (company == null) {
			logger.error("## 根据企业代码{}查询企业信息为空", companyCode);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，供应商上账金额不正确");
			return resultMap;
		}

		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
		if (order == null) {
			logger.error("## 根据上账订单号{}查询订单信息为空", orderId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，查询供应商上账订单信息异常");
			return resultMap;
		}

		/*String platformFee = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, RedisConstants.PLATFORM_FEE);
		if (StringUtil.isNullOrEmpty(platformFee)) {
		    logger.error("## 编辑供应商{}上账，获取平台费率失败", providerId);
			return 0;
		}*/
		//BigDecimal platformFeeSum = new BigDecimal(platformFee).add(new BigDecimal(1));
		//BigDecimal companyInAmtSum = new BigDecimal(inaccountAmt).divide(platformFeeSum, 0);

		order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));
		/*order.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setPlatformInSumAmt(order.getInaccountAmt());*/
		//order.setCompanyInSumAmt(companyInAmtSum);
		order.setCompanyId(company.getCompanyId());
		order.setRemarks(remarks);
		order.setUpdateUser(user.getId());
		order.setUpdateTime(System.currentTimeMillis());
		order.setLockVersion(order.getLockVersion() + 1);

		/*if (evidenceUrlFile != null && !evidenceUrlFile.isEmpty()) {
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
		}*/
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

		List<InaccountOrderDetail> editOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> addOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> delOrderDetailList = new ArrayList<InaccountOrderDetail>();
		InaccountOrderDetail detailA00 = new InaccountOrderDetail();
		detailA00.setBId("A00");
		detailA00.setOrderId(order.getOrderId());
		InaccountOrderDetail orderDetailA00 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailA00);
		//企业专项类型费率
		CompanyBillingTypeInf cbtA00 = new CompanyBillingTypeInf();
		cbtA00.setCompanyId(company.getCompanyId());
		cbtA00.setBId("A00");
		CompanyBillingTypeInf companyBillingTypeInfA00 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtA00);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtA00 = new ProviderBillingTypeInf();
		pbtA00.setProviderId(providerId);
		pbtA00.setBId("A00");
		ProviderBillingTypeInf providerBillingTypeInfA00 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtA00);
		if (!StringUtil.isNullOrEmpty(A00)) {
			if (companyBillingTypeInfA00 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfA00 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfA00.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfA00.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfA00 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfA00.getFee());
		}*/
		if (orderDetailA00 == null) {
			if (!StringUtil.isNullOrEmpty(A00)) {
				orderDetailA00 = new InaccountOrderDetail();
				orderDetailA00.setOrderListId(IdUtil.getNextId());
				orderDetailA00.setOrderId(order.getOrderId());
				orderDetailA00.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailA00.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
				orderDetailA00.setInaccountAmt(orderDetailA00.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailA00.setBId("A00");
				orderDetailA00.setPlatformInAmt(orderDetailA00.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailA00.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailA00.setInaccountAmt(orderDetailA00.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailA00.setPlatformInAmt(orderDetailA00.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailA00.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB01 = new CompanyBillingTypeInf();
		cbtB01.setCompanyId(company.getCompanyId());
		cbtB01.setBId("B01");
		CompanyBillingTypeInf companyBillingTypeInfB01 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB01);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB01 = new ProviderBillingTypeInf();
		pbtB01.setProviderId(providerId);
		pbtB01.setBId("B01");
		ProviderBillingTypeInf providerBillingTypeInfB01 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB01);
		if (!StringUtil.isNullOrEmpty(B01)) {
			if (companyBillingTypeInfB01 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB01 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB01.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB01.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB01 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB01.getFee());
		}*/
		if (orderDetailB01 == null) {
			if (!StringUtil.isNullOrEmpty(B01)) {
				orderDetailB01 = new InaccountOrderDetail();
				orderDetailB01.setOrderListId(IdUtil.getNextId());
				orderDetailB01.setOrderId(order.getOrderId());
				orderDetailB01.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB01.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
				orderDetailB01.setInaccountAmt(orderDetailB01.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB01.setBId("B01");
				orderDetailB01.setPlatformInAmt(orderDetailB01.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB01.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB01.setInaccountAmt(orderDetailB01.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB01.setPlatformInAmt(orderDetailB01.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB01.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB02 = new CompanyBillingTypeInf();
		cbtB02.setCompanyId(company.getCompanyId());
		cbtB02.setBId("B02");
		CompanyBillingTypeInf companyBillingTypeInfB02 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB02);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB02 = new ProviderBillingTypeInf();
		pbtB02.setProviderId(providerId);
		pbtB02.setBId("B02");
		ProviderBillingTypeInf providerBillingTypeInfB02 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB02);
		if (!StringUtil.isNullOrEmpty(B02)) {
			if (companyBillingTypeInfB02 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB02 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB02.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB02.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB02 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB02.getFee());
		}*/
		if (orderDetailB02 == null) {
			if (!StringUtil.isNullOrEmpty(B02)) {
				orderDetailB02 = new InaccountOrderDetail();
				orderDetailB02.setOrderListId(IdUtil.getNextId());
				orderDetailB02.setOrderId(order.getOrderId());
				orderDetailB02.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB02.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
				orderDetailB02.setInaccountAmt(orderDetailB02.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB02.setBId("B02");
				orderDetailB02.setPlatformInAmt(orderDetailB02.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB02.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB02.setInaccountAmt(orderDetailB02.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB02.setPlatformInAmt(orderDetailB02.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB02.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB03 = new CompanyBillingTypeInf();
		cbtB03.setCompanyId(company.getCompanyId());
		cbtB03.setBId("B03");
		CompanyBillingTypeInf companyBillingTypeInfB03 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB03);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB03 = new ProviderBillingTypeInf();
		pbtB03.setProviderId(providerId);
		pbtB03.setBId("B03");
		ProviderBillingTypeInf providerBillingTypeInfB03 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB03);
		if (!StringUtil.isNullOrEmpty(B03)) {
			if (companyBillingTypeInfB03 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB03 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB03.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB03.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB03 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB03.getFee());
		}*/
		if (orderDetailB03 == null) {
			if (!StringUtil.isNullOrEmpty(B03)) {
				orderDetailB03 = new InaccountOrderDetail();
				orderDetailB03.setOrderListId(IdUtil.getNextId());
				orderDetailB03.setOrderId(order.getOrderId());
				orderDetailB03.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB03.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
				orderDetailB03.setInaccountAmt(orderDetailB03.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB03.setBId("B03");
				orderDetailB03.setPlatformInAmt(orderDetailB03.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB03.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB03.setInaccountAmt(orderDetailB03.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB03.setPlatformInAmt(orderDetailB03.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB03.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB04 = new CompanyBillingTypeInf();
		cbtB04.setCompanyId(company.getCompanyId());
		cbtB04.setBId("B04");
		CompanyBillingTypeInf companyBillingTypeInfB04 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB04);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB04 = new ProviderBillingTypeInf();
		pbtB04.setProviderId(providerId);
		pbtB04.setBId("B01");
		ProviderBillingTypeInf providerBillingTypeInfB04 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB04);
		if (!StringUtil.isNullOrEmpty(B04)) {
			if (companyBillingTypeInfB01 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB04 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB01.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB04.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB04 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB04.getFee());
		}*/
		if (orderDetailB04 == null) {
			if (!StringUtil.isNullOrEmpty(B04)) {
				orderDetailB04 = new InaccountOrderDetail();
				orderDetailB04.setOrderListId(IdUtil.getNextId());
				orderDetailB04.setOrderId(order.getOrderId());
				orderDetailB04.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB04.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
				orderDetailB04.setInaccountAmt(orderDetailB04.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB04.setBId("B04");
				orderDetailB04.setPlatformInAmt(orderDetailB04.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB04.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB04.setInaccountAmt(orderDetailB04.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB04.setPlatformInAmt(orderDetailB04.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB04.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB05 = new CompanyBillingTypeInf();
		cbtB05.setCompanyId(company.getCompanyId());
		cbtB05.setBId("B05");
		CompanyBillingTypeInf companyBillingTypeInfB05 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB05);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB05 = new ProviderBillingTypeInf();
		pbtB05.setProviderId(providerId);
		pbtB05.setBId("B05");
		ProviderBillingTypeInf providerBillingTypeInfB05 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB05);
		if (!StringUtil.isNullOrEmpty(B05)) {
			if (companyBillingTypeInfB05 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB05 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB05.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB05.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB05 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB05.getFee());
		}*/
		if (orderDetailB05 == null) {
			if (!StringUtil.isNullOrEmpty(B05)) {
				orderDetailB05 = new InaccountOrderDetail();
				orderDetailB05.setOrderListId(IdUtil.getNextId());
				orderDetailB05.setOrderId(order.getOrderId());
				orderDetailB05.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB05.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
				orderDetailB05.setInaccountAmt(orderDetailB05.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB05.setBId("B05");
				orderDetailB05.setPlatformInAmt(orderDetailB05.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB05.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB05.setInaccountAmt(orderDetailB05.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB05.setPlatformInAmt(orderDetailB05.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB05.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB06 = new CompanyBillingTypeInf();
		cbtB06.setCompanyId(company.getCompanyId());
		cbtB06.setBId("B06");
		CompanyBillingTypeInf companyBillingTypeInfB06 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB06);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB06 = new ProviderBillingTypeInf();
		pbtB06.setProviderId(providerId);
		pbtB06.setBId("B06");
		ProviderBillingTypeInf providerBillingTypeInfB06 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB06);
		if (!StringUtil.isNullOrEmpty(B06)) {
			if (companyBillingTypeInfB06 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB06 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB01.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB06.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB06 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB06.getFee());
		}*/
		if (orderDetailB06 == null) {
			if (!StringUtil.isNullOrEmpty(B06)) {
				orderDetailB06 = new InaccountOrderDetail();
				orderDetailB06.setOrderListId(IdUtil.getNextId());
				orderDetailB06.setOrderId(order.getOrderId());
				orderDetailB06.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB06.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
				orderDetailB06.setInaccountAmt(orderDetailB06.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB06.setBId("B06");
				orderDetailB06.setPlatformInAmt(orderDetailB06.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB06.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB06.setInaccountAmt(orderDetailB06.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB06.setPlatformInAmt(orderDetailB06.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB06.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB07 = new CompanyBillingTypeInf();
		cbtB07.setCompanyId(company.getCompanyId());
		cbtB07.setBId("B07");
		CompanyBillingTypeInf companyBillingTypeInfB07 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB07);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB07 = new ProviderBillingTypeInf();
		pbtB07.setProviderId(providerId);
		pbtB07.setBId("B07");
		ProviderBillingTypeInf providerBillingTypeInfB07 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB07);
		if (!StringUtil.isNullOrEmpty(B07)) {
			if (companyBillingTypeInfB07 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB07 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB01.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB07.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB07 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB07.getFee());
		}*/
		if (orderDetailB07 == null) {
			if (!StringUtil.isNullOrEmpty(B07)) {
				orderDetailB07 = new InaccountOrderDetail();
				orderDetailB07.setOrderListId(IdUtil.getNextId());
				orderDetailB07.setOrderId(order.getOrderId());
				orderDetailB07.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB07.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
				orderDetailB07.setInaccountAmt(orderDetailB07.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB07.setBId("B07");
				orderDetailB07.setPlatformInAmt(orderDetailB07.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB07.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB07.setInaccountAmt(orderDetailB07.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB07.setPlatformInAmt(orderDetailB07.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB07.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
		//企业专项类型费率
		CompanyBillingTypeInf cbtB08 = new CompanyBillingTypeInf();
		cbtB08.setCompanyId(company.getCompanyId());
		cbtB08.setBId("B08");
		CompanyBillingTypeInf companyBillingTypeInfB08 = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbtB08);
		//供应商专项类型费率
		ProviderBillingTypeInf pbtB08 = new ProviderBillingTypeInf();
		pbtB08.setProviderId(providerId);
		pbtB08.setBId("B08");
		ProviderBillingTypeInf providerBillingTypeInfB08 = providerInfFacade.getProviderBillingTypeInfByBIdAndProviderId(pbtB08);
		if (!StringUtil.isNullOrEmpty(B08)) {
			if (companyBillingTypeInfB08 == null) {
				logger.error("## 编辑供应商{}上账，获取企业费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查企业费率信息是否正确");
				return resultMap;
			}
			if (providerBillingTypeInfB08 == null) {
				logger.error("## 编辑供应商{}上账，获取供应商费率失败", providerId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请检查供应商费率信息是否正确");
				return resultMap;
			}
			companyFee = new BigDecimal(companyBillingTypeInfB01.getFee()).add(new BigDecimal(1));
			providerFee = new BigDecimal(providerBillingTypeInfB08.getFee()).add(new BigDecimal(1));
		}
		/*if (companyBillingTypeInfB08 == null) {
			companyFee = new BigDecimal(platformFee);
		} else {
			companyFee = new BigDecimal(companyBillingTypeInfB08.getFee());
		}*/
		if (orderDetailB08 == null) {
			if (!StringUtil.isNullOrEmpty(B08)) {
				orderDetailB08 = new InaccountOrderDetail();
				orderDetailB08.setOrderListId(IdUtil.getNextId());
				orderDetailB08.setOrderId(order.getOrderId());
				orderDetailB08.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetailB08.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
				orderDetailB08.setInaccountAmt(orderDetailB08.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB08.setBId("B08");
				orderDetailB08.setPlatformInAmt(orderDetailB08.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB08.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
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
				orderDetailB08.setInaccountAmt(orderDetailB08.getTransAmt().divide(providerFee, 0, BigDecimal.ROUND_CEILING));
				orderDetailB08.setPlatformInAmt(orderDetailB08.getInaccountAmt());
				BigDecimal companyInAmt = orderDetailB08.getInaccountAmt().divide(companyFee, 0, BigDecimal.ROUND_CEILING);
				orderDetailB08.setCompanyInAmt(companyInAmt);
				orderDetailB08.setUpdateUser(user.getId());
				orderDetailB08.setUpdateTime(System.currentTimeMillis());
				orderDetailB08.setLockVersion(orderDetailB08.getLockVersion() + 1);
				editOrderDetailList.add(orderDetailB08);
			} else {
				delOrderDetailList.add(orderDetailB08);
			}
		}

		BigDecimal companyInAmtSum = new BigDecimal(0);
		BigDecimal inaccountInAmtSum = new BigDecimal(0);
		if (addOrderDetailList != null && addOrderDetailList.size() >= 1) {
			BigDecimal addOrderDetailCompanyInAmtSum = addOrderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
			companyInAmtSum = addOrderDetailCompanyInAmtSum;
			BigDecimal addOrderDetailAccountInAmtSum = addOrderDetailList.stream().map(InaccountOrderDetail::getInaccountAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
			inaccountInAmtSum = addOrderDetailAccountInAmtSum;
			if (!inaccountOrderDetailService.saveBatch(addOrderDetailList)) {
				logger.error("## 编辑上账订单明细失败");
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账订单明细信息失败");
				return resultMap;
			}
		}

		if (editOrderDetailList != null && editOrderDetailList.size() >= 1) {
			BigDecimal editOrderDetailCompanyInAmtSum = editOrderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
			companyInAmtSum = companyInAmtSum.add(editOrderDetailCompanyInAmtSum);
			BigDecimal editOrderDetailAccountInAmtSum = editOrderDetailList.stream().map(InaccountOrderDetail::getInaccountAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
			inaccountInAmtSum = inaccountInAmtSum.add(editOrderDetailAccountInAmtSum);
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

		order.setInaccountSumAmt(inaccountInAmtSum.setScale(0, BigDecimal.ROUND_UP));
		order.setPlatformInSumAmt(inaccountInAmtSum.setScale(0, BigDecimal.ROUND_UP));
		order.setCompanyInSumAmt(companyInAmtSum.setScale(0, BigDecimal.ROUND_UP));
		if (!inaccountOrderService.saveOrUpdate(order)) {
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
