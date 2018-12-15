package com.ebeijia.zl.web.oms.batchOrder.mapper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.github.pagehelper.PageInfo;

@Mapper
public interface BatchOrderMapper {

	List<BatchOrder> getBatchOrderList(BatchOrder order);
	
	PageInfo<BatchOrder> getBatchOrderPage(int startNum, int pageSize, BatchOrder order,HttpServletRequest req);
	
	int addBatchOrder(BatchOrder order);
	
	BatchOrder getBatchOrderById(String orderId);
	
	int updateBatchOrder(BatchOrder order);
	
	BatchOrder getBatchOrderByOrderId(String orderId);
	
	int deleteBatchOrder(String orderId);
	
}
