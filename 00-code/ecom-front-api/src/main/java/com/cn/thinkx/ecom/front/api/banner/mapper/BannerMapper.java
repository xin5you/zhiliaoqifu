package com.cn.thinkx.ecom.front.api.banner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.front.api.banner.domain.Banner;

@Mapper
public interface BannerMapper extends BaseDao<Banner> {

	/**
	 * 查询所有Banner信息
	 * 
	 * @return
	 */
	public List<Banner> getList();
	
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
