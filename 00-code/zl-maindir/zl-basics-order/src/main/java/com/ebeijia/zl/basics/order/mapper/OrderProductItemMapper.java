package com.ebeijia.zl.basics.order.mapper;

import com.ebeijia.zl.basics.order.domain.OrderProductItem;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
