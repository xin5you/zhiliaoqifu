package com.cn.thinkx.ecom.eshopbanner.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.eshopbanner.domain.EshopBanner;
import com.cn.thinkx.ecom.eshopbanner.mapper.EshopBannerMapper;
import com.cn.thinkx.ecom.eshopbanner.service.EshopBannerService;

@Service("eshopBannerService")
public class EshopBannerServiceImpl extends BaseServiceImpl<EshopBanner> implements EshopBannerService {

	@Autowired
	private EshopBannerMapper eshopBannerMapper;

	@Override
	public List<EshopBanner> getList(EshopBanner eb) {
		return this.eshopBannerMapper.getList(eb);
	}

	@Override
	public EshopBanner getEshopBanner(EshopBanner eb) {
		return this.eshopBannerMapper.getEshopBanner(eb);
	}

}
