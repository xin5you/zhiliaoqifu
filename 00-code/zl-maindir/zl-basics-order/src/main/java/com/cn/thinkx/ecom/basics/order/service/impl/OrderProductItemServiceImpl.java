package com.cn.thinkx.ecom.basics.order.service.impl;

import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.mapper.OrderProductItemMapper;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
