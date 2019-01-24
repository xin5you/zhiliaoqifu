package com.ebeijia.zl.web.oms.sys.service;

import com.ebeijia.zl.basics.system.domain.Organization;
import com.ebeijia.zl.basics.system.domain.Resource;
import com.ebeijia.zl.basics.system.domain.User;

import java.util.List;
import java.util.Map;

public interface UserRoleResourceService {

	/**
	 * 新增用户角色信息
	 * @param user
	 * @param roleIds
	 * @return
	 */
	int insertUserRole(User user, String[] roleIds);

	/**
	 * 更新用户角色信息
	 * @param user
	 * @param roleIds
	 * @return
	 */
	int updateUserRole(User user, String[] roleIds);

	/**
	 * 更新角色资源信息
	 * @param roleId
	 * @param resourceIds
	 * @return
	 */
	int updateRoleResource(String roleId, String[] resourceIds);

	/**
	 * 查询资源信息列表（树形结构）
	 * @return
	 */
	List<Resource> getOmsResource();

	/**
	 * 查询部门信息列表（树形结构）
	 * @return
	 */
	List<Organization> getOmsOrganization();

}
