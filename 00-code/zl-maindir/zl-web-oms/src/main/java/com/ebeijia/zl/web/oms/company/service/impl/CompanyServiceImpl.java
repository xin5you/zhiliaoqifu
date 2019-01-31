package com.ebeijia.zl.web.oms.company.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.service.ITbCouponHolderService;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.req.AccountTransferReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.ebeijia.zl.web.oms.common.util.OrderConstants;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.company.service.CompanyService;
import org.springframework.web.multipart.MultipartFile;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService{
	
	Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

	@Autowired
	private CompanyInfFacade companyInfFacade;

	@Autowired
	private RetailChnlInfFacade retailChnlInfFacade;

	@Autowired
	private AccountTransactionFacade accountTransactionFacade;

	@Autowired
	private AccountQueryFacade accountQueryFacade;

	@Override
	public int openAccountCompany(HttpServletRequest req) {
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		CompanyInf companyInf = null;
		try {
			companyInf = companyInfFacade.getCompanyInfById(companyId);
			if (StringUtil.isNullOrEmpty(companyInf)) {
				logger.error("## 查询企业信息失败，companyId--->{}", companyId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 查询企业{}信息失败", companyId);
			return 0;
		}
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		BatchOrderList orderList = new BatchOrderList();
		orderList.setUserName(companyInf.getName());
		orderList.setPhoneNo(companyInf.getPhoneNo());
		orderList.setAccountType(UserType.TYPE200.getCode());
		
		LinkedList<BatchOrderList> batchOrderList = new LinkedList<>();
		batchOrderList.add(orderList);
		
		int orderResult = batchOrderService.addBatchOrderAndOrderList(req, batchOrderList, TransCode.MB80.getCode(), UserType.TYPE200.getCode(), companyInf.getIsPlatform());
		if (orderResult < 0) {
			logger.error("## 新增企业开户订单信息失败");
			return 0;
		}
		
		String orderId = jedisClusterUtils.get(OrderConstants.companyOrderIdSession);
		try {
			int i = batchOrderService.batchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
			if (i < 1) {
				logger.error("## 调用开户接口失败");
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 调用企业开户接口失败");
			return 0;
		}
				
		jedisClusterUtils.del(OrderConstants.companyOrderIdSession);
		
		try {
			companyInf.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
			if (!companyInfFacade.updateCompanyInf(companyInf)) {
				logger.error("## 更新企业{}开户成功状态失败", companyId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 更新企业{}开户状态失败", companyId);
		}
		return 1;
	}

	@Override
	public Map<String, Object> addCompanyTransferCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		try {
			CompanyInf company = companyInfFacade.getCompanyInfById(companyId);
			if (company == null || company.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "平台打款失败，该平台不存在或未开户");
				return resultMap;
			}

			InaccountOrder order = inaccountOrderService.getById(orderId);
			List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);
			if (order == null || orderDetailList == null || orderDetailList.size() < 1) {
				logger.error("## 查询平台{}打款订单{}信息为空", companyId, orderId);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("status", "暂无可打款订单，请重新查看订单信息");
				return resultMap;
			}

			AccountTransferReqVo reqVo = new AccountTransferReqVo();
			reqVo.setTransAmt(order.getCompanyInSumAmt());
			reqVo.setUploadAmt(order.getCompanyInSumAmt());
			reqVo.setTfrInUserId(order.getCompanyId());
			reqVo.setTfrOutUserId(companyId);

			List<AccountTxnVo> transList = new ArrayList<>();
			Set<String> bIds = new TreeSet<>();
			for (InaccountOrderDetail orderDetail :orderDetailList ) {
				AccountTxnVo txnVo = new AccountTxnVo();
				txnVo.setTxnBId(orderDetail.getBId());
				txnVo.setTxnAmt(orderDetail.getCompanyInAmt());
				txnVo.setUpLoadAmt(orderDetail.getCompanyInAmt());
				transList.add(txnVo);
				bIds.add(orderDetail.getBId());
			}

			reqVo.setTransList(transList);
			reqVo.setTransId(TransCode.MB40.getCode());
			reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
			reqVo.setUserId(companyId);
			reqVo.setbIds(bIds);
			reqVo.setUserType(UserType.TYPE200.getCode());
			reqVo.setDmsRelatedKey(order.getTfrCompanyOrderId());
			reqVo.setUserChnlId(companyId);
			reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
			reqVo.setTransDesc(order.getRemarks());
			reqVo.setTransNumber(1);
			logger.info("平台转账，远程调用转账接口请求参数--->{}", JSONArray.toJSONString(reqVo));
			BaseResult result = new BaseResult();
			try {
				result = accountTransactionFacade.executeTransfer(reqVo);
			} catch (Exception e) {
				logger.error("## 远程调用转账接口异常", e);
			}
			logger.info("平台转账，远程调用转账接口返回参数--->{}", JSONArray.toJSONString(result));
			try {
				if (StringUtil.isNullOrEmpty(result.getCode())) {
					logger.info("平台转账，远程调用查询接口请求参数--->dmsRelatedKe{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
					result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
					logger.info("平台转账，远程调用查询接口返回参数--->{}", JSONArray.toJSONString(result));
				}
			} catch (Exception e) {
				logger.error("## 平台转账，远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "网络异常，请稍后再试");
			}

			if (result != null && Constants.SUCCESS_CODE.toString().equals(result.getCode())) {
				order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
			}

			if (!inaccountOrderService.saveOrUpdate(order)) {
				logger.error("## 更新企业{}收款状态{}失败", order.getCompanyId(), order.getPlatformReceiverCheck());
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "系统异常，请联系管理员");
				return resultMap;
			}

		} catch (Exception e) {
			logger.error(" ## 平台{}打款异常", companyId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "平台打款失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> updateCompanyTransferStat(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));

		InaccountOrder orderInf = new InaccountOrder();
		orderInf.setOrderId(orderId);
		orderInf.setOrderType(UserType.TYPE200.getCode());
		try {
			InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderInf);
			order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
			if (!inaccountOrderService.saveOrUpdate(order)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("status", "网络异常，请稍后再试");
			}
		} catch (Exception e) {
			logger.error("## 企业{}收款异常", companyId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("status", "系统异常，请联系管理员");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> editCompanyInAmtCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String orderListId = StringUtil.nullToString(req.getParameter("orderListId"));
		String companyInAmt = StringUtil.nullToString(req.getParameter("companyInAmt"));
		if (StringUtil.isNullOrEmpty(companyInAmt)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "请输入企业收款金额");
			return resultMap;
		}

		//查询当前编辑收款订单明细信息
		InaccountOrderDetail orderDetail = inaccountOrderDetailService.getById(orderListId);
		if (orderDetail == null) {
			logger.error("## 查询企业收款订单明细{}为空", orderListId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询订单明细信息异常");
			return resultMap;
		}

		//查询收款订单信息
		InaccountOrder order = inaccountOrderService.getById(orderDetail.getOrderId());
		if (orderDetail == null) {
			logger.error("## 查询企业收款订单{}为空", orderDetail.getOrderId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询订单信息异常");
			return resultMap;
		}

		//查询订单所有明细信息
		List<InaccountOrderDetail> orderDetail1List = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderDetail.getOrderId());
		if (orderDetail1List == null && orderDetail1List.size() < 1) {
			logger.error("## 查询企业收款所有订单{}明细信息为空", orderDetail.getOrderId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询订单明细信息异常");
			return resultMap;
		}

		//过滤掉当前修改的企业收款金额记录
		orderDetail1List = 	orderDetail1List.stream().filter(t -> !orderListId.equals(t.getOrderListId())).collect(Collectors.toList());
		//计算出企业所有收款金额总和
		BigDecimal companyInAmtSum = orderDetail1List.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.valueOf(BigDecimal.ROUND_UP, 0), BigDecimal::add);
		companyInAmtSum = companyInAmtSum.add(new BigDecimal(NumberUtils.RMBYuanToCent(companyInAmt))).setScale(0, BigDecimal.ROUND_UP);
		if (companyInAmtSum.compareTo(order.getPlatformInSumAmt()) == 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "企业收款金额不合法，请重新输入");
			return resultMap;
		}

		orderDetail.setCompanyInAmt(new BigDecimal(NumberUtils.RMBYuanToCent(companyInAmt)));
		orderDetail.setUpdateTime(System.currentTimeMillis());
		orderDetail.setUpdateUser(user.getId());
		orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);

		order.setCompanyInSumAmt(companyInAmtSum);
		order.setUpdateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId());
		order.setLockVersion(order.getLockVersion() + 1);

		if (!inaccountOrderDetailService.updateById(orderDetail)) {
			logger.error("## 编辑企业收款金额{}订单明细{}失败", companyInAmt, orderDetail.getOrderListId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业收款金额失败，请稍后再试");
			return resultMap;
		}

		if (!inaccountOrderService.updateById(order)) {
			logger.error("## 编辑企业收款金额{}订单{}失败", new BigDecimal(NumberUtils.RMBCentToYuan(companyInAmtSum.toString())).toString(), order.getOrderId());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业收款金额失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> addCompanyInf(CompanyInf companyInf) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			CompanyInf companyInfLawCode = companyInfFacade.getCompanyInfByLawCode(companyInf.getLawCode());
			if (companyInfLawCode != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "社会信用代码已存在，请重新输入");
				return resultMap;
			}
			CompanyInf companyInfName = companyInfFacade.getCompanyInfByName(companyInf.getName());
			if (companyInfName != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "企业名称已存在，请重新输入");
				return resultMap;
			}
			if (IsPlatformEnum.IsPlatformEnum_1.getCode().equals(companyInf.getIsPlatform())) {
				CompanyInf c = companyInfFacade.getCompanyInfByIsPlatform(companyInf.getIsPlatform());
				if (c != null && IsOpenAccountEnum.ISOPEN_TRUE.getCode().equals(c.getIsOpen())) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "平台标识已有开户企业，请重新选择");
					return resultMap;
				}
			}
			if (!companyInfFacade.insertCompanyInf(companyInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增企业信息失败");
			}
		} catch (Exception e) {
			logger.error("## 新增企业信息出错", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增企业信息失败");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> editCompanyInf(CompanyInf companyInf) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			CompanyInf companyLawCode = companyInfFacade.getCompanyInfByLawCode(companyInf.getLawCode());
			if (companyLawCode != null && !companyLawCode.getCompanyId().equals(companyInf.getCompanyId())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "社会信用代码已存在，请重新输入");
				return resultMap;
			}
			CompanyInf companyInfName = companyInfFacade.getCompanyInfByName(companyInf.getName());
			if (companyInfName != null && !companyInfName.getCompanyId().equals(companyInf.getCompanyId())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "企业名称已存在，请重新输入");
				return resultMap;
			}
			CompanyInf companyInfCode = companyInfFacade.getCompanyInfById(companyInf.getCompanyId());
			if (!companyInfCode.getIsPlatform().equals(companyInf.getIsPlatform()) && !companyInf.getIsPlatform().equals(IsPlatformEnum.IsPlatformEnum_0.getCode())) {
				CompanyInf c = companyInfFacade.getCompanyInfByIsPlatform(companyInf.getIsPlatform());
				if (c != null && IsOpenAccountEnum.ISOPEN_TRUE.getCode().equals(c.getIsOpen())) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "平台标识已有开户企业，请重新选择");
					return resultMap;
				}
			}
			if (!companyInfFacade.updateCompanyInf(companyInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "修改企业信息失败");
			}
		} catch (Exception e) {
			logger.error("## 编辑企业信息出错", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业信息失败");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> addPlatformTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));
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
			logger.error("## 平台{}上账金额不正确，应上账总额{}", companyId, inaccountAmtSum);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加上账信息失败，平台上账金额不正确");
			return resultMap;
		}

		try {
			RetailChnlInf retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(channelId);
			if (retailChnlInf == null || retailChnlInf.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，该平台不存在或未开户");
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("## 添加平台上账异常，查询分销商{}信息异常", channelId, e);
		}

		InaccountOrder order = new InaccountOrder();
		order.setOrderId(IdUtil.getNextId());
		//order.setTfrPlatformOrderId(IdUtil.getNextId());
		order.setTfrCompanyOrderId(IdUtil.getNextId());
		order.setOrderType(UserType.TYPE200.getCode());
		order.setCheckStat(CheckStatEnum.CHECK_FALSE.getCode());
		order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setPlatformInSumAmt(order.getInaccountSumAmt());
		order.setCompanyInSumAmt(order.getInaccountSumAmt());
		order.setProviderId(channelId);
		order.setCompanyId(companyId);
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
		resultMap = commonService.uploadImage(evidenceUrlFile, req, ImageTypeEnum.ImageTypeEnum_10.getValue(), order.getOrderId());
		if (!String.valueOf(resultMap.get("status").toString()).equals("true")) {
			logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
			order.setEvidenceUrl("");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "凭证图片上传失败");
			return resultMap;
		}
		order.setEvidenceUrl(resultMap.get("msg").toString());

		List<InaccountOrderDetail> orderDetailList = new ArrayList<InaccountOrderDetail>();
		for(Map.Entry<String, String> entry : bMap.entrySet()) {
			if (!StringUtil.isNullOrEmpty(entry.getValue())) {
				InaccountOrderDetail orderDetail = new InaccountOrderDetail();
				orderDetail.setOrderListId(IdUtil.getNextId());
				orderDetail.setOrderId(order.getOrderId());
				orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
				orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
				orderDetail.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
				orderDetail.setBId(entry.getKey());
				orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
				orderDetail.setCompanyInAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
				orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				orderDetail.setCreateUser(user.getId());
				orderDetail.setUpdateUser(user.getId());
				orderDetail.setCreateTime(System.currentTimeMillis());
				orderDetail.setUpdateTime(System.currentTimeMillis());
				orderDetail.setLockVersion(0);
				orderDetailList.add(orderDetail);
			}
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
	public Map<String, Object> editPlatformTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));
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
			logger.error("## 平台{}上账金额不正确，应上账总额{}", companyId, inaccountAmtSum);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，平台上账金额不正确");
			return resultMap;
		}

		try {
			RetailChnlInf retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(channelId);
			if (retailChnlInf == null || retailChnlInf.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，该平台不存在或未开户");
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("## 编辑平台上账异常，查询分销商{}信息异常", channelId, e);
		}

		InaccountOrder orderInf = new InaccountOrder();
		orderInf.setOrderId(orderId);
		orderInf.setOrderType(UserType.TYPE200.getCode());
		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderInf);
		if (order == null) {
			logger.error("## 根据上账订单号{}查询订单信息为空", orderId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑上账信息失败，查询平台上账订单信息异常");
			return resultMap;
		}

		order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setPlatformInSumAmt(order.getInaccountSumAmt());
		order.setCompanyInSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setProviderId(channelId);
		order.setCompanyId(companyId);
		order.setRemarks(remarks);
		order.setUpdateUser(user.getId());
		order.setUpdateTime(System.currentTimeMillis());
		order.setLockVersion(order.getLockVersion() + 1);

		if (evidenceUrlFile != null && !evidenceUrlFile.isEmpty()) {
			resultMap = commonService.uploadImage(evidenceUrlFile, req, ImageTypeEnum.ImageTypeEnum_10.getValue(), order.getOrderId());
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

		List<InaccountOrderDetail> editOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> addOrderDetailList = new ArrayList<>();
		List<InaccountOrderDetail> delOrderDetailList = new ArrayList<InaccountOrderDetail>();

		for(Map.Entry<String, String> entry : bMap.entrySet()) {
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
					orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
					orderDetail.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
					orderDetail.setBId(entry.getKey());
					orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
					orderDetail.setCompanyInAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
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
					orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
					orderDetail.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
					orderDetail.setPlatformInAmt(orderDetail.getInaccountAmt());
					orderDetail.setCompanyInAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
					orderDetail.setUpdateUser(user.getId());
					orderDetail.setUpdateTime(System.currentTimeMillis());
					orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);
					editOrderDetailList.add(orderDetail);
				} else {
					delOrderDetailList.add(orderDetail);
				}
			}
		}

		if (addOrderDetailList != null && addOrderDetailList.size() >= 1) {
			if (!inaccountOrderDetailService.saveBatch(addOrderDetailList)) {
				logger.error("## 编辑上账订单明细失败");
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账订单明细信息失败");
				return resultMap;
			}
		}

		if (editOrderDetailList != null && editOrderDetailList.size() >= 1) {
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
	public Map<String, Object> addPlatformTransferCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));

		InaccountOrder order = inaccountOrderService.getById(orderId);
		List<InaccountOrderDetail> orderDetail = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);

		if (order == null || orderDetail == null || orderDetail.size() < 1) {
			logger.error("## 查询平台{}上账订单{}信息为空", companyId, orderId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "上帐信息不存在");
			return resultMap;
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
		reqVo.setFromCompanyId(companyId);
		reqVo.setTransAmt(order.getInaccountSumAmt());
		reqVo.setUploadAmt(order.getInaccountSumAmt());
		reqVo.setTransList(transList);
		reqVo.setTransId(TransCode.MB20.getCode());
		reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
		reqVo.setUserId(companyId);
		reqVo.setbIds(bIds);
		reqVo.setUserType(UserType.TYPE200.getCode());
		reqVo.setDmsRelatedKey(orderId);
		reqVo.setUserChnlId(companyId);
		reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		reqVo.setTransDesc(order.getRemarks());
		reqVo.setTransNumber(1);

		BaseResult result = new BaseResult();
		try {
			logger.info("平台充值，远程调用充值接口请求参数--->{}", JSONArray.toJSONString(reqVo));
			result = accountTransactionFacade.executeRecharge(reqVo);
			logger.info("平台充值，远程调用充值接口返回参数--->{}", JSONArray.toJSONString(result));
		} catch (Exception e) {
			logger.error("## 平台充值，远程调用充值接口异常", e);
		}
		try {
			if (StringUtil.isNullOrEmpty(result.getCode())) {
				logger.info("平台充值，远程调用查询接口请求参数--->dmsRelatedKey{},--->transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				logger.info("平台充值，远程调用查询接口返回参数--->{}", JSONArray.toJSONString(result));
			}
		} catch (Exception e) {
			logger.error("## 平台充值，远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
		}
		if (result == null) {
			logger.error("## 平台充值，远程调用充值接口失败，返回参数为空，orderId--->{},companyId--->{}", orderId, companyId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "上账失败");
			return resultMap;
		}

		if (result.getCode().equals(Constants.SUCCESS_CODE)) {
			order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_TRUE.getCode());
			order.setPlatformReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
		} else {
			order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
			order.setRemarks(result.getMsg());
		}
		if (!inaccountOrderService.updateById(order)) {
			logger.error("## 远程调用充值接口，更新订单失败orderId--->{},companyId--->{}", orderId, companyId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "上帐信息不存在");
			return resultMap;
		}
		resultMap.put("status", Boolean.TRUE);
		return resultMap;
	}

	@Override
	public Map<String, Object> updatePlatformRemitStatCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));

		InaccountOrder order = inaccountOrderService.getById(orderId);
		List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);

		if (order == null || orderDetailList == null || orderDetailList.size() < 1) {
			logger.error("## 查询平台{}订单{}信息为空", companyId, orderId);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("status", "暂无可打款订单，请重新查看订单信息");
			return resultMap;
		}

		AccountTransferReqVo reqVo = new AccountTransferReqVo();
		reqVo.setTransAmt(order.getInaccountSumAmt());
		reqVo.setUploadAmt(order.getInaccountSumAmt());
		reqVo.setTfrInUserId(order.getProviderId());
		reqVo.setTfrOutUserId(companyId);

		List<AccountTxnVo> transList = new ArrayList<>();
		Set<String> bIds = new TreeSet<>();
		for (InaccountOrderDetail orderDetail :orderDetailList ) {
			AccountTxnVo txnVo = new AccountTxnVo();
			txnVo.setTxnBId(orderDetail.getBId());
			txnVo.setTxnAmt(orderDetail.getCompanyInAmt());
			txnVo.setUpLoadAmt(orderDetail.getCompanyInAmt());
			transList.add(txnVo);
			bIds.add(orderDetail.getBId());
		}

		reqVo.setTransList(transList);
		reqVo.setTransId(TransCode.MB40.getCode());
		reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
		reqVo.setUserId(companyId);
		reqVo.setbIds(bIds);
		reqVo.setUserType(UserType.TYPE200.getCode());
		reqVo.setDmsRelatedKey(order.getTfrCompanyOrderId());
		reqVo.setUserChnlId(companyId);
		reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		reqVo.setTransDesc(order.getRemarks());
		reqVo.setTransNumber(1);
		logger.info("平台转账，远程调用转账接口请求参数--->{}", JSONArray.toJSONString(reqVo));
		BaseResult result = new BaseResult();
		try {
			result = accountTransactionFacade.executeTransfer(reqVo);
		} catch (Exception e) {
			logger.error("## 平台转账，远程调用转账接口异常", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
		}
		logger.info("平台转账，远程调用转账接口返回参数--->{}", JSONArray.toJSONString(result));
		try {
			if (StringUtil.isNullOrEmpty(result.getCode())) {
				logger.info("平台转账，远程调用查询接口请求参数--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
				logger.info("平台转账，远程调用查询接口请求参数--->{}", JSONArray.toJSONString(result));
			}
		} catch (Exception e) {
			logger.error("## 平台转账，远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
		}
		if (result != null && Constants.SUCCESS_CODE.toString().equals(result.getCode())) {
			order.setTransferCheck(TransferCheckEnum.INACCOUNT_TRUE.getCode());
			order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
		}

		if (!inaccountOrderService.saveOrUpdate(order)) {
			logger.error("## 更新平台{}转账至分销商订单{}状态{}失败", companyId, orderId, order.getTransferCheck());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统异常，请联系管理员");
			return resultMap;
		}
		return resultMap;
	}

}
