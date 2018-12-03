package com.cn.thinkx.ecom.basics.order.service;

import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

public interface OrderProductItemService extends BaseService<OrderProductItem> {

	//通过skuCode和门店订单ID获取订单货品明细
	List<OrderProductItem> getOrderProductItemList(OrderProductItem opi);
	
	/**
	 * 查询订单中得商品及购买数量
	 * @param orderId 订单ID
	 * @return
	 */
	List<OrderProductItem> getOrderProductItemByOrderId(String orderId);
	
	OrderProductItem getOrderProductItemByItemId(String itemId);
}
