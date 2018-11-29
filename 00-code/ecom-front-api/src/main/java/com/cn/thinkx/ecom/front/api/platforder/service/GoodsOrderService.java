package com.cn.thinkx.ecom.front.api.platforder.service;

import com.cn.thinkx.ecom.basics.order.domain.OrderDeliveryInfo;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;

/**
 * 电商订单处理service
 * 
 * @author zhuqiuyou
 *
 */
public interface GoodsOrderService {

	/**
	 * 电商系统，外部系统 购物下单操作
	 * 
	 * @param platfOrder
	 *            一级订单平台
	 * @return
	 */
	public void createEcomGoodsOrder(PlatfOrder platfOrder);

	/**
	 * 查询订单的物流轨迹信息
	 * 
	 * @param platfShopOrder
	 * @param packId
	 *            包裹信息ID
	 */
	OrderDeliveryInfo getOrderExpress(PlatfShopOrder platfShopOrder, String packId) throws Exception;

	/**
	 * 用户取消订单
	 * 
	 * @param orderId
	 * @return
	 */
	JsonResult cancelOrder(String sOrderId);

	/**
	 * 用户确认收货,确认收货信息推给严选
	 * 
	 * @param sOrderId
	 * @return
	 */
	JsonResult confirmOrder(String sOrderId);

	/**
	 * 定时刷新确认收货订单（凌晨24点）
	 */
	void doRefreshPlatOrderInf();

}
