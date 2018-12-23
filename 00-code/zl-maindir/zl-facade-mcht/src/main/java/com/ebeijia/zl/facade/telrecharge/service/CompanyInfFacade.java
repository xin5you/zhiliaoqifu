package com.ebeijia.zl.facade.telrecharge.service;

import java.util.List;

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

}
