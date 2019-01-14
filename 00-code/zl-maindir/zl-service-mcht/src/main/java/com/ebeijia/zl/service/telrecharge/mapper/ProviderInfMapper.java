package com.ebeijia.zl.service.telrecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;

@Mapper
public interface ProviderInfMapper extends BaseMapper<ProviderInf> {

	/**
	 * 修改原来是默认路由的数据为不是默认路由
	 * 
	 * @return
	 */
	int updateByDefaultRoute();
	
	List<ProviderInf> getList(ProviderInf providerInf);

	/**
	 * 根据供应商代码查询供应商信息
	 * @param lawCode
	 * @return
	 */
	ProviderInf getProviderInfByLawCode(String lawCode);

	/**
	 * 根据操作顺序查询供应商信息
	 * @param operSolr
	 * @return
	 */
	ProviderInf getProviderInfByOperSolr(Integer operSolr);
	
}
