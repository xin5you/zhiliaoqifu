package com.cn.thinkx.ecom.eshopbanner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.eshopbanner.domain.EshopBanner;

@Mapper
public interface EshopBannerMapper extends BaseDao<EshopBanner> {

	/**
	 * 根据条件查询商城Banner表信息(集合)
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
