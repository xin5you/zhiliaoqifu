package com.ebeijia.zl.web.oms.phoneRecharge.service.impl;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.phoneRecharge.service.ProviderInfService;
import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrder;
import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.ebeijia.zl.web.oms.specialAccount.service.SpeAccountBatchOrderListService;
import com.ebeijia.zl.web.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.ebeijia.zl.web.oms.sys.model.User;

@Service("providerInfService")
public class ProviderInfServiceImpl implements ProviderInfService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Reference(check = false, version = "1.0.0")
	private ProviderInfFacade telProviderInfFacade;
	
	@Autowired
	private SpeAccountBatchOrderService speAccountBatchOrderService;
	
	@Autowired
	private SpeAccountBatchOrderListService speAccountBatchOrderListService;
	
	@Override
	public int telProviderOpenAccount(HttpServletRequest req) {
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		ProviderInf providerInf = null;
		try {
			providerInf = telProviderInfFacade.getProviderInfById(providerId);
			if (StringUtil.isNullOrEmpty(providerInf)) {
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 查询供应商{}信息失败", providerId);
			return 0;
		}
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderType(TransCode.MB80.getCode());
		order.setOrderDate(System.currentTimeMillis());
		order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
		order.setCompanyId(providerId);
		order.setCreateUser(user.getId());
		order.setUpdateUser(user.getId());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateTime(System.currentTimeMillis());
		int orderResult = speAccountBatchOrderService.addSpeAccountBatchOrder(order);
		SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
		orderList.setOrderId(order.getOrderId());
		orderList.setUserName(providerInf.getProviderName());
		orderList.setAccountType(UserType.TYPE300.getCode());
		String[] bizType = {providerInf.getBId()};
		if (orderResult < 0) {
			return 0;
		}
		int orderListResult = speAccountBatchOrderListService.addOrderList(orderList, user, bizType);
		if (orderListResult < 0) {
			return 0;
		}
		int i = speAccountBatchOrderService.batchSpeAccountBatchOpenAccountITF(order.getOrderId(), user, BatchOrderStat.BatchOrderStat_00.getCode());
		if (i < 1) {
			return 0;
		}
		providerInf.setIsOpen("1");
		try {
			telProviderInfFacade.updateProviderInf(providerInf);
		} catch (Exception e) {
			logger.error("## 更新供应商{}开户状态失败", providerId);
		}
		return 1;
	}

}
