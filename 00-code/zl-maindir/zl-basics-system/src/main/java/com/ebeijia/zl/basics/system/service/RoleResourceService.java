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

	int deleteRoleResourceByRoleId(String roleId);
	
	List<RoleResource> getRoleResourceByRoleId(String roleId);
}
