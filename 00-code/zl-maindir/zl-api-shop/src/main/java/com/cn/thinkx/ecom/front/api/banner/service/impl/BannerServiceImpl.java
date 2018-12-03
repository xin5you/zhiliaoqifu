package com.cn.thinkx.ecom.front.api.banner.service.impl;

import com.cn.thinkx.ecom.front.api.banner.domain.Banner;
import com.cn.thinkx.ecom.front.api.banner.mapper.BannerMapper;
import com.cn.thinkx.ecom.front.api.banner.service.BannerService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import com.ebeijia.zl.common.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
				b.setChannel(Constants.ChannelEcomType.CEU03.getCode());
			else if (str.indexOf("jialebao") != -1)
				b.setChannel(Constants.ChannelEcomType.CEU02.getCode());
			else
				b.setChannel(Constants.ChannelEcomType.CEU01.getCode());
		}
		return list;
	}

	@Override
	public Banner selectBannerUrl(Banner banner) {
		return this.bannerMapper.selectBannerUrl(banner);
	}

}
