package com.cn.thinkx.ecom.banner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.banner.domain.Banner;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface BannerMapper extends BaseDao<Banner> {

	/**
	 * 查询所有Banner信息
	 * 
	 * @return
	 */
	public List<Banner> getList();
	
}
