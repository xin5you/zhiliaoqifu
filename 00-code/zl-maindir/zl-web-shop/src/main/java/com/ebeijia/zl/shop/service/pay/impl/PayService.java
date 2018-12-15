package com.ebeijia.zl.shop.service.pay.impl;

import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import org.springframework.stereotype.Service;

@Service
public class PayService implements IPayService {

    @Override
    public int transferToCard(DealInfo dealInfo) {
        return 0;
    }

    @Override
    public void payOrder(PayInfo payInfo, String session) {
        //验证输入信息有效性
        //幂等性验证
        //写入redis乐观锁
        //获取订单状态
        //写入新版本号
        //处理订单
        //持久化
    }

    @Override
    public void listDeals(String type, Long begin, Long end) {

    }
}
