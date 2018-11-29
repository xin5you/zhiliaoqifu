package com.cn.thinkx.ecom.bm001.api.service;

import com.cn.thinkx.ecom.common.domain.BaseResult;

public interface BMOrderService {

	/**
	 * 话费充值
	 * @param request
	 * @return
	 */
	BaseResult  handleHbkToBMPayBill(String mobileNo, String rechargeAmount, String orderId, String callBack, String accessToken);
	
}
