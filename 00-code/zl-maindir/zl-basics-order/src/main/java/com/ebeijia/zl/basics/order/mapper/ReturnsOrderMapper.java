package com.ebeijia.zl.basics.order.mapper;

import com.ebeijia.zl.basics.order.domain.ReturnsInf;
import com.ebeijia.zl.basics.order.domain.ReturnsOrder;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReturnsOrderMapper extends BaseDao<ReturnsOrder> {

	List<ReturnsOrder> getReturnsOrderList(ReturnsOrder returnsOrder);
	
	List<ReturnsOrder> getReturnsOrderListByReturnsOrder(ReturnsOrder returnsOrder);
	
	ReturnsOrder  getReturnsOrderByReturnsId(String returnsId);
	
	List<ReturnsOrder> getReturnsOrderBySorderId(String sOrderId);
	
	ReturnsInf getReturnsInfByItemId(String itemId);
	
	/**
	 * 通过申请单id和订单id查询退货申请信息
	 * 
	 * @param returnsOrder
	 * @return
	 */
	ReturnsOrder getReturnsOrderByApplyId(ReturnsOrder returnsOrder);
	
	ReturnsOrder getReturnsOrderByItemId(ReturnsOrder returnsOrder);
}
