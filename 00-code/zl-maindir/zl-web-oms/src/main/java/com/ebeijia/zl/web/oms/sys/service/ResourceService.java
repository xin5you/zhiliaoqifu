package com.ebeijia.zl.web.oms.sys.service;

import java.util.List;

import com.ebeijia.zl.web.oms.sys.model.Resource;
import com.github.pagehelper.PageInfo;

public interface ResourceService {

	Resource getResourceById(String resourceId);
	
	Resource getResourceByKey(String key, String loginType);
	
	int insertResource(Resource resource);
	
	int updateResource(Resource resource);
	
	int deleteResource(String resourceId);
	
	// <!-- 根据角色Id获取该用户的权限-->
	List<Resource> getRoleResourceByRoleId(String roleId);

	/***
	 * 根据用户Id 查询对应的所有资源
	 * @param userId
	 * @return
	 */
	public List<Resource> getUserResourceByUserId(String userId);
	
	List<Resource> getResourceList(Resource entity);

	PageInfo<Resource> getResourcePage(int startNum, int pageSize, Resource entity);
}
