package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsBilling;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomGoodsBillingMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsBillingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 *
 * 商品专项账户关联表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomGoodsBillingService extends ServiceImpl<TbEcomGoodsBillingMapper, TbEcomGoodsBilling> implements ITbEcomGoodsBillingService{

    @Override
    public TbEcomGoodsBilling getGoodsBillingByGoodsId(String goodsId) {
        return baseMapper.getGoodsBillingByGoodsId(goodsId);
    }

    @Override
    public int insert(TbEcomGoodsBilling ecomGoodsBilling) {
        return baseMapper.insert(ecomGoodsBilling);
    }
}
