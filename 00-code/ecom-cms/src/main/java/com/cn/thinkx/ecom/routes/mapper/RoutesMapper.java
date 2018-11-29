package com.cn.thinkx.ecom.routes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.routes.domain.Routes;

@Mapper
public interface RoutesMapper extends BaseDao<Routes> {

	/**
	 * 查询所有电商入口信息
	 * 
	 * @return
	 */
	List<Routes> getList();

	/**
	 * 根据电商名称查询电商入口信息
	 * 
	 * @param ecomName
	 * @return
	 */
	Routes selectByEcomName(String ecomName);

	/**
	 * 根据电商名称查询电商入口信息
	 * 
	 * @param ecomName
	 * @return
	 */
	List<Routes> selectByEcomCode(String ecomCode);

}
