package com.cn.thinkx.ecom.front.api.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.OrderType;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.front.api.order.domain.OrderInf;
import com.cn.thinkx.ecom.front.api.order.mapper.OrderInfMapper;
import com.cn.thinkx.ecom.front.api.order.service.OrderInfService;

@Service("orderInfService")
public class OrderInfServiceImpl extends BaseServiceImpl<OrderInf> implements OrderInfService {

	@Autowired
	private OrderInfMapper orderInfMapper;

	@Override
	public List<OrderInf> getList() {
		return this.orderInfMapper.getList();
	}

	@Override
	public OrderInf selectByRouterOrderNo(String routerOrderNo) {
		return this.orderInfMapper.selectByRouterOrderNo(routerOrderNo);
	}

	@Override
	public List<OrderInf> selectByOrderUserId(String userId) {
		List<OrderInf> list = orderInfMapper.selectByOrderUserId(userId);
		if (list != null && list.size() > 0) {
			for (OrderInf o : list) {
				if (OrderType.OT00.getCode().equals(o.getOrderType()))
					o.setOrderType(Constants.OrderType.OT00.getValue());
				else if (OrderType.OT01.getCode().equals(o.getOrderType()))
					o.setOrderType(Constants.OrderType.OT01.getValue());
				else if (OrderType.OT02.getCode().equals(o.getOrderType()))
					o.setOrderType(Constants.OrderType.OT02.getValue());
				else if (OrderType.OT03.getCode().equals(o.getOrderType()))
					o.setOrderType(Constants.OrderType.OT03.getValue());
				else
					o.setOrderType(Constants.OrderType.OT04.getValue());
			}
		}
		return list;
	}

}
