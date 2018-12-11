package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.common.util.OmsEnum.BatchOrderStat;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.CompanyService;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderListService;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.wecard.facade.telrecharge.domain.CompanyInf;
import com.cn.thinkx.wecard.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.StringUtil;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService{

	@Autowired
	private CompanyInfFacade companyInfFacade;
	
	@Autowired
	private SpeAccountBatchOrderService speAccountBatchOrderService;
	
	@Autowired
	private SpeAccountBatchOrderListService speAccountBatchOrderListService;
	
	@Override
	public int openAccountCompany(HttpServletRequest req) {
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		CompanyInf companyInf = companyInfFacade.getCompanyInfById(companyId);
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderType(TransCode.MB80.getCode());
		order.setOrderDate(System.currentTimeMillis());
		order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
		order.setCompanyId(companyId);
		order.setCreateUser(user.getId());
		order.setUpdateUser(user.getId());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateTime(System.currentTimeMillis());
		int orderResult = speAccountBatchOrderService.addSpeAccountBatchOrder(order);
		SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
//		orderList.setOrderListId(UUID.randomUUID().toString());
		orderList.setOrderId(order.getOrderId());
		orderList.setUserName(companyInf.getName());
		orderList.setPhoneNo(companyInf.getPhoneNo());
		orderList.setAccountType(UserType.TYPE200.getCode());
		/*orderList.setBizType(SpecAccountTypeEnum.A0.getbId());
		orderList.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
		orderList.setCreateUser(user.getId());
		orderList.setUpdateUser(user.getId());
		orderList.setCreateTime(System.currentTimeMillis());
		orderList.setUpdateTime(System.currentTimeMillis());*/
		String[] bizType = {SpecAccountTypeEnum.A0.getbId()};
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
		companyInf.setIsOpen("1");
		if (companyInfFacade.updateCompanyInf(companyInf)) {
			return 1;
		}
		return 0;
	}

	
}
