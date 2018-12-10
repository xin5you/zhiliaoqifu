package com.ebeijia.zl.shop.service.pay.impl;

import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import org.springframework.stereotype.Service;

@Service
public class PayService implements IPayService {

    @Override
    public int transferToCard(String token, DealInfo dealInfo) {
        return 0;
    }

    @Override
    public void payOrder(String token, PayInfo payInfo, String session) {
        //验证输入信息有效性
        //幂等性验证
        //
    }
}
