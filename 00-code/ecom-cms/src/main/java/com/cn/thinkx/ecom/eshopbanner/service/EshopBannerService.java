package com.cn.thinkx.ecom.eshopbanner.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.eshopbanner.domain.EshopBanner;

public interface EshopBannerService extends BaseService<EshopBanner> {

	/**
	 * 根据条件查询商城Banner表信息
	 * 
	 * @param eb
	 * @return
	 */
	List<EshopBanner> getList(EshopBanner eb);

	/**
	 * 根据条件查询商城Banner表信息（对象）
	 * 
	 * @param eb
	 * @return
	 */
	EshopBanner getEshopBanner(EshopBanner eb);

}
