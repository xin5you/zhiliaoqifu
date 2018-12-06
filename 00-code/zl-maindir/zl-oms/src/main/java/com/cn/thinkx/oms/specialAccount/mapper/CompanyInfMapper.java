package com.cn.thinkx.oms.specialAccount.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.specialAccount.model.CompanyInf;

@Mapper
public interface CompanyInfMapper {
	
	public CompanyInf getCompanyInfById(String cId);
	
	public CompanyInf getCompanyInfByComCode(String comCode);
	
	public CompanyInf getCompanyInfByLawCode(String lawCode);
	
	public int insertCompanyInf(CompanyInf companyInf);
	
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf);
	
	public int updateCompanyInf(CompanyInf companyInf);
	
	public int deleteCompanyInf(CompanyInf companyInf);
	
	
}
