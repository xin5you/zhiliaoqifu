package com.cn.thinkx.ecom.front.api.banner.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;

public interface EcomBannerService {

	/**
	 * 查询楼层中推荐商品信息
	 * 
	 * @param floor
	 * @return
	 */
	List<Floor> getFloorGoodsList(Floor floor);
	
	/**
	 * 查看商城对应的banner
	 * 
	 * @param entity
	 * @return
	 */
	List<SettingBanner> getSettingBannerList(SettingBanner entity);
}
