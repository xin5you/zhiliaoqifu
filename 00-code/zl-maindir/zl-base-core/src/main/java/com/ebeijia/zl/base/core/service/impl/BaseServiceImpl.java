package com.ebeijia.zl.base.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ebeijia.zl.base.core.domain.BaseEntity;
import com.ebeijia.zl.base.core.mapper.BaseMapper;
import com.ebeijia.zl.base.core.service.BaseService;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

	@Autowired
	private BaseMapper<T> baseMapper;

	public T getById(String id){
		return baseMapper.getById(id);
	};

	public int insert(T t) {
		return baseMapper.insert(t);
	}

	public int update(T t) {
		return baseMapper.update(t);
	}

	public int deleteById(String Id) {
		return baseMapper.deleteById(Id);
	}

	public List<T> getList(T t){
		return baseMapper.getList(t);
	}
}
