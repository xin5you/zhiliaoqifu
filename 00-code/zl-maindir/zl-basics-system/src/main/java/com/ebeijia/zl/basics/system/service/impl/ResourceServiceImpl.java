package com.ebeijia.zl.basics.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.common.utils.tools.StringUtil;
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

	@Override
	public List<Resource> getRoleResourceByRoleId(String roleId) {
		return baseMapper.getRoleResourceByRoleId(roleId);
	}

	@Override
	public List<Resource> getUserResourceByUserId(String userId) {
		return baseMapper.getUserResourceByUserId(userId);
	}

	@Override
	public List<Resource> getResourceList(Resource entity) {
		QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("login_type", entity.getLoginType());

		if (!StringUtil.isNullOrEmpty(entity.getDataStat())) {
			queryWrapper.eq("data_stat", entity.getDataStat());
		}
		if (!StringUtil.isNullOrEmpty(entity.getResourceType())) {
			queryWrapper.eq("resource_type", entity.getResourceType());
		}
		if (!StringUtil.isNullOrEmpty(entity.getResourceName())) {
			queryWrapper.like("resource_name", entity.getResourceName());
		}
		if (!StringUtil.isNullOrEmpty(entity.getUrl())) {
			queryWrapper.like("url", entity.getUrl());
		}
		queryWrapper.orderByAsc("seq");
		return baseMapper.selectList(queryWrapper);
	}

	@Override
	public Resource getResourceByKey(String key, String loginType) {
		QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("login_type", loginType);
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		queryWrapper.eq("resource_key", key);
		return baseMapper.selectOne(queryWrapper);
	}

	@Override
	public PageInfo<Resource> getResourcePage(int startNum, int pageSize, Resource entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Resource> resourceList = getResourceList(entity);
		PageInfo<Resource> userPage = new PageInfo<Resource>(resourceList);
		return userPage;
	}

	@Override
	public Resource getResourceBySeq(Integer seq) {
		QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("seq", seq);
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		return baseMapper.selectOne(queryWrapper);
	}

	@Override
	public List<Resource> getResourceListByPidIsNull(Resource entity) {
		QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
		queryWrapper.isNull("pid");
		queryWrapper.eq("login_type", entity.getLoginType());
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		if (!StringUtil.isNullOrEmpty(entity.getResourceType())) {
			queryWrapper.eq("resource_type", entity.getResourceType());
		}
		queryWrapper.orderByAsc("seq");
		return baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<Resource> getResourceListByPidAndType(Resource entity) {
		QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("pid", entity.getPid());
		queryWrapper.eq("login_type", entity.getLoginType());
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		if (!StringUtil.isNullOrEmpty(entity.getResourceType())) {
			queryWrapper.eq("resource_type", entity.getResourceType());
		}
		queryWrapper.orderByAsc("seq");
		return baseMapper.selectList(queryWrapper);
	}

}
