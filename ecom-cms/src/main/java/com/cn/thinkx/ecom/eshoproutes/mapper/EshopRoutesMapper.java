package com.cn.thinkx.ecom.eshoproutes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.eshoproutes.domain.EshopRoutes;

@Mapper
public interface EshopRoutesMapper extends BaseDao<EshopRoutes> {

	/**
	 * 根据条件查询商城电商入口表信息（集合）
	 * 
	 * @param es
	 * @return
	 */
	List<EshopRoutes> getList(EshopRoutes es);

	/**
	 * 根据条件查询商城电商入口表信息(对象)
	 * 
	 * @param es
	 * @return
	 */
	EshopRoutes getEshopRoutes(EshopRoutes es);

}
