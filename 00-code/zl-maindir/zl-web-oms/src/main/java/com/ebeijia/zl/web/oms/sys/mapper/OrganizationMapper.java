package com.ebeijia.zl.web.oms.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.web.oms.sys.model.Organization;

@Mapper
public interface OrganizationMapper{
	
	 int saveOrganization(Organization entity);
	 
	 int updateOrganization(Organization entity);
	 
	 int deleteOrganization(String organId);
	 
	 Organization getOrganizationById(String organId);
	 
	 List<Organization> getOrganizationList(Organization entity);
	
}
