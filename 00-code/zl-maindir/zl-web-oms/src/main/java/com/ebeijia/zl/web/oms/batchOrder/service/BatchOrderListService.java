package com.ebeijia.zl.web.oms.batchOrder.service;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

public interface BatchOrderListService {

	LinkedList<BatchOrderList> getRedisBatchOrderList(String bathOpen);
	
	PageInfo<BatchOrderList> getBatchOrderListPage(int startNum, int pageSize, String orderId);
	
	List<BatchOrderList> getBatchOrderListByOrderId(String orderId);
	
	int addOrderListByUpdateOpenAccount(BatchOrderList orderList, User user, String[] bizType);
	
	List<BatchOrderList> getBatchOrderListByOrder(BatchOrderList orderList);
	
	int deleteBatchOrderList(HttpServletRequest req);
}