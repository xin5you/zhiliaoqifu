package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.config.ShopConfig;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface IPayService {
    @Cacheable(cacheNames = ShopConfig.ID + "TRANSFER")
    int transferToCard(DealInfo dealInfo, Double session);

    @Cacheable(cacheNames = ShopConfig.ID + "PAYORDER")
    void payOrder(PayInfo payInfo, String session);

    @Cacheable(cacheNames = ShopConfig.ID + "LISTDEALS")
    PageInfo<AccountLogVO> listDeals(String session, String userId, String type, String start, String limit);

    List<AccountVO> listAccountDetail(String openId, String session);
}
