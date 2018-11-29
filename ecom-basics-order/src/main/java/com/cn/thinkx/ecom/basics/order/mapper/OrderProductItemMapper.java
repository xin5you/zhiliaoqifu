package com.cn.thinkx.ecom.basics.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface OrderProductItemMapper extends BaseDao<OrderProductItem> {

	List<OrderProductItem> getOrderProductItemList(OrderProductItem opd);
	
	/**
	 * 查询订单中得商品及购买数量
	 * @param orderId 订单ID
	 * @return
	 */
	List<OrderProductItem> getOrderProductItemByOrderId(String orderId);
	
	OrderProductItem getOrderProductItemByItemId(String itemId);
}
