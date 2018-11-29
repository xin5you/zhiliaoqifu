package com.cn.thinkx.ecom.basics.order.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface PlatfOrderService extends BaseService<PlatfOrder> {

	List<PlatfOrder> getPlatfOrderList(PlatfOrder po);
	
	String getPrimaryKey();
	
	/**
	 * 通过会员id查看订单信息
	 * 
	 * @param po
	 * @return
	 */
	List<PlatfOrder> getPlatfOrderGoodsByMemberId(PlatfOrder po);
	
	/**
	 * 将半个小时以前得订单修改为已取消状态
	 * @return
	 */
	int updateOlatfOrder();
}
