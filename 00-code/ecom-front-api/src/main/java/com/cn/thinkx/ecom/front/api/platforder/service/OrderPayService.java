package com.cn.thinkx.ecom.front.api.platforder.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;

public interface OrderPayService {

	PlatfOrder orderPay(HttpServletRequest req, HttpServletResponse resp);
	
	PlatfOrder getPlatfOrderByPrimaryKey(String primaryKey);
	
}
