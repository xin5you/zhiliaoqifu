package com.cn.thinkx.wecard.facade.telrecharge.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.thinkx.wecard.facade.telrecharge.domain.ProviderInf;


/**
 *
 * 供应商信息 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
public interface ProviderInfService extends IService<ProviderInf> {

	int updateByDefaultRoute();
	
	public List<ProviderInf> getProviderInfList(ProviderInf providerInf);
}
