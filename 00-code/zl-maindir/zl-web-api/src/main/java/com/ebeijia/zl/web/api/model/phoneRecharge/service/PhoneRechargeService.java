package com.ebeijia.zl.web.api.model.phoneRecharge.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.ebeijia.zl.web.api.model.phoneRecharge.model.PhoneRechargeOrder;

public interface PhoneRechargeService {
	
	PhoneRechargeOrder getPhoneRechargeOrderById(@Param("rId")String rId);
	
	int insertPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	int updatePhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	boolean flowRechargeNotify(HttpServletRequest request);
	
	boolean phoneRechargeNotify(HttpServletRequest req);
	
	/**
	 * 手机充值（立方）
	 * 
	 * @param orderId
	 */
	void phoneRechargeToLiFang(String orderId);
	
	/**
	 * 手机充值（鼎驰）
	 * 
	 * @param orderId
	 */
	void phoneRechargeToDingChi(String orderId);
}
