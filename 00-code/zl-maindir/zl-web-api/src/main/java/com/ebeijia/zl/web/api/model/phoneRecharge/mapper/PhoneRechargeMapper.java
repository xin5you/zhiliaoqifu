package com.ebeijia.zl.web.api.model.phoneRecharge.mapper;

import org.apache.ibatis.annotations.Param;

import com.ebeijia.zl.web.api.model.phoneRecharge.model.PhoneRechargeOrder;

public interface PhoneRechargeMapper {
	
	PhoneRechargeOrder getPhoneRechargeOrderById(@Param("rId")String rId);
	
	int insertPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	int updatePhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);	
	
}
