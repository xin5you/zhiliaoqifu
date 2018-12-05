package com.cn.thinkx.ecom.system.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.system.domain.Resource;
import com.github.pagehelper.PageInfo;

public interface ResourceService extends BaseService<Resource> {

	/**
	 * 资源列表（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<Resource> getResourcePage(int startNum, int pageSize, Resource entity);

	/**
	 * 获取所有资源信息
	 * 
	 * @return
	 */
	List<Resource> getList();

	/**
	 * 获取所有资源信息
	 * 
	 * @return
	 */
	List<Resource> getList1();

	/**
	 * 根据角色Id获取该用户的权限
	 * 
	 * @param roleId
	 * @return
	 */
	List<Resource> getRoleResourceByRoleId(String roleId);

	/**
	 * 根据资源类型查询资源信息
	 * 
	 * @param resourceType
	 * @return
	 */
	List<Resource> getResourceTypeList(String resourceType);

}
