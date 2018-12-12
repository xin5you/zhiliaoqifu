package com.ebeijia.zl.basics.goods.service;

import java.util.List;

import com.ebeijia.zl.basics.goods.domain.Esetting;
import com.ebeijia.zl.basics.goods.domain.SettingBanner;
import com.ebeijia.zl.common.core.service.BaseService;

public interface EsettingService extends BaseService<Esetting> {

	Esetting getSettingByEcomCode(String ecomCode);
	
	/**
	 * 查询商城服务对应的banner
	 * 
	 * @param entity
	 * @return
	 */
	List<SettingBanner> getSettingBannerList(SettingBanner entity);
	
	/**
	 * 查询商城服务没有的banner信息
	 * 
	 * @param entity
	 * @return
	 */
	List<SettingBanner> getNotSettingBannerList(SettingBanner entity);
}
