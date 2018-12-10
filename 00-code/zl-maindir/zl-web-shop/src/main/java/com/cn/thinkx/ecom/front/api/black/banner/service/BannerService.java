package com.cn.thinkx.ecom.front.api.black.banner.service;

import com.cn.thinkx.ecom.front.api.black.banner.domain.Banner;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

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
