package com.cn.thinkx.ecom.basics.order.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.OrderRefund;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface OrderRefundService extends BaseService<OrderRefund> {

	List<OrderRefund> getOrderRefundList(OrderRefund orderRefund);
	
	/**
	 * 保存退款信息
	 * 
	 * @param returnsId
	 * @param pso
	 */
	void saveOrderRefundInf(String returnsId,PlatfShopOrder pso);
	
	/**
	 * 通过退款申请id查询退款信息
	 * 
	 * @param returnsId
	 * @return
	 */
	OrderRefund getOrderRefundByReturnsId(String returnsId);
	
}
