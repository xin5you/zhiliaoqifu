package com.cn.thinkx.ecom.basics.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.order.domain.OrderRefund;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface OrderRefundMapper extends BaseDao<OrderRefund> {

	List<OrderRefund> getOrderRefundList(OrderRefund orderRefund);
	
	/**
	 * 通过退款申请id查询退款信息
	 * 
	 * @param returnsId
	 * @return
	 */
	OrderRefund getOrderRefundByReturnsId(String returnsId);
}
