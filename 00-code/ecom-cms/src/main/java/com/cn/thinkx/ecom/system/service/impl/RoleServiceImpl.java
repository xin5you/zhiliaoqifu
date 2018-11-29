package com.cn.thinkx.ecom.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.system.domain.Role;
import com.cn.thinkx.ecom.system.domain.RoleResource;
import com.cn.thinkx.ecom.system.mapper.RoleMapper;
import com.cn.thinkx.ecom.system.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	/**
	 * 查询角色分页查询
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param role
	 * @return
	 */
	public PageInfo<Role> getRolePage(int startNum, int pageSize, Role role) {
		PageHelper.startPage(startNum, pageSize);
		List<Role> roleList = this.roleMapper.getList(role);
		PageInfo<Role> rolePage = new PageInfo<Role>(roleList);
		return rolePage;
	}

	/**
	 * 删除角色
	 * 
	 * @param roleId
	 */
	public void deleteRole(String roleId) {

		/** 删除用户与角色关系 */
		roleMapper.deleteUserRoleByRoleId(roleId);

		/** 删除角色与资源关联关系表 */
		roleMapper.deleteRoleResourceByRoleId(roleId);

		/** 删除角色 */
		roleMapper.deleteByPrimaryKey(roleId);

	}

	/**
	 * 角色授权
	 * 
	 * @param roleId
	 * @param resourceIds
	 * @return
	 */
	public int addRoleResource(String roleId, String resourceId) {
		int count = 0;
		if (resourceId != null && resourceId != "") {
			RoleResource roleResource = new RoleResource();
			roleResource.setRoleId(roleId);
			roleResource.setResourceId(resourceId);
			count = roleMapper.addRoleResource(roleResource);
		}
		return count;
	}

	@Override
	public List<Role> getUserRoleByUserId(String userId) {
		return this.roleMapper.getUserRoleByUserId(userId);
	}

	@Override
	public void deleteUserRoleByRoleId(String roleId) {
		this.roleMapper.deleteUserRoleByRoleId(roleId);
	}

	@Override
	public List<Role> getList() {
		return this.roleMapper.getList();
	}

	@Override
	public void deleteRoleResourceByRoleId(String roleId) {
		this.roleMapper.deleteRoleResourceByRoleId(roleId);
	}

	@Override
	public Role selectByName(String name) {
		return roleMapper.selectByName(name);
	}

}
