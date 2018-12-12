package com.ebeijia.zl.api.bm001.api.service;


import com.ebeijia.zl.common.utils.domain.BaseResult;

public interface BMOrderService {

	/**
	 * 话费充值
	 * @param request
	 * @return
	 */
	BaseResult handleHbkToBMPayBill(String mobileNo, String rechargeAmount, String orderId, String callBack, String accessToken);
	
}
