package com.ebeijia.zl.basics.billingtype.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.billingtype.domain.BillingType;
import com.github.pagehelper.PageInfo;

/**
 *
 * 企业员工在平台账户类型 Service 接口类
 *
 * @User J
 * @Date 2018-12-18
 */
public interface BillingTypeService extends IService<BillingType> {

	/** 
	 * 通过ID查找专用账户类型
	 * @param bId
	 * @return 专用账户类型对象
	 */
	public BillingType getBillingTypeInfById(String bId);
	
	/**
	 * 通过name查找专用账户类型
	 * @param name
	 * @return 专用账户类型对象
	 */
	public BillingType getBillingTypeInfByName(String name);
	
	/**
	 * 查询所有开票类型
	 * @param MerchantInf
	 * @return 开票类型集合
	 */
	public List<BillingType> getBillingTypeInfList(BillingType billingTypeInf);
	
	/**
	 * 结合分页查询所有开票类型
	 * @param MerchantInf
	 * @return PageInfo对象
	 */
	public PageInfo<BillingType> getBillingTypeInfListPage(int startNum, int pageSize, BillingType billingTypeInf);
	
	public int updateBillingTypeInf(BillingType billingTypeInf);
	
	public int deleteBillingTypeInf(BillingType billingTypeInf);
	
	public int insertBillingTypeInf(BillingType billingTypeInf);
}
