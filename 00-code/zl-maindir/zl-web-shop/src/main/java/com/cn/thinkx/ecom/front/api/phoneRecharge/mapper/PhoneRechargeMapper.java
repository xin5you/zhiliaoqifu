package com.cn.thinkx.ecom.front.api.phoneRecharge.mapper;

import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhoneRechargeMapper extends BaseDao<PhoneRechargeOrder> {

	int innsertPhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	int updatePhoneRechargeOrder(PhoneRechargeOrder phoneRechargeOrder);
	
	List<PhoneRechargeOrder> getPhoneRechargeOrderNotSuccess();
	
	PhoneRechargeOrder getPhoneRechargeOrderByChannelOrderNo(PhoneRechargeOrder phoneRechargeOrder);
}
