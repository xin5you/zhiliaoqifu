package com.cn.thinkx.ecom.front.api.routes.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.front.api.routes.domain.Routes;

public interface RoutesService extends BaseService<Routes> {
	
	/**
	 * 根据电商代码查询电商入口信息
	 * 
	 * @param ecomCode 渠道号
	 * @return
	 */
	List<Routes> selectByEcomCode(String ecomCode);
	
	/**
	 * 商城主页（入口列表）
	 * 
	 * @param id
	 * @return
	 */
	List<Routes> selectByRoutesHomePage(String id);
	
}
