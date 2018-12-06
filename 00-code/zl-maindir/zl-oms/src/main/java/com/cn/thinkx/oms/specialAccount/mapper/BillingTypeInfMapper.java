package com.cn.thinkx.oms.specialAccount.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;

@Mapper
public interface BillingTypeInfMapper {
	
	public BillingTypeInf getBillingTypeInfById(String bId);
	
	public BillingTypeInf getBillingTypeInfByName(String name);
	
	public int insertBillingTypeInf(BillingTypeInf billingTypeInf);
	
	public List<BillingTypeInf> getBillingTypeInfList(BillingTypeInf billingTypeInf);
	
	public int updateBillingTypeInf(BillingTypeInf billingTypeInf);
	
	public int deleteBillingTypeInf(BillingTypeInf billingTypeInf);
	
	
}
