package com.cn.thinkx.ecom.platforder.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.github.pagehelper.PageInfo;

public interface PlatfOrderInfService {

	/**
	 * 查询电商平台一级订单信息（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<PlatfOrder> getPlatforderListPage(int startNum, int pageSize, PlatfOrder entity);

	/**
	 * 查询电商平台一级订单信息
	 * 
	 * @param po
	 * @return
	 */
	List<PlatfOrder> getPlatfOrderList(PlatfOrder po);

	/**
	 * 查看电商平台二级订单通过一级订单查询（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<PlatfShopOrder> getPlatfShopOrderListPage(int startNum, int pageSize, PlatfShopOrder entity);

	/**
	 * 查看电商平台二级订单（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<PlatfShopOrder> getPlatfShopOrderListsPage(int startNum, int pageSize, PlatfShopOrder entity);

	/**
	 * 查询电商平台二级订单信息
	 * 
	 * @param entity
	 * @param temp
	 * @return
	 */
	List<PlatfShopOrder> getPlatfShopOrderLists(PlatfShopOrder entity);

	/**
	 * 重新提交订单
	 * 
	 * @param sOrderId
	 * @return
	 */
	BaseResult<Object> placeOrder(String sOrderId);

	/**
	 * 订单作废
	 * 
	 * @param sOrderId
	 * @return
	 */
	BaseResult<Object> cancellationOrder(String sOrderId);
}
