package com.ebeijia.zl.basics.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.system.domain.RoleResource;


/**
 *
 * 平台角色资源关联表 Service 接口类
 *
 * @User myGen
 * @Date 2018-12-17
 */
public interface RoleResourceService extends IService<RoleResource> {

	/**
	 * 根据角色ID删除角色资源信息表数据
	 * @param roleId
	 * @return
	 */
	int deleteRoleResourceByRoleId(String roleId);

	/**
	 * 根据角色ID查询角色资源表信息
	 * @param roleId
	 * @return
	 */
	List<RoleResource> getRoleResourceByRoleId(String roleId);

	/**
	 * 根据资源ID查询角色资源表信息
	 * @param resourceId
	 * @return
	 */
	List<RoleResource> getRoleResourceByResourceId(String resourceId);
}
