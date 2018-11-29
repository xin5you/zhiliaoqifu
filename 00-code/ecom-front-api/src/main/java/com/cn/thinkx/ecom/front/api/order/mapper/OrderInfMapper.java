package com.cn.thinkx.ecom.front.api.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.front.api.order.domain.OrderInf;

@Mapper
public interface OrderInfMapper extends BaseDao<OrderInf> {

	/**
	 * 订单查询列表
	 * 
	 * @return
	 */
	List<OrderInf> getList();

	/**
	 * 根据电商订单号查询信息
	 * 
	 * @param routerOrderNo
	 * @return
	 */
	OrderInf selectByRouterOrderNo(String routerOrderNo);

	/**
	 * 根据用户编号查询订单列表信息
	 * 
	 * @param userId
	 * @return
	 */
	List<OrderInf> selectByOrderUserId(String userId);

}
