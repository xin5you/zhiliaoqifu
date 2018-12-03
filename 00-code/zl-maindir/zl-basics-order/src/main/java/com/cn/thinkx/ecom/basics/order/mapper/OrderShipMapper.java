package com.cn.thinkx.ecom.basics.order.mapper;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderShipMapper extends BaseDao<OrderShip> {

	List<OrderShip> getOrderShipList(OrderShip os);
	
	/**
	 * 获取订单的收货地址
	 * @param orderId
	 * @return
	 */
	OrderShip getOrderShipByOrderId(String orderId);
}
