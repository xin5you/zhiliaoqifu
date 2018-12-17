package com.ebeijia.zl.shop.service.pay.impl;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrderDetails;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayService implements IPayService {

    @Override
    public int transferToCard(DealInfo dealInfo, Double session) {
        return (int) (Math.random()*10000);
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
    public List<TbEcomPayOrderDetails> listDeals(String type, Long begin, Long end) {
        return null;
    }
}
