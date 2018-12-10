package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cn.thinkx.oms.specialAccount.mapper.CompanyInfMapper;
import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("companyInfService")
public class CompanyInfServiceImpl implements CompanyInfService {
	
	@Autowired
	@Qualifier("companyInfMapper")
	private CompanyInfMapper companyInfMapper;
	
	@Autowired
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;

	/**
	 * 通过ID查找企业信息
	 * @param cId
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfById(String companyId) {
		return companyInfMapper.getCompanyInfById(companyId);
	}

	/**
	 * 保存企业信息
	 * @param companyInf
	 * @param bIds
	 * @return Boolean
	 */
	public int insertCompanyInf(CompanyInf companyInf) {
		return companyInfMapper.insertCompanyInf(companyInf);
	}

	/**
	 * 修改企业信息
	 * @param bIds
	 * @return CompanyInf
	 */
	public int updateCompanyInf(CompanyInf companyInf) {
		return companyInfMapper.updateCompanyInf(companyInf);
	}

	/**
	 * 删除企业信息
	 * @param cId
	 * @param user
	 * @return
	 */
	public int deleteCompanyInf(CompanyInf companyInf) {
		return companyInfMapper.deleteCompanyInf(companyInf);
	}

	/**
	 * 查询所有企业
	 * @param companyInf
	 * @return List
	 */
	public List<CompanyInf> getCompanyInfList(CompanyInf companyInf) {
		return companyInfMapper.getCompanyInfList(companyInf);
	}
	
	/**
	 * 结合分页查询所有企业
	 * @param startNum
	 * @param pageSize
	 * @param companyInf
	 * @return PageInfo
	 */
	public PageInfo<CompanyInf> getCompanyInfList(int startNum, int pageSize,CompanyInf companyInf) {
		PageHelper.startPage(startNum, pageSize);
		List<CompanyInf> list = getCompanyInfList(companyInf);
		PageInfo<CompanyInf> page = new PageInfo<CompanyInf>(list);
		return page;
	}

	/**
	 * 通过统一社会信用代码查询企业
	 * @param lawCode
	 * @return CompanyInf
	 */
	public CompanyInf getCompanyInfByLawCode(String lawCode) {
		return companyInfMapper.getCompanyInfByLawCode(lawCode);
	}

}
