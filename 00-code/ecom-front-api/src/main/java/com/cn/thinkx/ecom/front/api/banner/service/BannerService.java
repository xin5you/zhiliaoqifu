package com.cn.thinkx.ecom.front.api.banner.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.front.api.banner.domain.Banner;

public interface BannerService extends BaseService<Banner> {

	/**
	 * 商城主页（banner列表）
	 * 
	 * @param id
	 * @return
	 */
	List<Banner> selectByBannerHomePage(String id);

	/**
	 * 根据bannerUrl和ID查询主页信息
	 * 
	 * @param banner
	 * @return
	 */
	Banner selectBannerUrl(Banner banner);

}
