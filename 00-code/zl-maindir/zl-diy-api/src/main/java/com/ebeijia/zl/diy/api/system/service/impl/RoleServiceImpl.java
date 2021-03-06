package com.ebeijia.zl.diy.api.system.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.diy.api.base.service.impl.BaseServiceImpl;
import com.ebeijia.zl.diy.api.system.domain.Role;
import com.ebeijia.zl.diy.api.system.mapper.RoleMapper;
import com.ebeijia.zl.diy.api.system.service.RoleService;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Role> getUserRoleByUserId(String userId) {
		return this.roleMapper.getUserRoleByUserId(userId);
	}

	@Override
	public String getRoleNameByUserId(String userId) {
		return roleMapper.getRoleNameByUserId(userId);
	}

	@Override
	public List<Role> getRoleList() {
		return this.roleMapper.getRoleList();
	}

}
