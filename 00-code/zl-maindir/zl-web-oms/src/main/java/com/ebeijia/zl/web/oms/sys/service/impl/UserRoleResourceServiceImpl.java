package com.ebeijia.zl.web.oms.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ebeijia.zl.basics.system.domain.*;
import com.ebeijia.zl.basics.system.service.*;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.web.oms.common.service.RecursiveAdapter;
import com.ebeijia.zl.web.oms.common.util.RecursiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.web.oms.sys.service.UserRoleResourceService;

@Service("userRoleResourceService")
public class UserRoleResourceServiceImpl implements UserRoleResourceService {

	@Autowired
	private UserService userService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private RoleResourceService roleResourceService;
	
	@Override
	public int insertUserRole(User user, String[] roleIds) {
		if (!userService.save(user)) {
			return 0;
		}
		if (roleIds == null || roleIds.length < 1){
			return 0;
		}
		List<UserRole> userRoleList = new ArrayList<>();
		for (int i = 0; i < roleIds.length; i++){
			UserRole userRole = new UserRole();
			userRole.setId(IdUtil.getNextId());
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleIds[i]);
			userRoleList.add(userRole);
		}
		if (!userRoleService.saveBatch(userRoleList)) {
			return 0;
		}
		return 1;
	}

	@Override
	public int updateUserRole(User user, String[] roleIds) {
		if (!userService.updateById(user)) {
			return 0;
		}
		List<UserRole> urList = userRoleService.getUserRoleByUserId(user.getId());
		if (urList != null && urList.size() >= 1) {
			if (userRoleService.deleteUserRoleByUserId(user.getId()) < 1) {
				return 0;
			}
		}
		if (roleIds == null || roleIds.length < 1){
			return 0;
		}
		List<UserRole> userRoleList = new ArrayList<>();
		for (int i = 0; i < roleIds.length; i++){
			UserRole userRole = new UserRole();
			userRole.setId(IdUtil.getNextId());
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleIds[i]);
			userRoleList.add(userRole);
		}
		if (!userRoleService.saveBatch(userRoleList)) {
			return 0;
		}
		return 1;
	}

	@Override
	public int updateRoleResource(String roleId, String[] resourceIds) {
		List<RoleResource> roleResList = roleResourceService.getRoleResourceByRoleId(roleId);
		if (roleResList != null && roleResList.size() >= 1) {
			if (roleResourceService.deleteRoleResourceByRoleId(roleId) < 1) {
	    		return 0;
	    	}
		}
		if(resourceIds == null || resourceIds.length < 1){
    		return 0;
    	}
		List<RoleResource> roleResourceList = new ArrayList<>();
		for(int i = 0; i < resourceIds.length; i++){
			RoleResource roleResource = new RoleResource();
			roleResource.setId(IdUtil.getNextId());
			roleResource.setRoleId(roleId);
			roleResource.setResourceId(resourceIds[i]);
			roleResourceList.add(roleResource);
		}
		if (!roleResourceService.saveBatch(roleResourceList)) {
			return 0;
		}
		return 1;
	}

	@Override
	public List<Resource> getOmsResource() {
		Resource resource = new Resource();
		resource.setLoginType(LoginType.LoginType1.getCode());
		List<Resource> list = resourceService.getResourceList(resource);
		RecursiveUtil<Resource> recursiveUtil = new RecursiveUtil<>(new RecursiveAdapter<Resource>() {
			@Override
			public String getId(Resource resources) {
				return String.valueOf(resources.getId());
			}
			@Override
			public String getPid(Resource resources) {
				return resources.getPid();
			}
		});

		List<Resource> result = recursiveUtil.recursiveHandle(list, null);
		return result;
	}

	@Override
	public List<Organization> getOmsOrganization() {
		List<Organization> organList = organizationService.getOrganizationList(new Organization());
		RecursiveUtil<Organization> recursiveUtil = new RecursiveUtil<>(new RecursiveAdapter<Organization>() {
			@Override
			public String getId(Organization organ) {
				return String.valueOf(organ.getId());
			}
			@Override
			public String getPid(Organization organ) {
				return organ.getPid();
			}
		});

		List<Organization> result = recursiveUtil.recursiveHandle(organList, null);
		return result;
	}

}
