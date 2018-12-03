package com.cn.thinkx.ecom.basics.order.service.impl;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.basics.order.mapper.OrderShipMapper;
import com.cn.thinkx.ecom.basics.order.service.OrderShipService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderShipService")
public class OrderShipServiceImpl extends BaseServiceImpl<OrderShip> implements OrderShipService {

	@Autowired
	private OrderShipMapper orderShipMapper;
	
	@Override
	public List<OrderShip> getOrderShipList(OrderShip os) {
		return orderShipMapper.getOrderShipList(os);
	}
	
	/**
	 * 获取订单的收货地址
	 * @param orderId
	 * @return
	 */
	public OrderShip getOrderShipByOrderId(String orderId){
		return orderShipMapper.getOrderShipByOrderId(orderId);
	}


}
