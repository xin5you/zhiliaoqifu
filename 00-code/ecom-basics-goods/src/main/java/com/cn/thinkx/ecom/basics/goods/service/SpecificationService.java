package com.cn.thinkx.ecom.basics.goods.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.Specification;
import com.cn.thinkx.ecom.common.service.BaseService;

/**
 * 商品规格名称 service
 * 
 * @author zhuqiuyou
 *
 */
public interface SpecificationService extends BaseService<Specification> {

	/**
	 * 根据商品规格名称查找
	 * @param specName
	 * @return
	 * @throws Exception
	 */
	Specification getSpecificationByName(String specName) throws Exception;
	
	/**
	 * 保存规格名称
	 * @param spec
	 * @return
	 * @throws Exception
	 */
	public Specification  saveSpecification(Specification spec)throws Exception;
	
	/**
	 * 查找所有的规格信息
	 * @param spec
	 * @return
	 * @throws Exception
	 */
	List<Specification> getList(Specification spec)throws Exception;
}
