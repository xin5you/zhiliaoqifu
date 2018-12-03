package com.cn.thinkx.ecom.basics.order.service;

import com.cn.thinkx.ecom.basics.order.domain.ReturnsInf;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

public interface ReturnsOrderService extends BaseService<ReturnsOrder> {

	List<ReturnsOrder> getReturnsOrderList(ReturnsOrder returnsOrder);
	
	List<ReturnsOrder> getReturnsOrderListByReturnsOrder(ReturnsOrder returnsOrder);
	
	ReturnsOrder  getReturnsOrderByReturnsId(String returnsId);
	
	/**
	 * 根据订单号查询退货申请信息
	 * 
	 * @param sOrderId
	 * @return
	 */
	List<ReturnsOrder> getReturnsOrderBySorderId(String sOrderId);
	
	ReturnsInf getReturnsInfByItemId(String itemId);
	
	/**
	 * 通过申请单id和订单id查询退货申请信息
	 * 
	 * @param returnsOrder
	 * @return
	 */
	ReturnsOrder getReturnsOrderByApplyId(ReturnsOrder returnsOrder);
	
	ReturnsOrder getReturnsOrderByItemId(ReturnsOrder returnsOrder);
	
}
