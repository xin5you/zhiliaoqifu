package com.cn.thinkx.ecom.basics.order.mapper;

import com.cn.thinkx.ecom.basics.order.domain.OrderRefund;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
