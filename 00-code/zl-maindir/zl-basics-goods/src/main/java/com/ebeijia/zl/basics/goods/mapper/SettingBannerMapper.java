package com.ebeijia.zl.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.basics.goods.domain.SettingBanner;
import com.ebeijia.zl.common.core.mapper.BaseDao;

@Mapper
public interface SettingBannerMapper extends BaseDao<SettingBanner> {

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
	
	/**
	 * 刪除商城服务没有的banner信息
	 * 
	 * @param entity
	 * @return
	 */
	int deleteBySettingIdAndBannerId(SettingBanner entity);
}
