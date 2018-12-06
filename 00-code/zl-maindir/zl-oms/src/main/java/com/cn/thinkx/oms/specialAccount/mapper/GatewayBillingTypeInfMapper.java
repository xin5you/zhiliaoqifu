package com.cn.thinkx.oms.specialAccount.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.specialAccount.model.GatewayBillingTypeInf;

@Mapper
public interface GatewayBillingTypeInfMapper {
	
	public int insertGatewayBillingTypeInf(GatewayBillingTypeInf gatewayBillingTypeInf);
	
	public List<GatewayBillingTypeInf> getGatewayBillingTypeInfList(String gId);
	
	public int deleteGatewayBillingTypeInfByGId(GatewayBillingTypeInf gatewayBillingTypeInf);
	
	public int updateGatewayBillingTypeInf(GatewayBillingTypeInf gatewayBillingTypeInf);
	
	public int deleteGatewayBillingTypeInfByBId(GatewayBillingTypeInf gatewayBillingTypeInf);
	
	public int deleteGatewayBillingTypeInf(GatewayBillingTypeInf gatewayBillingTypeInf);
	
	
}
