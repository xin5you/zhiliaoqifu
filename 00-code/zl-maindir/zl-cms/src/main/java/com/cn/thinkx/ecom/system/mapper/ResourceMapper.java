package com.cn.thinkx.ecom.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.system.domain.Resource;

@Mapper
public interface ResourceMapper extends BaseDao<Resource> {

	/**
	 * 查询所有资源信息
	 * 
	 * @return
	 */
	public List<Resource> getList();

	/**
	 * 查询所有资源信息
	 * 
	 * @return
	 */
	public List<Resource> getList1();

	/**
	 * 根据用户Id获取该用户的权限
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Resource> getRoleResourceByRoleId(String roleId);

	/**
	 * 根据资源类型查询资源信息
	 * 
	 * @param resourceType
	 * @return
	 */
	public List<Resource> getResourceTypeList(String resourceType);

}
