package com.cn.thinkx.ecom.platforder.service;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.github.pagehelper.PageInfo;

public interface OrderProductItemService {

	/**
	 * 查询所有订单的货品明细
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<PlatfOrder> getOrderShipListPage(int startNum, int pageSize, PlatfOrder entity);
}
