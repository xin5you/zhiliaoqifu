package com.cn.thinkx.oms.specialAccount.service;

import java.util.List;

import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.github.pagehelper.PageInfo;

public interface CompanyInfService {

	public CompanyInf getCompanyInfById(String companyId);
	
	public int insertCompanyInf(CompanyInf companyInf);
	
	public int updateCompanyInf(CompanyInf companyInf);
	
	public int deleteCompanyInf(CompanyInf companyInf);

	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf);
	
	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize,CompanyInf companyInf);
	
	public CompanyInf getCompanyInfByLawCode(String lawCode);
	
}
