package com.cn.thinkx.ecom.platforder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.basics.order.mapper.OrderShipMapper;
import com.cn.thinkx.ecom.platforder.service.OrderShipService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("orderShipInfService")
public class OrderShipServiceImpl implements OrderShipService{

	@Autowired
	private OrderShipMapper orderShipMapper;
	
	@Override
	public PageInfo<OrderShip> getOrderShipListPage(int startNum, int pageSize, OrderShip entity) {
		PageHelper.startPage(startNum, pageSize);
		List<OrderShip> orderShipList = orderShipMapper.getOrderShipList(entity);
		
		PageInfo<OrderShip> page = new PageInfo<OrderShip>(orderShipList);
		return page;
	}

}
