package com.cn.thinkx.ecom.front.api.eshop.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.front.api.eshop.domain.EshopInf;

@Mapper
public interface EshopInfMapper extends BaseDao<EshopInf> {
	
	/**
	 * 根据条件查询商城信息
	 * 
	 * @param eshopInf
	 * @return
	 */
	EshopInf selectByEshopInf(EshopInf eshopInf);
	
}
