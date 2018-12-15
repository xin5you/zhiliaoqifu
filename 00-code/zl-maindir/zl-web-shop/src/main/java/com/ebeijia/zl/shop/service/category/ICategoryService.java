package com.ebeijia.zl.shop.service.category;

import com.ebeijia.zl.config.ShopConfig;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsCategory;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ICategoryService {

    @Cacheable(cacheNames = ShopConfig.ID+"category"+"#p0")
    List<TbEcomGoodsCategory> listCategory(String type);

}
