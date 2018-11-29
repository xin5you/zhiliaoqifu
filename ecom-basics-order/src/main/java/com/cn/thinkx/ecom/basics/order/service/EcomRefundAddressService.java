package com.cn.thinkx.ecom.basics.order.service;

import com.cn.thinkx.ecom.basics.order.domain.EcomRefundAddress;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface EcomRefundAddressService extends BaseService<EcomRefundAddress> {

	/**
	 * 保存退货地址
	 * 
	 * @param entity
	 */
	void saveEcomRefundAddress(EcomRefundAddress entity);
	
	/**
	 * 通过退款申请id查询退货地址信息
	 * 
	 * @param returnsId
	 * @return
	 */
	EcomRefundAddress selectByReturnsId(String returnsId);
}
