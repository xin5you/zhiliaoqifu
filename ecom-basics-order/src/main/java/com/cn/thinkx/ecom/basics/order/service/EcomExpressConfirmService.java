package com.cn.thinkx.ecom.basics.order.service;

import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface EcomExpressConfirmService extends BaseService<EcomExpressConfirm> {

	/**
	 * 通过退款申请id查询退货包裹确认信息
	 * 
	 * @param returnsId
	 * @return
	 */
	EcomExpressConfirm selectByReturnsId(String returnsId);
}
