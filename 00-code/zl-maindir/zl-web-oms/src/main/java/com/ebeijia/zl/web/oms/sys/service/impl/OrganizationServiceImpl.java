package com.ebeijia.zl.web.oms.sys.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.web.oms.sys.mapper.OrganizationMapper;
import com.ebeijia.zl.web.oms.sys.model.Organization;
import com.ebeijia.zl.web.oms.sys.service.OrganizationService;

/**
 * 组织机构 部门表
 * @author zqy
 *
 */
@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {
	
    @Autowired
    private OrganizationMapper organizationMapper;

	@Override
	public int saveOrganization(Organization entity) {
		return organizationMapper.saveOrganization(entity);
	}

	@Override
	public int updateOrganization(Organization entity) {
		return organizationMapper.updateOrganization(entity);
	}

	@Override
	public int deleteOrganization(String organId) {
		return organizationMapper.deleteOrganization(organId);
	}

	@Override
	public Organization getOrganizationById(String organId) {
		return organizationMapper.getOrganizationById(organId);
	}

	@Override
	public List<Organization> getOrganizationList(Organization entity) {
		return organizationMapper.getOrganizationList(entity);
	}

}
