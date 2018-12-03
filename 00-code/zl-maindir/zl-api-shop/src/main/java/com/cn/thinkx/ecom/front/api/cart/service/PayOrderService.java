package com.cn.thinkx.ecom.front.api.cart.service;


import com.ebeijia.zl.common.utils.domain.BaseResult;

import javax.servlet.http.HttpServletRequest;

public interface PayOrderService {

	/**
	 * 下订单
	 * @param req
	 * @return
	 */
	BaseResult payOrder(HttpServletRequest req);
}
