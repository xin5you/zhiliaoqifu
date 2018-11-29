package com.cn.thinkx.ecom.eshoproutes.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.eshoproutes.domain.EshopRoutes;

public interface EshopRoutesService extends BaseService<EshopRoutes> {

	/**
	 * 根据条件查询商城电商入口表信息
	 * 
	 * @param es
	 * @return
	 */
	List<EshopRoutes> getList(EshopRoutes es);

	/**
	 * 根据条件查询商城电商入口表信息(对象)
	 * 
	 * @param es
	 * @return
	 */
	EshopRoutes getEshopRoutes(EshopRoutes es);

}
