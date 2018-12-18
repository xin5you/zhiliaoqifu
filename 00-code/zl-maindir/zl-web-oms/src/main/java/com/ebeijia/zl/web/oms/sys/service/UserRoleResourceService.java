package com.ebeijia.zl.web.oms.sys.service;

import com.ebeijia.zl.basics.system.domain.User;

public interface UserRoleResourceService {

	int insertUserRole(User user, String[] roleIds);
	
	int updateUserRole(User user, String[] roleIds);
	
	int updateRoleResource(String roleId ,String[] resourceIds);
}
