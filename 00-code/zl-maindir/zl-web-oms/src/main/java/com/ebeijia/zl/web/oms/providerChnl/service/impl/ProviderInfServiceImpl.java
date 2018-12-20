package com.ebeijia.zl.web.oms.providerChnl.service.impl;

import com.ebeijia.zl.basics.billingtype.domain.BillingType;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeService;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderListService;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import com.ebeijia.zl.web.oms.providerChnl.service.ProviderInfService;
import com.ebeijia.zl.web.oms.utils.OrderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service("providerInfService")
public class ProviderInfServiceImpl implements ProviderInfService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProviderInfFacade providerInfFacade;
	
	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	private BatchOrderListService batchOrderListService;

	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

	@Autowired
	private CompanyInfFacade companyInfFacade;

	@Autowired
	private BillingTypeService billingTypeService;

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
		
		providerInf.setIsOpen(IsOpenEnum.ISOPEN_TRUE.getCode());
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
	public int addProviderTransfer(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String platformFee = jedisClusterUtils.hget(OmsEnum.TB_BASE_DICT, OmsEnum.PLATFORM_FEE);

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
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));

		CompanyInf company = companyInfFacade.getCompanyInfByLawCode(companyCode);

		InaccountOrder order = new InaccountOrder();
		order.setOrderId(IdUtil.getNextId());
		order.setOrderType(TransCode.MB20.getCode());
		order.setCheckStat(CheckStatEnum.CHECK_FALSE.getCode());
		order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));
		order.setInacccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
		order.setProviderId(providerId);
		order.setCompanyId(company.getCompanyId());
		order.setRemitCheck(RemitCheckEnum.REMIT_TRUE.getCode());
		order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
		order.setPlatformReceiverCheck(ReceiverEnum.RECEIVER_FALSE.getCode());
		order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_FALSE.getCode());
		order.setEvidenceUrl(evidenceUrl);
		order.setRemarks(remarks);
		order.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		order.setCreateUser(user.getId());
		order.setUpdateUser(user.getId());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateTime(System.currentTimeMillis());
		order.setLockVersion(0);

		List<InaccountOrderDetail> orderDetailList = new ArrayList<InaccountOrderDetail>();
		if (!StringUtil.isNullOrEmpty(A00)) {
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderListId(IdUtil.getNextId());
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
			orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
			orderDetail.setBId("A00");
			orderDetail.setPlatformInAmt(orderDetail.getTransAmt());
			orderDetail.setCompanyInAmt(orderDetail.getTransAmt().multiply(new BigDecimal(platformFee)));
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
			orderDetail.setCompanyInAmt(orderDetail.getTransAmt().multiply(new BigDecimal(platformFee)));
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
			orderDetail.setCompanyInAmt(orderDetail.getTransAmt().multiply(new BigDecimal(platformFee)));
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
			orderDetail.setCompanyInAmt(orderDetail.getTransAmt().multiply(new BigDecimal(platformFee)));
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
			orderDetail.setCompanyInAmt(orderDetail.getTransAmt().multiply(new BigDecimal(platformFee)));
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
			orderDetail.setCompanyInAmt(orderDetail.getTransAmt().multiply(new BigDecimal(platformFee)));
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
			orderDetail.setCompanyInAmt(orderDetail.getTransAmt().multiply(new BigDecimal(platformFee)));
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

}
