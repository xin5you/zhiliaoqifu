package com.ebeijia.zl.basics.goods.service.impl;

import com.ebeijia.zl.basics.goods.domain.Esetting;
import com.ebeijia.zl.basics.goods.domain.SettingBanner;
import com.ebeijia.zl.basics.goods.mapper.EsettingMapper;
import com.ebeijia.zl.basics.goods.mapper.SettingBannerMapper;
import com.ebeijia.zl.basics.goods.service.EsettingService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
