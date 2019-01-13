package com.ebeijia.zl.shop.service.banner;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;

public interface IBannerService {
    TbEcomBanner getImage(String pos, String spec, Integer index);
}
