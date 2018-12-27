package com.ebeijia.zl.shop.dao.order.service.impl;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.mapper.TbEcomPlatfOrderMapper;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 订单总表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomPlatfOrderService extends ServiceImpl<TbEcomPlatfOrderMapper, TbEcomPlatfOrder> implements ITbEcomPlatfOrderService{

    @Autowired
    private TbEcomPlatfOrderMapper platfOrderMapper;

    @Override
    public List<TbEcomPlatfOrder> getPlatfOrderList(TbEcomPlatfOrder platfOrder) {
        return platfOrderMapper.getPlatfOrderList(platfOrder);
    }
}
