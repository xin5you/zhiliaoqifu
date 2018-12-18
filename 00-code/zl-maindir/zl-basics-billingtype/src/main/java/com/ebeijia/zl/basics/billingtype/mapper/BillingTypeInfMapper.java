package com.ebeijia.zl.basics.billingtype.mapper;
 
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.billingtype.domain.BillingTypeInf;

@Mapper
public interface BillingTypeInfMapper extends BaseMapper<BillingTypeInf> {
	
	public BillingTypeInf getBillingTypeInfById(String bId);
	
	public BillingTypeInf getBillingTypeInfByName(String name);
	
	public List<BillingTypeInf> getBillingTypeInfList(BillingTypeInf billingTypeInf);
	
	public int updateBillingTypeInf(BillingTypeInf billingTypeInf);
	
	public int deleteBillingTypeInf(BillingTypeInf billingTypeInf);
	
	public int insertBillingTypeInf(BillingTypeInf billingTypeInf);
}
