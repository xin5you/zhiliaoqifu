package com.cn.thinkx.ecom.front.api.platforder.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.common.domain.BaseResult;

public interface PlatfOrderInfService {

	List<PlatfOrder> getPlatfOrderGoodsByMemberId(PlatfOrder entity);

	/**
	 * 删除订单（一级）
	 * 
	 * @param req
	 * @return
	 */
	BaseResult<Object> deletePlatfOrder(String orderId);
}
