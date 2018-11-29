package com.cn.thinkx.ecom.common.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T extends Serializable> {
	
	T selectByPrimaryKey(String primaryKey) throws Exception;

	int insert(T record) throws Exception;

	int insertSelective(T record) throws Exception;

	int updateByPrimaryKeySelective(T record) throws Exception;

	int updateByPrimaryKey(T record) throws Exception;

	int deleteByPrimaryKey(String primaryKey) throws Exception;
	
	List<T> getList(T record)  throws Exception;

}
