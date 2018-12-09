package com.cn.thinkx.oms.specialAccount.service;

import java.util.List;

import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.github.pagehelper.PageInfo;

public interface BillingTypeInfService {

	/**
	 * 通过ID查找专用账户类型
	 * @param bId
	 * @return 专用账户类型对象
	 */
	public BillingTypeInf getBillingTypeInfById(String bId);
	
	/**
	 * 通过name查找专用账户类型
	 * @param name
	 * @return 专用账户类型对象
	 */
	public BillingTypeInf getBillingTypeInfByName(String name);
	
	/**
	 * 查询所有开票类型
	 * @param MerchantInf
	 * @return 开票类型集合
	 */
	public List<BillingTypeInf> getBillingTypeInfList(BillingTypeInf billingTypeInf);
	
	/**
	 * 结合分页查询所有开票类型
	 * @param MerchantInf
	 * @return PageInfo对象
	 */
	public PageInfo<BillingTypeInf> getBillingTypeInfList(int startNum, int pageSize, BillingTypeInf billingTypeInf);
	
	public int updateBillingTypeInf(BillingTypeInf billingTypeInf);
	
	public int deleteBillingTypeInf(BillingTypeInf billingTypeInf);
	
	public int insertBillingTypeInf(BillingTypeInf billingTypeInf);
}
