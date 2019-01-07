package com.ebeijia.zl.facade.telrecharge.service;

import java.util.List;

import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.github.pagehelper.PageInfo;

/**
 * 企业服务
 * @author zhuqiuyou
 *
 */
public interface CompanyInfFacade {

	public CompanyInf getCompanyInfById(String companyId);

	public boolean insertCompanyInf(CompanyInf companyInf);

	public boolean updateCompanyInf(CompanyInf companyInf);

	public boolean deleteCompanyInf(CompanyInf companyInf);

	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf);

	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize,CompanyInf companyInf);

	public CompanyInf getCompanyInfByLawCode(String lawCode);

	public CompanyInf getCompanyInfByIsPlatform(String isPlatform);

	/**
	 * 根据主键查询企业专项类型信息
	 * @param id
	 * @return
	 */
	public CompanyBillingTypeInf getCompanyBillingTypeInfById(String id);

	/**
	 * 根据条件查询企业专项类型信息列表
	 * @param companyBillingTypeInf
	 * @return
	 */
	public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(CompanyBillingTypeInf companyBillingTypeInf);

	/**
	 * 根据条件查询企业专项类型信息列表（分页）
	 * @param companyBillingTypeInf
	 * @return
	 */
	public PageInfo<CompanyBillingTypeInf> getCompanyBillingTypeInfPage(int startNum, int pageSize,CompanyBillingTypeInf companyBillingTypeInf);

	/**
	 * 根据BId与companyId查询企业专项类型信息
	 * @param companyBillingTypeInf
	 * @return
	 */
	public CompanyBillingTypeInf getCompanyBillingTypeInfByBIdAndCompanyId(CompanyBillingTypeInf companyBillingTypeInf);

	/**
	 * 新增企业专项类型信息
	 * @param companyBillingTypeInf
	 * @return
	 */
	public boolean insertCompanyBillingTypeInf(CompanyBillingTypeInf companyBillingTypeInf);

	/**
	 * 编辑企业专项类型信息
	 * @param companyBillingTypeInf
	 * @return
	 */
	public boolean updateCompanyBillingTypeInf(CompanyBillingTypeInf companyBillingTypeInf);

	/**
	 * 删除企业专项类型信息
	 * @param id
	 * @return
	 */
	public boolean deleteCompanyBillingTypeInf(String id);
}
