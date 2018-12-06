package com.cn.thinkx.oms.specialAccount.service;

import java.util.List;

import com.cn.thinkx.oms.specialAccount.model.CompanyBillingTypeInf;
import com.cn.thinkx.oms.sys.model.User;

public interface CompanyBillingTypeInfService {

	
	/**
	 * 修改企业专用账户类型
	 * @param merchantId
	 * @return
	 */
	public Boolean updateCompanyBillingTypeInf(String cId,String[] bIds,User user);
	
	
	/**
	 * 通过企业id删除企业专用账户类型
	 * @param bId
	 * @return
	 */
	public int deleteCompanyBillingTypeInfByCId(CompanyBillingTypeInf companyBillingTypeInf);
	
	/**
	 * 通过开票类型id删除企业专用账户类型
	 * @param bId
	 * @return
	 */
	public int deleteCompanyBillingTypeInfByBId(CompanyBillingTypeInf companyBillingTypeInf);
	
	/**
	 * 查询所有企业开票专用账户类型
	 * @param MerchantInf
	 * @return
	 */
	public List<CompanyBillingTypeInf> getCompanyBillingTypeInfList(String cId);
	
}
