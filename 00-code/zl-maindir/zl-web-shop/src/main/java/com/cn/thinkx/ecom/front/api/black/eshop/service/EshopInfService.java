package com.cn.thinkx.ecom.front.api.black.eshop.service;

import com.cn.thinkx.ecom.front.api.black.eshop.domain.EshopInf;
import com.ebeijia.zl.common.core.service.BaseService;

public interface EshopInfService extends BaseService<EshopInf> {
	
	/**
	 * 根据条件查询商城信息
	 * 
	 * @param eshopInf
	 * @return
	 */
	 EshopInf selectByEshopInf(EshopInf eshopInf);
	
}
