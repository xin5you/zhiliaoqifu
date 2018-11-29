package com.cn.thinkx.ecom.front.api.order.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.front.api.order.domain.OrderInf;

public interface OrderInfService extends BaseService<OrderInf> {

	/**
	 * 订单列表查询
	 * 
	 * @return
	 */
	List<OrderInf> getList();
	
	/**
	 * 根据第三方订单号查询订单信息
	 * 
	 * @param routerOrderNo
	 * @return
	 */
	OrderInf selectByRouterOrderNo(String routerOrderNo);
	
	/**
	 * 根据userID查询订单列表信息
	 * 
	 * @param userId
	 * @return
	 */
	List<OrderInf> selectByOrderUserId(String userId);
	
}
