package com.cn.thinkx.ecom.front.api.routes.mapper;


import com.cn.thinkx.ecom.front.api.routes.domain.Routes;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoutesMapper extends BaseDao<Routes> {

	/**
	 * 根据电商名称查询电商入口信息
	 * 
	 * @param ecomName
	 * @return
	 */
	List<Routes> selectByEcomCode(String ecomCode);

	/**
	 * 商城主页（入口列表）
	 * 
	 * @param id
	 * @return
	 */
	List<Routes> selectByRoutesHomePage(String id);

}
