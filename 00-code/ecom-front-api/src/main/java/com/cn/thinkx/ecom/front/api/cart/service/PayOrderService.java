package com.cn.thinkx.ecom.front.api.cart.service;

import javax.servlet.http.HttpServletRequest;

import com.cn.thinkx.ecom.common.domain.BaseResult;

public interface PayOrderService {

	/**
	 * 下订单
	 * @param req
	 * @return
	 */
	BaseResult payOrder(HttpServletRequest req);
}
