package com.ebeijia.zl.web.oms.providerChnl.service.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.web.oms.sys.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.IsOpenEnum;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderListService;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.providerChnl.service.ProviderInfService;
import com.ebeijia.zl.web.oms.utils.OrderConstants;

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
	public int addProviderTransferCommit(HttpServletRequest req) {
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String amount = StringUtil.nullToString(req.getParameter("amount"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		ProviderInf providerInf = null;
		try {
			providerInf = providerInfFacade.getProviderInfById(providerId);
			if (StringUtil.isNullOrEmpty(providerInf)) {
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 查询供应商{}信息失败", providerId);
			return 0;
		}
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		BatchOrder order = new BatchOrder();
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderType(TransCode.MB40.getCode());
		order.setOrderDate(System.currentTimeMillis());
		order.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
		order.setCompanyId(providerId);
		order.setCreateUser(user.getId());
		order.setUpdateUser(user.getId());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateTime(System.currentTimeMillis());
		int orderResult = batchOrderService.addBatchOrder(order);
		BatchOrderList orderList = new BatchOrderList();
		orderList.setOrderId(order.getOrderId());
		orderList.setUserName(providerInf.getProviderName());
		orderList.setAccountType(UserType.TYPE300.getCode());
		orderList.setAmount(new BigDecimal(NumberUtils.RMBYuanToCent(amount)));
		orderList.setTfrInBid(SpecAccountTypeEnum.A00.getbId());
		orderList.setTfrInId(companyId);
//		orderList.setTfrOutBid(providerInf.getBId());
		orderList.setTfrOutId(providerInf.getProviderId());
//		String[] bizType = {providerInf.getBId()};
		if (orderResult < 0) {
			return 0;
		}
//		int orderListResult = batchOrderListService.addOrderListByUpdateOpenAccount(orderList, user, bizType);
//		if (orderListResult < 0) {
//			return 0;
//		}
		int i = batchOrderService.batchOpenAccountITF(order.getOrderId(), user, BatchOrderStat.BatchOrderStat_00.getCode());
		if (i < 1) {
			return 0;
		}
		return 0;
	}

}
