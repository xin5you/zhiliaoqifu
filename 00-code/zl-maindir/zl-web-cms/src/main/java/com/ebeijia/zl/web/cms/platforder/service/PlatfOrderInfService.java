package com.ebeijia.zl.web.cms.platforder.service;

import java.util.List;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
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
	PageInfo<TbEcomPlatfOrder> getPlatforderListPage(int startNum, int pageSize, TbEcomPlatfOrder entity);

	/**
	 * 查看电商平台二级订单通过一级订单查询（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<TbEcomPlatfShopOrder> getPlatfShopOrderListPageByPlatOrder(int startNum, int pageSize, TbEcomPlatfShopOrder entity);

	/**
	 * 查看电商平台二级订单（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<TbEcomPlatfShopOrder> getPlatfShopOrderListPage(int startNum, int pageSize, TbEcomPlatfShopOrder entity);

}
