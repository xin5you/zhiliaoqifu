package com.cn.thinkx.ecom.eshoproutes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.eshoproutes.domain.EshopRoutes;
import com.cn.thinkx.ecom.eshoproutes.mapper.EshopRoutesMapper;
import com.cn.thinkx.ecom.eshoproutes.service.EshopRoutesService;

@Service("eshopRoutesService")
public class EshopRoutesServiceImpl extends BaseServiceImpl<EshopRoutes> implements EshopRoutesService {

	@Autowired
	private EshopRoutesMapper eshopRoutesMapper;

	@Override
	public List<EshopRoutes> getList(EshopRoutes es) {
		return this.eshopRoutesMapper.getList(es);
	}

	@Override
	public EshopRoutes getEshopRoutes(EshopRoutes es) {
		return this.eshopRoutesMapper.getEshopRoutes(es);
	}

}
