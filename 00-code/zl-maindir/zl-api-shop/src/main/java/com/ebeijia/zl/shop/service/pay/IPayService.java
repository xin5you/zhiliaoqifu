package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;

public interface IPayService {
    int transferToCard(String token, DealInfo dealInfo);

    void payOrder(String token, PayInfo payInfo, String session);

}
