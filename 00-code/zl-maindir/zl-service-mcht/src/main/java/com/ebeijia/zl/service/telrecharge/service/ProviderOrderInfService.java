package com.ebeijia.zl.service.telrecharge.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;


/**
 *
 * 供应商订单明细表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
public interface ProviderOrderInfService extends IService<ProviderOrderInf> {

	List<ProviderOrderInf> getProviderOrderInfList(ProviderOrderInf  providerOrderInf);
	
	/**
	 * 查找updateTime 10分钟以内，1分钟以上的订单
	 * @param ProviderOrderInf
	 * @return
	 */
	List<ProviderOrderInf> getListByTimer(ProviderOrderInf providerOrderInf);
	
	ProviderOrderInf getTelOrderInfByChannelOrderId(String OrderId);
}
