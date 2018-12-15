package com.ebeijia.zl.facade.telrecharge.service;

import java.util.List;

import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.github.pagehelper.PageInfo;

/**
 * 供应商 充值订单
 * @author zhuqiuyou
 *
 */						 
public interface ProviderOrderInfFacade {

	ProviderOrderInf getProviderOrderInfById(String regOrderId) throws Exception;

	boolean saveProviderOrderInf(ProviderOrderInf  providerOrderInf) throws Exception;

	boolean updateProviderOrderInf(ProviderOrderInf  providerOrderInf) throws Exception;

	boolean deleteProviderOrderInfById(String regOrderId) throws Exception;
	
	List<ProviderOrderInf> getProviderOrderInfList(ProviderOrderInf  providerOrderInf) throws Exception;
	
	ProviderOrderInf getTelOrderInfByChannelOrderId(String channelOrderId) throws Exception;
	
	PageInfo<ProviderOrderInf> getProviderOrderInfPage(int startNum, int pageSize, ProviderOrderInf providerOrderInf) throws Exception;
	
	/**
	 * 查找updateTime 10分钟以内，1分钟以上的订单
	 * @param ProviderOrderInf
	 * @return
	 */
	List<ProviderOrderInf> getListByTimer(ProviderOrderInf providerOrderInf);
}
