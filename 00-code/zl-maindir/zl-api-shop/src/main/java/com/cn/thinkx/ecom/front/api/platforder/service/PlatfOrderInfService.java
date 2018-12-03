package com.cn.thinkx.ecom.front.api.platforder.service;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.ebeijia.zl.common.utils.domain.BaseResult;

import java.util.List;

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
