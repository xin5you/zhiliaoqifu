package com.cn.thinkx.ecom.order.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.order.domain.OrderInf;
import com.github.pagehelper.PageInfo;

public interface OrderInfService extends BaseService<OrderInf> {
	
	/**
	 * 电商交易流水明细
	 * 
	 * @return
	 */
	List<OrderInf> listOrderDetail(OrderInf orderInf);
	
	/**
	 * 电商交易流水明细（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<OrderInf> getOrderDetailPage(int startNum, int pageSize, OrderInf entity);

	/**
	 * 电商交易流水汇总
	 * 
	 * @param orderInf
	 * @return
	 */
	List<OrderInf> listOrderSummarizing(OrderInf orderInf); 
	
	/**
	 * 电商交易流水汇总（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<OrderInf> getOrderSummarizingPage(int startNum, int pageSize, OrderInf entity);
	
	/**
	 * 根据userID或personalName查询tb_person_inf信息
	 * 
	 * @param orderInf
	 * @return
	 */
	OrderInf getPersonInf(OrderInf orderInf);
	
}
