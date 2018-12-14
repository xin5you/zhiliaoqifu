package com.ebeijia.zl.web.oms.batchOrder.service;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

public interface BatchOrderService {
	
	List<BatchOrder> getBatchOrderList(BatchOrder order);
	
	PageInfo<BatchOrder> getBatchOrderPage(int startNum, int pageSize, BatchOrder order, HttpServletRequest req);
	
	int addBatchOrderAndOrderList(HttpServletRequest req ,LinkedList<BatchOrderList> personInfList, String orderType, String accountType);
	
	BatchOrder getBatchOrderById(String orderId);
	
	int deleteOpenAccountOrRechargeCommit(String orderId, User user);
	
	int updateBatchOrderAndOrderListByOrderStat(String orderId, String orderStat, User user);
	
	int batchOpenAccountITF(String orderId, User user,String orderStat);
	
	int batchRechargeITF(String orderId, User user, String orderStat);
	
	int addBatchOrder(BatchOrder order);
}
