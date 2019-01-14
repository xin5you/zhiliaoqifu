package com.ebeijia.zl.facade.telrecharge.service;

import java.util.List;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.github.pagehelper.PageInfo;

/**
 * 供应商信息接口
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

	/**
	 * 根据供应商代码查询供应商信息
	 * @param lawCode
	 * @return
	 */
	ProviderInf getProviderInfByLawCode(String lawCode) throws Exception;

	/**
	 * 根据操作顺序查询供应商信息
	 * @param operSolr
	 * @return
	 */
	ProviderInf getProviderInfByOperSolr(Integer operSolr) throws Exception;

	/**
	 * 根据主键查询供应商专项类型信息
	 * @param id
	 * @return
	 */
	public ProviderBillingTypeInf getProviderBillingTypeInfById(String id);

	/**
	 * 根据条件查询供应商专项类型信息列表
	 * @param providerBillingTypeInf
	 * @return
	 */
	public List<ProviderBillingTypeInf> getProviderBillingTypeInfList(ProviderBillingTypeInf providerBillingTypeInf);

	/**
	 * 根据条件查询供应商专项类型信息列表（分页）
	 * @param providerBillingTypeInf
	 * @return
	 */
	public PageInfo<ProviderBillingTypeInf> getProviderBillingTypeInfPage(int startNum, int pageSize,ProviderBillingTypeInf providerBillingTypeInf);

	/**
	 * 根据BId与ProviderId查询供应商专项类型信息
	 * @param providerBillingTypeInf
	 * @return
	 */
	public ProviderBillingTypeInf getProviderBillingTypeInfByBIdAndProviderId(ProviderBillingTypeInf providerBillingTypeInf);

	/**
	 * 新增供应商专项类型信息
	 * @param providerBillingTypeInf
	 * @return
	 */
	public boolean insertProviderBillingTypeInf(ProviderBillingTypeInf providerBillingTypeInf);

	/**
	 * 编辑供应商专项类型信息
	 * @param providerBillingTypeInf
	 * @return
	 */
	public boolean updateProviderBillingTypeInf(ProviderBillingTypeInf providerBillingTypeInf);

	/**
	 * 删除供应商专项类型信息
	 * @param id
	 * @return
	 */
	public boolean deleteProviderBillingTypeInf(String id);


}
