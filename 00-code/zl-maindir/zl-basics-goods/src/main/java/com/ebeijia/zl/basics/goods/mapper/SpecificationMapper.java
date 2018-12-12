package com.ebeijia.zl.basics.goods.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.basics.goods.domain.Specification;
import com.ebeijia.zl.common.core.mapper.BaseDao;

@Mapper
public interface SpecificationMapper  extends BaseDao<Specification> {

	/**
	 * 根据商品规格名称查找
	 * @param specName
	 * @return
	 * @throws Exception
	 */
	Specification getSpecificationByName(String specName) throws Exception;
	
}
