package com.cn.thinkx.ecom.basics.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface OrderShipMapper extends BaseDao<OrderShip> {

	List<OrderShip> getOrderShipList(OrderShip os);
	
	/**
	 * 获取订单的收货地址
	 * @param orderId
	 * @return
	 */
	OrderShip getOrderShipByOrderId(String orderId);
}
