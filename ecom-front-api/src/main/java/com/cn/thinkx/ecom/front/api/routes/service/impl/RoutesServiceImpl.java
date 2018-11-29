package com.cn.thinkx.ecom.front.api.routes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.front.api.routes.domain.Routes;
import com.cn.thinkx.ecom.front.api.routes.mapper.RoutesMapper;
import com.cn.thinkx.ecom.front.api.routes.service.RoutesService;

@Service("routesService")
public class RoutesServiceImpl extends BaseServiceImpl<Routes> implements RoutesService {
	
	@Autowired
	private RoutesMapper routesMapper;

	@Override
	public List<Routes> selectByEcomCode(String ecomCode) {
		return this.routesMapper.selectByEcomCode(ecomCode);
	}

	@Override
	public List<Routes> selectByRoutesHomePage(String id) {
		return this.routesMapper.selectByRoutesHomePage(id);
	}

}
