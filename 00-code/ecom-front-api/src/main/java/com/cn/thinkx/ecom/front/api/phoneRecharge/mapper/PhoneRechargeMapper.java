package com.cn.thinkx.ecom.front.api.phoneRecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;

@Mapper
public interface PhoneRechargeMapper extends BaseDao<PhoneRechargeOrder> {

	int innsertPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	int updatePhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	List<PhoneRechargeOrder> getPhoneRechargeOrderNotSuccess();
	
	PhoneRechargeOrder getPhoneRechargeOrderByChannelOrderNo(PhoneRechargeOrder phoneRechargeOrder);
}
