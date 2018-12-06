package com.cn.thinkx.oms.specialAccount.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.specialAccount.model.CompanyBillingTypeInf;

@Mapper
public interface CompanyBillingTypeInfMapper {
	
	public int insertCompanyBillingTypeInf(CompanyBillingTypeInf companyBillingTypeInf);
	
	public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(String cId);
	
	public int deleteCompanyBillingTypeInfByCId(CompanyBillingTypeInf companyBillingTypeInf);
	
	public int updateCompanyBillingTypeInf(CompanyBillingTypeInf companyBillingTypeInf);
	
	public int deleteCompanyBillingTypeInfByBId(CompanyBillingTypeInf companyBillingTypeInf);
	
	
}
