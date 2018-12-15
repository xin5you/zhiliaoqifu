package com.ebeijia.zl.shop.service.banner.impl;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.shop.dao.info.domain.TbEcomBanner;
import com.ebeijia.zl.shop.dao.info.service.ITbEcomBannerService;
import com.ebeijia.zl.shop.service.banner.IBannerService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

@Service
public class BannerService implements IBannerService {
    @Autowired
    ITbEcomBannerService bannerService;

    @ShopTransactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TbEcomBanner getImage(String pos, String spec, Integer index) {
        TbEcomBanner tbEcomBanner = new TbEcomBanner();
        for (int i=0;i++<100;) {
            tbEcomBanner.setId(IdUtil.getNextId());
            bannerService.save(tbEcomBanner);
        }
        throw new AdviceMessenger(200,"no-image");
    }
}
