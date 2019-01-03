package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.config.ShopConfig;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrderDetails;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

public interface IPayService {
    @Cacheable(cacheNames = ShopConfig.ID + "TRANSFER")
    int transferToCard(DealInfo dealInfo, Double session);

    @ShopTransactional(propagation = Propagation.REQUIRES_NEW)
    int payOrder(PayInfo payInfo, String openId, String dmsRelatedKey, String desc);

    PageInfo<AccountLogVO> listDeals(String range, String type, String start, String limit);

    List<AccountVO> listAccountDetail(String openId, String session);

    List<TbEcomPayOrderDetails> getDeal(String dms);
}
