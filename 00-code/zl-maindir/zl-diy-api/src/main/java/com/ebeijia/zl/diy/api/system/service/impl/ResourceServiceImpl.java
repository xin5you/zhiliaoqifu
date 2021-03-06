package com.ebeijia.zl.diy.api.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.diy.api.base.service.impl.BaseServiceImpl;
import com.ebeijia.zl.diy.api.system.domain.Resource;
import com.ebeijia.zl.diy.api.system.mapper.ResourceMapper;
import com.ebeijia.zl.diy.api.system.service.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements ResourceService {

	@Autowired
	private ResourceMapper resourceMapper;

	public List<Resource> getRoleResourceByRoleId(String roleId) {
		return resourceMapper.getRoleResourceByRoleId(roleId);
	}

	public List<Resource> getList() {
		return resourceMapper.getList();
	}

}
