package com.cn.thinkx.oms.specialAccount.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.oms.specialAccount.model.GatewayInf;

@Mapper
public interface GatewayInfMapper {
	
	public GatewayInf getGatewayInfById(String gId);
	
	public GatewayInf getGatewayInfByGatewayInf(GatewayInf gatewayInf);
	
	public int insertGatewayInf(GatewayInf gatewayInf);
	
	public List<GatewayInf> getGatewayInfList(GatewayInf gatewayInf);
	
	public int updateGatewayInf(GatewayInf gatewayInf);
	
	public int deleteGatewayInf(GatewayInf gatewayInf);
	
	
}
