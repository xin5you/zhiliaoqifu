package com.cn.thinkx.ecom.front.api.black.banner.mapper;

import com.cn.thinkx.ecom.front.api.black.banner.domain.Banner;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
