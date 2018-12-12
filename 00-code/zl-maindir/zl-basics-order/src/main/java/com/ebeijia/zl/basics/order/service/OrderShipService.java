package com.ebeijia.zl.basics.order.service;

import com.ebeijia.zl.basics.order.domain.OrderShip;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

public interface OrderShipService extends BaseService<OrderShip> {

	List<OrderShip> getOrderShipList(OrderShip os);
	
	/**
	 * 获取订单的收货地址
	 * @param orderId
	 * @return
	 */
	OrderShip getOrderShipByOrderId(String orderId);
}
