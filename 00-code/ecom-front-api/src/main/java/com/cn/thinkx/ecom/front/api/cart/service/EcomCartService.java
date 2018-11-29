package com.cn.thinkx.ecom.front.api.cart.service;

import com.cn.thinkx.ecom.basics.order.domain.Cart;
import com.cn.thinkx.ecom.common.domain.BaseResult;

/**
 * 商城购物车
 * @author zhuqiuyou
 *
 */
public interface EcomCartService {

	
	/**
	 * 添加购物车
	 * 
	 * @param membeId 会员ID
	 * @param cart
	 * @return
	 */
	public BaseResult<Object> addCart(String membeId, Cart cart);
}
