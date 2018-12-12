package com.ebeijia.zl.web.oms.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.web.oms.sys.model.Role;
import com.ebeijia.zl.web.oms.sys.model.RoleResource;

@Mapper
public interface RoleMapper  {
	
	/***
	 * 获取角色
	 * @param roleId
	 * @return
	 */
	Role getRoleById(String roleId);
	
	/**
	 * 添加角色
	 * @param entity
	 */
	void insertRole(Role entity);

	/**
	 * 修改角色
	 * @param entity
	 */
	void updateRole(Role entity);
	
	/**
	 * 删除角色
	 * @param roleId
	 */
	void deleteRoleById(String roleId);

	/**
	 * 查询所有角色
	 * @param role
	 * @return
	 */
	List<Role> getRoleList(Role role);
	
	/**
	 * 获取某个用户的角色
	 * @param userId
	 * @return
	 */
	List<Role> getUserRoleByUserId(String userId);
	
	
	/**
	 * 删除用户角色
	 * @param entity
	 */
	void deleteUserRoleByRoleId(String roleId);
	
	/**
	 * 删除角色资源
	 * @param entity
	 */
	void deleteRoleResourceByRoleId(String roleId);
	
	
	void saveRoleResource(RoleResource roleResource);
}