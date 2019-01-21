package com.ebeijia.zl.service.telrecharge.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;


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

	/**
	 * 根据供应商名称查询供应商信息
	 * @param providerName
	 * @return
	 */
	ProviderInf getProviderInfByProviderName(String providerName);
}
