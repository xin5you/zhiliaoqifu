package com.cn.thinkx.ecom.front.api.banner.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.constants.Constants.ChannelEcomType;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.front.api.banner.domain.Banner;
import com.cn.thinkx.ecom.front.api.banner.mapper.BannerMapper;
import com.cn.thinkx.ecom.front.api.banner.service.BannerService;

@Service("bannerService")
public class BannerServiceImpl extends BaseServiceImpl<Banner> implements BannerService {

	@Autowired
	private BannerMapper bannerMapper;

	@Override
	public List<Banner> selectByBannerHomePage(String id) {
		List<Banner> list = bannerMapper.selectByBannerHomePage(id);
		for (Banner b : list) {
			String str = b.getBannerUrl();
			if (str.indexOf("jia-fu") != -1)
				b.setChannel(ChannelEcomType.CEU03.getCode());
			else if (str.indexOf("jialebao") != -1)
				b.setChannel(ChannelEcomType.CEU02.getCode());
			else
				b.setChannel(ChannelEcomType.CEU01.getCode());
		}
		return list;
	}

	@Override
	public Banner selectBannerUrl(Banner banner) {
		return this.bannerMapper.selectBannerUrl(banner);
	}

}
