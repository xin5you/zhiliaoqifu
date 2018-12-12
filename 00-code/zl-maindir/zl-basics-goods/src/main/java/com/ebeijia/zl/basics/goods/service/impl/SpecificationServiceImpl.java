package com.ebeijia.zl.basics.goods.service.impl;

import com.ebeijia.zl.basics.goods.domain.Specification;
import com.ebeijia.zl.basics.goods.mapper.SpecificationMapper;
import com.ebeijia.zl.basics.goods.service.SpecificationService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("specificationService")
public class SpecificationServiceImpl extends BaseServiceImpl<Specification> implements SpecificationService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	

	@Autowired
	private SpecificationMapper specificationMapper;
	
	/**
	 * 根据商品规格名称查找
	 * @param specName
	 * @return
	 * @throws Exception
	 */
	public Specification getSpecificationByName(String specName) throws Exception{
		return specificationMapper.getSpecificationByName(specName);
	}
	
	/**
	 * 保存规格名称
	 * @param spec
	 * @return
	 * @throws Exception
	 */
	public Specification  saveSpecification(Specification spec)throws Exception{
		Specification record=this.getSpecificationByName(spec.getSpecName());
		if(record ==null){
			specificationMapper.insert(spec);
			record=spec;
		}
		return record;
	}

	/**
	 * 查找所有的规格信息
	 * @param spec
	 * @return
	 * @throws Exception
	 */
	public List<Specification> getList(Specification spec)throws Exception{
		return specificationMapper.getList(spec);
	}
}
