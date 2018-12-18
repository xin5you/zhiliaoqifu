package com.ebeijia.zl.web.oms.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.basics.system.domain.RoleResource;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.domain.UserRole;
import com.ebeijia.zl.basics.system.service.ResourceService;
import com.ebeijia.zl.basics.system.service.RoleResourceService;
import com.ebeijia.zl.basics.system.service.RoleService;
import com.ebeijia.zl.basics.system.service.UserRoleService;
import com.ebeijia.zl.basics.system.service.UserService;
import com.ebeijia.zl.web.oms.sys.service.UserRoleResourceService;

@Service("userRoleResourceService")
public class UserRoleResourceServiceImpl implements UserRoleResourceService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private RoleResourceService roleResourceService;
	
	@Override
	public int insertUserRole(User user, String[] roleIds) {
		if (!userService.save(user)) {
			return 0;
		}
		if (roleIds == null && roleIds.length < 1){
			return 0;
		}
		List<UserRole> userRoleList = new ArrayList<>();
		for (int i = 0; i < roleIds.length; i++){
			UserRole userRole = new UserRole();
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
		
		if (userRoleService.deleteUserRoleByUserId(user.getId()) < 1) {
			return 0;
		}
		
		if (roleIds == null && roleIds.length < 1){
			return 0;
		}
		List<UserRole> userRoleList = new ArrayList<>();
		for (int i = 0; i < roleIds.length; i++){
			UserRole userRole = new UserRole();
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
		if (roleResourceService.deleteRoleResourceByRoleId(roleId) < 1) {
    		return 0;
    	}
		if(resourceIds == null && resourceIds.length < 1){
    		return 0;
    	}
		List<RoleResource> roleResourceList = new ArrayList<>();
		for(int i = 0; i < resourceIds.length; i++){
			RoleResource roleResource = new RoleResource();
			roleResource.setRoleId(roleId);
			roleResource.setResourceId(resourceIds[i]);
			roleResourceList.add(roleResource);
		}
		if (!roleResourceService.saveBatch(roleResourceList)) {
			return 0;
		}
		return 1;
	}

}
