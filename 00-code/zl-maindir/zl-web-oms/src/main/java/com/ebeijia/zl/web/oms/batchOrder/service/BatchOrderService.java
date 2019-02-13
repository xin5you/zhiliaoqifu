package com.ebeijia.zl.web.oms.batchOrder.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.github.pagehelper.PageInfo;

public interface BatchOrderService extends IService<BatchOrder> {
	
	List<BatchOrder> getBatchOrderList(BatchOrder order);
	
	PageInfo<BatchOrder> getBatchOrderPage(int startNum, int pageSize, BatchOrder order, HttpServletRequest req);
	
	int addBatchOrderAndOrderList(HttpServletRequest req, LinkedList<BatchOrderList> personInfList, String orderType, String accountType, String isPlatform);
	
	BatchOrder getBatchOrderById(String orderId);
	
	int deleteOpenAccountOrRechargeCommit(String orderId, User user);
	
	int updateBatchOrderAndOrderListByOrderStat(String orderId, String orderStat, User user);

	Map<String, Object> batchOpenAccountITF(String orderId, User user, String orderStat);
	
	int batchRechargeITF(String orderId, User user, String orderStat);
	
	int batchTransferAccountITF(String orderId, User user, String orderStat);
	
	int addBatchOrder(BatchOrder order);
	
	BatchOrder getBatchOrderByOrderId(String orderId);
}
