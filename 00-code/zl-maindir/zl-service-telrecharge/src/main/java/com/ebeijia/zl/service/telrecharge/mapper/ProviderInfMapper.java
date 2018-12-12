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
}
