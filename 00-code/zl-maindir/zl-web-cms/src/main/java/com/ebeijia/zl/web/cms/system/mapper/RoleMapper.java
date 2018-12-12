package com.ebeijia.zl.web.cms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.web.cms.system.domain.Role;
import com.ebeijia.zl.web.cms.system.domain.RoleResource;
import com.ebeijia.zl.common.core.mapper.BaseDao;

@Mapper
public interface RoleMapper extends BaseDao<Role> {

	/**
	 * 查询所有角色
	 * 
	 * @param role
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
	 * 获取某个用户的角色
	 * 
	 * @param userId
	 * @return
	 */
	List<Role> getUserRoleByUserId(String userId);

	/**
	 * 删除用户角色
	 * 
	 * @param entity
	 */
	void deleteUserRoleByRoleId(String roleId);

	/**
	 * 新增角色资源
	 * 
	 * @param roleResource
	 * @return
	 */
	int addRoleResource(RoleResource roleResource);

	/**
	 * 删除角色拥有资源
	 * 
	 * @param roleId
	 */
	void deleteRoleResourceByRoleId(String roleId);

}
