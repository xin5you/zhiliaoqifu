package com.ebeijia.zl.common.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ebeijia.zl.common.core.mapper.BaseDao;
import com.ebeijia.zl.common.core.service.BaseService;
import com.ebeijia.zl.common.utils.domain.BaseEntity;


@Transactional
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

	@Autowired
	private BaseDao<T> baseDao;

	@Override
	public T selectByPrimaryKey(String primaryKey) throws Exception {
		return baseDao.selectByPrimaryKey(primaryKey);
	}

	@Override
	public int insert(T record) throws Exception {
		return baseDao.insert(record);
	}

	@Override
	public int insertSelective(T record) throws Exception {
		return baseDao.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(T record) throws Exception {
		return baseDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(T record) throws Exception {
		return baseDao.updateByPrimaryKey(record);
	}

	@Override
	public int deleteByPrimaryKey(String primaryKey) throws Exception {
		return baseDao.deleteByPrimaryKey(primaryKey);
	}

	@Override
	public List<T> getList(T record) throws Exception {
		return baseDao.getList(record);
	}

}
