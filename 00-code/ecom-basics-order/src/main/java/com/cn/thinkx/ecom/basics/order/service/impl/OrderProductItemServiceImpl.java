package com.cn.thinkx.ecom.basics.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.mapper.OrderProductItemMapper;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.NumberUtils;

@Service("orderProductItemService")
public class OrderProductItemServiceImpl extends BaseServiceImpl<OrderProductItem> implements OrderProductItemService {

	@Autowired
	private OrderProductItemMapper orderProductItemMapper;
	
	@Override
	public List<OrderProductItem> getOrderProductItemList(OrderProductItem opi) {
		return orderProductItemMapper.getOrderProductItemList(opi);
	}

	@Override
	public List<OrderProductItem> getOrderProductItemByOrderId(String orderId) {
		return orderProductItemMapper.getOrderProductItemByOrderId(orderId);
	}

	@Override
	public OrderProductItem getOrderProductItemByItemId(String itemId) {
		OrderProductItem item = orderProductItemMapper.getOrderProductItemByItemId(itemId);
		item.setProductPrice(NumberUtils.RMBCentToYuan(item.getProductPrice()));
		return item;
	}


}
