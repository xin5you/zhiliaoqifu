package com.ebeijia.zl.basics.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.UserRole;
import com.ebeijia.zl.basics.system.mapper.UserRoleMapper;
import com.ebeijia.zl.basics.system.service.UserRoleService;

/**
 *
 * 平台用户角色关联表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

	@Autowired
	private UserRoleMapper userRoleMapper;
	
	@Override
	public int deleteUserRoleByUserId(String userId) {
		return userRoleMapper.deleteUserRoleByUserId(userId);
	}

	@Override
	public List<UserRole> getUserRoleByRoleId(String roleId) {
		return userRoleMapper.getUserRoleByRoleId(roleId);
	}

}
