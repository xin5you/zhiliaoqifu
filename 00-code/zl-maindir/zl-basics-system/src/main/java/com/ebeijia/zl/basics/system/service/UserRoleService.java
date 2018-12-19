package com.ebeijia.zl.basics.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.system.domain.UserRole;


/**
 *
 * 平台用户角色关联表 Service 接口类
 *
 * @User myGen
 * @Date 2018-12-17
 */
public interface UserRoleService extends IService<UserRole> {

	int deleteUserRoleByUserId (String userId);
	
	List<UserRole> getUserRoleByRoleId(String roleId);
	
	List<UserRole> getUserRoleByUserId(String userId);
}
