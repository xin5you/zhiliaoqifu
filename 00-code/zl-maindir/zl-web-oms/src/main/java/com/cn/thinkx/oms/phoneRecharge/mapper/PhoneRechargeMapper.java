package com.cn.thinkx.oms.phoneRecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.phoneRecharge.model.PhoneRechargeOrder;
import com.cn.thinkx.oms.phoneRecharge.model.PhoneRechargeOrderUpload;

@Mapper
public interface PhoneRechargeMapper {

	/**
	 * 查询手机充值订单信息
	 * 
	 * @return
	 */
	List<PhoneRechargeOrder> getPhoneRechargeList(PhoneRechargeOrder entity);

	List<PhoneRechargeOrderUpload> getPhoneRechargeListUpload(PhoneRechargeOrder entity);

	PhoneRechargeOrder getPhoneRechargeByRid(String rId);

	int updatePhoneRechargeOrder(PhoneRechargeOrder entity);
}
