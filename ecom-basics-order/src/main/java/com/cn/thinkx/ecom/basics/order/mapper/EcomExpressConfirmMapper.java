package com.cn.thinkx.ecom.basics.order.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface EcomExpressConfirmMapper extends BaseDao<EcomExpressConfirm> {

	/**
	 * 通过退款申请id查询退货包裹确认信息
	 * 
	 * @param returnsId
	 * @return
	 */
	EcomExpressConfirm selectByReturnsId(String returnsId);
}
