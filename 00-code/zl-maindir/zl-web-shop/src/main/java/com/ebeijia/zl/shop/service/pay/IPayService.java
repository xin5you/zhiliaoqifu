package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.config.ShopConfig;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrderDetails;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
public interface IPayService {
    @Cacheable(cacheNames = ShopConfig.ID+"#0/#1")
    int transferToCard(DealInfo dealInfo, Double session);
    @Cacheable(cacheNames = ShopConfig.ID+"#0/#1")
    void payOrder(PayInfo payInfo, String session);
    @Cacheable(cacheNames = ShopConfig.ID+"#0/#1/#2")
    List<TbEcomPayOrderDetails> listDeals(String type, Long begin, Long end);

}
