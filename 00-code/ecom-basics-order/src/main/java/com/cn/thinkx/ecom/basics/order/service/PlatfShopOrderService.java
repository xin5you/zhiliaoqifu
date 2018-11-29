package com.cn.thinkx.ecom.basics.order.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface PlatfShopOrderService extends BaseService<PlatfShopOrder> {

	List<PlatfShopOrder> getPlatfShopOrderList(PlatfShopOrder pso);

	/**
	 * 修改二级订单状态
	 * 
	 * @param pso
	 */
	void updatePlatfShopOrderStatus(PlatfShopOrder pso);

	/**
	 * 查找二级订单 by 一级订单id and 二级订单状态
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 */
	List<PlatfShopOrder> getPlatfShopOrderByOIdAndStatus(String orderId, String status);

	/**
	 * 查询所有的二级订单信息
	 * 
	 * @param pso
	 * @return
	 */
	List<PlatfShopOrder> getPlatfShopOrderLists(PlatfShopOrder pso);

	/**
	 * 查询一级订单下的所有二级订单不等于已完成的数据
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 */
	List<PlatfShopOrder> getPlatfShopOrderByOIdAndNotStatus(String orderId, String status);

	/**
	 * 将半个小时以前得门店订单修改为已取消状态
	 * 
	 * @return
	 */
	int updatePlatfShopOrder();

	/**
	 * 自建电商退款
	 * 
	 * @param platfShopOrder
	 * @param returnsId
	 */
	boolean doHkbStoreRefund(PlatfShopOrder platfShopOrder);

}
