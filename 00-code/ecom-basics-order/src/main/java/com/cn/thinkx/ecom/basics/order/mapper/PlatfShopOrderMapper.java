package com.cn.thinkx.ecom.basics.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper	
public interface PlatfShopOrderMapper extends BaseDao<PlatfShopOrder> {

	List<PlatfShopOrder> getPlatfShopOrderList(PlatfShopOrder pso);
	
	/**
	 * 修改订单状态
	 * @param pso
	 */
	void updatePlatfShopOrderStatus(PlatfShopOrder pso);
	
	/**
	 * 查找二级订单 by 一级订单id and 二级订单状态
	 * @param orderId
	 * @param status
	 * @return
	 */
	public List<PlatfShopOrder> getPlatfShopOrderByOIdAndStatus(@Param("orderId")String orderId,@Param("subOrderStatus")String status);
	
	/**
	 * 查询所有的二级订单信息
	 * 
	 * @param pso
	 * @return
	 */
	List<PlatfShopOrder> getPlatfShopOrderLists(PlatfShopOrder pso);
	
	/**
	 * 查询一级订单下的所有二级订单不等于已完成的数据
	 * @param orderId
	 * @param status
	 * @return
	 */
	List<PlatfShopOrder> getPlatfShopOrderByOIdAndNotStatus(@Param("orderId")String orderId,@Param("subOrderStatus")String status);

	/**
	 * 将半个小时以前得门店订单修改为已取消状态
	 * @return
	 */
	int updatePlatfShopOrder();
}
