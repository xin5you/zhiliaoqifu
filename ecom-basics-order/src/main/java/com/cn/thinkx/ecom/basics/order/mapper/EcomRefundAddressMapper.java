package com.cn.thinkx.ecom.basics.order.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.order.domain.EcomRefundAddress;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface EcomRefundAddressMapper extends BaseDao<EcomRefundAddress>{

	/**
	 * 通过退款申请id查询退货地址信息
	 * 
	 * @param returnsId
	 * @return
	 */
	EcomRefundAddress selectByReturnsId(String returnsId);
}
