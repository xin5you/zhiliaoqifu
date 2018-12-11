package com.cn.thinkx.wecard.facade.telrecharge.service;

import java.util.List;

import com.cn.thinkx.wecard.facade.telrecharge.domain.ProviderInf;
import com.github.pagehelper.PageInfo;

/**
 * 分销商 可购买的产品表
 * @author zhuqiuyou
 *
 */						 
public interface ProviderInfFacade {

	ProviderInf getProviderInfById(String providerId) throws Exception;

	boolean saveProviderInf(ProviderInf  ProviderInf) throws Exception;

	boolean updateProviderInf(ProviderInf  ProviderInf) throws Exception;

	boolean deleteProviderInfById(String providerId) throws Exception;
	
	List<ProviderInf> getProviderInfList(ProviderInf  providerInf) throws Exception;
	
	PageInfo<ProviderInf> getProviderInfPage(int startNum, int pageSize, ProviderInf providerInf) throws Exception; 
}
