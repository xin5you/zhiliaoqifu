package com.cn.thinkx.ecom.front.api.black.routes.service.impl;

import com.cn.thinkx.ecom.front.api.black.routes.domain.Routes;
import com.cn.thinkx.ecom.front.api.black.routes.mapper.RoutesMapper;
import com.cn.thinkx.ecom.front.api.black.routes.service.RoutesService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
