package com.cn.thinkx.ecom.basics.order.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface OrderShipService extends BaseService<OrderShip> {

	List<OrderShip> getOrderShipList(OrderShip os);
	
	/**
	 * 获取订单的收货地址
	 * @param orderId
	 * @return
	 */
	OrderShip getOrderShipByOrderId(String orderId);
}
