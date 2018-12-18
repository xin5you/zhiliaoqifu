package com.ebeijia.zl.web.oms.batchOrder.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;

@Mapper
public interface BatchOrderListMapper extends BaseMapper<BatchOrderList> {
	
	int addBatchOrderList(BatchOrderList orderList);
	
	int addBatchOrderListByList(List<BatchOrderList> list);
	
	List<BatchOrderList> getBatchOrderListByOrderId(String orderId);
	
	int updateBatchOrderList(BatchOrderList orderList);
	
	int updateBatchOrderListByList(List<BatchOrderList> list);
	
	List<BatchOrderList> getBatchOrderListByOrderStat(BatchOrderList orderList);
	
	List<BatchOrderList> getBatchOrderListByOrder(BatchOrderList orderList);
	
	BatchOrderList getBatchOrderListByOrderListId(String orderListId);
	
	int deleteBatchOrderList(String orderListId);

}
