package com.cn.thinkx.ecom.system.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.system.domain.Role;
import com.github.pagehelper.PageInfo;

public interface RoleService extends BaseService<Role> {

	/**
	 * 查询所有角色信息
	 * 
	 * @return
	 */
	List<Role> getList();
	
	/**
	 * 根据名字查询角色信息
	 * 
	 * @param name
	 * @return
	 */
	Role selectByName(String name);

	/**
	 * 角色列表（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param role
	 * @return
	 */
	PageInfo<Role> getRolePage(int startNum, int pageSize, Role role);

	/**
	 * 新增角色资源
	 * 
	 * @param roleId
	 * @param resourceId
	 * @return
	 */
	int addRoleResource(String roleId, String resourceId);
	
	/**
	 * 删除角色资源信息
	 * @param roleId
	 */
	void deleteRoleResourceByRoleId(String roleId);

	/**
	 * 根据用户编号查询用户角色信息
	 * 
	 * @param userId
	 * @return
	 */
	List<Role> getUserRoleByUserId(String userId);

	/**
	 * 删除用户角色信息
	 * 
	 * @param roleId
	 */
	void deleteUserRoleByRoleId(String roleId);
	
	/**
	 * 删除角色信息
	 * 
	 * @param roleId
	 */
	void deleteRole(String roleId);

}
