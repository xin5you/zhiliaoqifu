package com.cn.thinkx.ecom.front.api.black.eshop.mapper;

import com.cn.thinkx.ecom.front.api.black.eshop.domain.EshopInf;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

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
