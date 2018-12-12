package com.ebeijia.zl.web.cms.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.web.cms.system.domain.Resource;
import com.ebeijia.zl.web.cms.system.mapper.ResourceMapper;
import com.ebeijia.zl.web.cms.system.service.ResourceService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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

	@Override
	public List<Resource> getResourceTypeList(String resourceType) {
		return resourceMapper.getResourceTypeList(resourceType);
	}

	@Override
	public PageInfo<Resource> getResourcePage(int startNum, int pageSize, Resource entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Resource> list = resourceMapper.getList(entity);
		if (list != null && list.size() > 0) {
			for (Resource s : list)
				s.setDataStat(Constants.PrmStat.findByCode(s.getDataStat()).getValue());
		}
		PageInfo<Resource> page = new PageInfo<Resource>(list);
		return page;
	}

	@Override
	public List<Resource> getList1() {
		return this.getList();
	}

}
