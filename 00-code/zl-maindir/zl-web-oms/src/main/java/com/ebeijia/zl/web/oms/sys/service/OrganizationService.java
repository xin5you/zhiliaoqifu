package com.ebeijia.zl.web.oms.sys.service;

import java.util.List;

import com.ebeijia.zl.web.oms.sys.model.Organization;

public interface OrganizationService {
	
	 int saveOrganization(Organization entity);
	 
	 int updateOrganization(Organization entity);
	 
	 int deleteOrganization(String organId);
	 
	 Organization getOrganizationById(String organId);
	 
	 List<Organization> getOrganizationList(Organization entity);
}
