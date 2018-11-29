package com.cn.thinkx.ecom.platforder.service;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.github.pagehelper.PageInfo;

public interface OrderShipService {

	/**
	 * 查询所有的订单收货地址（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<OrderShip> getOrderShipListPage(int startNum, int pageSize, OrderShip entity);
}
