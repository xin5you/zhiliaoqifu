package com.ebeijia.zl.shop.service.banner.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomBannerService;
import com.ebeijia.zl.shop.service.banner.IBannerService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;

@Service
public class BannerService implements IBannerService {
    @Autowired
    ITbEcomBannerService bannerService;

    @ShopTransactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TbEcomBanner getImage(String pos, String spec, Integer index) {
        TbEcomBanner query = new TbEcomBanner();
        query.setPosition(pos);
        query.setSpec(spec);
        query.setDisable("0");
        QueryWrapper<TbEcomBanner> queryWrapper = new QueryWrapper<>(query);
        queryWrapper.orderByAsc("sort");

        List<TbEcomBanner> list = bannerService.list(queryWrapper);
        if (list == null || list.size()==0){
            throw new AdviceMessenger(200, "no-image");
        }
        TbEcomBanner tbEcomBanner = list.get(index);
        if (tbEcomBanner == null) {
            tbEcomBanner = list.get(0);
        }
        if (tbEcomBanner == null) {
            throw new AdviceMessenger(200, "no-image");
        }
        return tbEcomBanner;
    }
}
