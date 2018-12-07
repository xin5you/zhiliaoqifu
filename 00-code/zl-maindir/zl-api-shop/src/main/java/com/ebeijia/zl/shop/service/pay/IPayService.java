package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.shop.vo.DealInfo;

public interface IPayService {
    int transferToCard(String token, DealInfo dealInfo);
}
