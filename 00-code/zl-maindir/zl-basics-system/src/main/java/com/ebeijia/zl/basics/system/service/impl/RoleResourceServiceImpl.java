package com.ebeijia.zl.basics.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.RoleResource;
import com.ebeijia.zl.basics.system.mapper.RoleResourceMapper;
import com.ebeijia.zl.basics.system.service.RoleResourceService;

/**
 *
 * 平台角色资源关联表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {

	@Autowired
	private RoleResourceMapper roleResourceMapper;
	
	@Override
	public int deleteRoleResourceByRoleId(String roleId) {
		return roleResourceMapper.deleteRoleResourceByRoleId(roleId);
	}

	@Override
	public List<RoleResource> getRoleResourceByRoleId(String roleId) {
		return roleResourceMapper.getRoleResourceByRoleId(roleId);
	}

}
