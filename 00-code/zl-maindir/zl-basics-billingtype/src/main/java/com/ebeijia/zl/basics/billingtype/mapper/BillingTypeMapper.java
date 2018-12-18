package com.ebeijia.zl.basics.billingtype.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.billingtype.domain.BillingType;

/**
 *
 * 企业员工在平台账户类型 Mapper 接口
 *
 * @Date 2018-12-18
 */
@Mapper
public interface BillingTypeMapper extends BaseMapper<BillingType> {

	public BillingType getBillingTypeInfById(String bId);
	
	public BillingType getBillingTypeInfByName(String name);
	
	public List<BillingType> getBillingTypeInfList(BillingType billingTypeInf);
	
	public int updateBillingTypeInf(BillingType billingTypeInf);
	
	public int deleteBillingTypeInf(BillingType billingTypeInf);
	
	public int insertBillingTypeInf(BillingType billingTypeInf);
}
