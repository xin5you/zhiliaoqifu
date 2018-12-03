package com.cn.thinkx.ecom.basics.order.service;

import com.cn.thinkx.ecom.basics.order.domain.OrderExpressPlatf;
import com.ebeijia.zl.common.core.service.BaseService;

public interface OrderExpressPlatfService extends BaseService<OrderExpressPlatf> {
	
	/**
	 * 保存订单物流关联表
	 * @param orderExpressPlatf
	 */
	void saveOrderExpressPlatf(OrderExpressPlatf orderExpressPlatf);

}
