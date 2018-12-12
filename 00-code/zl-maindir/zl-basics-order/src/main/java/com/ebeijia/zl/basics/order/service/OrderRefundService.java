package com.ebeijia.zl.basics.order.service;

import com.ebeijia.zl.basics.order.domain.OrderRefund;
import com.ebeijia.zl.basics.order.domain.PlatfShopOrder;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

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
