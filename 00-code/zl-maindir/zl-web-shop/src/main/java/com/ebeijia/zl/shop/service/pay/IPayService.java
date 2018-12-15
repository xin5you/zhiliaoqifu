package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;

public interface IPayService {
    int transferToCard(DealInfo dealInfo);

    void payOrder(PayInfo payInfo, String session);

    void listDeals(String type, Long begin, Long end);

}
