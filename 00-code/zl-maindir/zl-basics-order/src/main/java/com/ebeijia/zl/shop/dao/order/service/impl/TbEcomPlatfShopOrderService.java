package com.ebeijia.zl.shop.dao.order.service.impl;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.dao.order.mapper.TbEcomPlatfShopOrderMapper;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfShopOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 渠道子订单表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomPlatfShopOrderService extends ServiceImpl<TbEcomPlatfShopOrderMapper, TbEcomPlatfShopOrder> implements ITbEcomPlatfShopOrderService{

    @Autowired
    private TbEcomPlatfShopOrderMapper platfShopOrderMapper;

    @Override
    public List<TbEcomPlatfShopOrder> getPlatfShopOrderListByPlatfOrder(TbEcomPlatfShopOrder platfShopOrder) {
        return platfShopOrderMapper.getPlatfShopOrderListByPlatfOrder(platfShopOrder);
    }

    @Override
    public List<TbEcomPlatfShopOrder> getPlatfShopOrderList(TbEcomPlatfShopOrder platfShopOrder) {
        return platfShopOrderMapper.getPlatfShopOrderList(platfShopOrder);
    }
}
