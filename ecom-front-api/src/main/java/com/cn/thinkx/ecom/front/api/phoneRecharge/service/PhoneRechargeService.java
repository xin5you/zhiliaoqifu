package com.cn.thinkx.ecom.front.api.phoneRecharge.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeGetReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeGetResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeResp;

public interface PhoneRechargeService extends BaseService<PhoneRechargeOrder> {
	
	PhoneRechargeOrder innsertPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	int updatePhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	PhoneRechargeOrder getPhoneRechargeOrderByPrimaryKey(String primaryKey);
	
	String handlePhoneRechargeOrder(PhoneRechargeReq req, PhoneRechargeResp resp);
	
	void doPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	String doGetPhoneInfo(String phone);
	
	void doPhoneRechargeRefund(PhoneRechargeOrder phoneRechargeOrder);
	
	List<PhoneRechargeOrder> getPhoneRechargeOrderNotSuccess();
	
	String handleGetPhoneRechargeOrder(PhoneRechargeGetReq prReq, PhoneRechargeGetResp prResp);
	
}
