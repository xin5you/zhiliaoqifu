package com.cn.thinkx.ecom.front.api.eshop.service;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.front.api.eshop.domain.EshopInf;

public interface EshopInfService extends BaseService<EshopInf> {
	
	/**
	 * 根据条件查询商城信息
	 * 
	 * @param eshopInf
	 * @return
	 */
	 EshopInf selectByEshopInf(EshopInf eshopInf);
	
}
