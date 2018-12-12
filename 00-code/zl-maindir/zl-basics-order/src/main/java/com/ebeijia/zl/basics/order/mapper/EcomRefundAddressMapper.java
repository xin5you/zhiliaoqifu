package com.ebeijia.zl.basics.order.mapper;

import com.ebeijia.zl.basics.order.domain.EcomRefundAddress;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EcomRefundAddressMapper extends BaseDao<EcomRefundAddress> {

	/**
	 * 通过退款申请id查询退货地址信息
	 * 
	 * @param returnsId
	 * @return
	 */
	EcomRefundAddress selectByReturnsId(String returnsId);
}
