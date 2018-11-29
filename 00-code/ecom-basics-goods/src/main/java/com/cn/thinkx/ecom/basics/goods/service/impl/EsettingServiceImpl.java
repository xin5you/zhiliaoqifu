package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;
import com.cn.thinkx.ecom.basics.goods.mapper.EsettingMapper;
import com.cn.thinkx.ecom.basics.goods.mapper.SettingBannerMapper;
import com.cn.thinkx.ecom.basics.goods.service.EsettingService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("esettingService")
public class EsettingServiceImpl extends BaseServiceImpl<Esetting> implements EsettingService {
	
	@Autowired
	private EsettingMapper esettingMapper;
	
	@Autowired
	private SettingBannerMapper settingBannerMapper;

	@Override
	public Esetting getSettingByEcomCode(String ecomCode) {
		return esettingMapper.getSettingByEcomCode(ecomCode);
	}

	@Override
	public List<SettingBanner> getSettingBannerList(SettingBanner entity) {
		return settingBannerMapper.getSettingBannerList(entity);
	}

	@Override
	public List<SettingBanner> getNotSettingBannerList(SettingBanner entity) {
		return settingBannerMapper.getNotSettingBannerList(entity);
	}

}
