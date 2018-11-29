package com.cn.thinkx.ecom.order.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.order.domain.OrderInf;

@Mapper
public interface OrderInfMapper extends BaseDao<OrderInf> {
	
	/**
	 * 电商交易流水明细
	 * 
	 * @return
	 */
	List<OrderInf> listOrderDetail(OrderInf orderInf); 
	
	/**
	 * 电商交易流水汇总
	 * 
	 * @param orderInf
	 * @return
	 */
	List<OrderInf> listOrderSummarizing(OrderInf orderInf); 
	
	/**
	 * 根据userID或personalName查询tb_person_inf信息
	 * 
	 * @param orderInf
	 * @return
	 */
	OrderInf getPersonInf(OrderInf orderInf);
	
}
