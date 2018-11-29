package com.cn.thinkx.ecom.basics.goods.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.goods.domain.Specification;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface SpecificationMapper  extends BaseDao<Specification>  {

	/**
	 * 根据商品规格名称查找
	 * @param specName
	 * @return
	 * @throws Exception
	 */
	Specification getSpecificationByName(String specName) throws Exception;
	
}
