package com.ebeijia.zl.web.oms.company.service.impl;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.IsOpenEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderListService;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.company.service.CompanyService;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.utils.OrderConstants;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService{
	
	Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private CompanyInfFacade companyInfFacade;
	
	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	private BatchOrderListService batchOrderListService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;
	
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
		
		int orderResult = batchOrderService.addBatchOrderAndOrderList(req, batchOrderList, TransCode.MB80.getCode(), UserType.TYPE200.getCode());
		if (orderResult < 0) {
			logger.error("## 新增企业开户订单信息失败");
			return 0;
		}
		
		String orderId = jedisClusterUtils.get(OrderConstants.companyOrderIdSession);
		int i = batchOrderService.batchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
		if (i < 1) {
			logger.error("## 调用开户接口失败");
			return 0;
		}
		
		jedisClusterUtils.del(OrderConstants.companyOrderIdSession);
		
		try {
			companyInf.setIsOpen(IsOpenEnum.ISOPEN_TRUE.getCode());
			if (!companyInfFacade.updateCompanyInf(companyInf)) {
				logger.error("## 更新企业{}开户成功状态失败", companyId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 更新企业{}开户状态失败", companyId);
		}
		return 1;
	}

	
}
