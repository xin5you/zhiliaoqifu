package com.ebeijia.zl.shop.service.pay.impl;

import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.vo.DealInfo;
import org.springframework.stereotype.Service;

@Service
public class PayService implements IPayService {
    @Override
    public int transferToCard(String token, DealInfo dealInfo) {
        return 0;
    }
}
