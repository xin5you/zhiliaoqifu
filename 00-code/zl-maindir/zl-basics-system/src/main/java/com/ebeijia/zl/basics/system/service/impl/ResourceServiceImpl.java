package com.ebeijia.zl.basics.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ebeijia.zl.common.utils.enums.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.system.domain.Resource;
import com.ebeijia.zl.basics.system.mapper.ResourceMapper;
import com.ebeijia.zl.basics.system.service.ResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 平台资源表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public List<Resource> getRoleResourceByRoleId(String roleId) {
		return resourceMapper.getRoleResourceByRoleId(roleId);
	}

	@Override
	public List<Resource> getUserResourceByUserId(String userId) {
		return resourceMapper.getUserResourceByUserId(userId);
	}

	@Override
	public List<Resource> getResourceList(Resource entity) {
		return resourceMapper.getResourceList(entity);
	}

	@Override
	public Resource getResourceByKey(String key, String loginType) {
		Resource resource = new Resource();
		resource.setResourceKey(key);
		resource.setLoginType(loginType);
		return resourceMapper.getResourceByKey(resource);
	}

	@Override
	public PageInfo<Resource> getResourcePage(int startNum, int pageSize, Resource entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Resource> resourceList = new ArrayList<>();
		if (LoginType.LoginType1.getCode().equals(entity.getLoginType())) {
			resourceList = resourceMapper.getResourceList(entity);
		} else if (LoginType.LoginType2.getCode().equals(entity.getLoginType())) {
			resourceList = resourceMapper.getResourceListByResource(entity);
		} else if (LoginType.LoginType3.getCode().equals(entity.getLoginType())) {
			resourceList = resourceMapper.getResourceListByResource(entity);
		} else {

		}
		PageInfo<Resource> userPage = new PageInfo<Resource>(resourceList);
		return userPage;
	}

	@Override
	public List<Resource> getResourceListByResource(Resource resource) {
		return resourceMapper.getResourceListByResource(resource);
	}

}
