package com.ebeijia.zl.basics.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

	@Override
	public int deleteRoleResourceByRoleId(String roleId) {
		List<RoleResource> roleResourceList = baseMapper.getRoleResourceByRoleId(roleId);
		if (roleResourceList != null && roleResourceList.size() >= 1) {
			return baseMapper.deleteRoleResourceByRoleId(roleId);
		}
		return 1;
	}

	@Override
	public List<RoleResource> getRoleResourceByRoleId(String roleId) {
		return baseMapper.getRoleResourceByRoleId(roleId);
	}

	@Override
	public List<RoleResource> getRoleResourceByResourceId(String resourceId) {
		QueryWrapper<RoleResource> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("resource_id", resourceId);
		return baseMapper.selectList(queryWrapper);
	}

}
