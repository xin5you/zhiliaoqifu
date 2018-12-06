package com.cn.thinkx.oms.specialAccount.service;

import java.util.List;

import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

public interface CompanyInfService {


	/**
	 * 通过ID查找企业信息
	 * @param cId
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfById(String cId);
	
	
	/**
	 * 保存企业信息
	 * @param companyInf
	 * @param bIds
	 * @return Boolean
	 */
	public Boolean insertCompanyInf(CompanyInf companyInf,String[] bIds);
	
	/**
	 * 修改企业信息
	 * @param bIds
	 * @return CompanyInf
	 */
	public Boolean updateCompanyInf(CompanyInf companyInf,String[] bIds);
	
	
	/**
	 * 删除企业信息
	 * @param cId
	 * @param user
	 * @return
	 */
	public Boolean deleteCompanyInf(String cId,User user);
	
	/**
	 * 查询所有企业
	 * @param companyInf
	 * @return List
	 */
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf);
	
	/**
	 * 结合分页查询所有企业
	 * @param startNum
	 * @param pageSize
	 * @param companyInf
	 * @return PageInfo
	 */
	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize,CompanyInf companyInf);
	
	/**
	 * 通过企业代码查询企业
	 * @param comCode
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfByComCode(String comCode);
	
	/**
	 * 通过统一社会信用代码查询企业
	 * @param lawCode
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfByLawCode(String lawCode);
	
}
