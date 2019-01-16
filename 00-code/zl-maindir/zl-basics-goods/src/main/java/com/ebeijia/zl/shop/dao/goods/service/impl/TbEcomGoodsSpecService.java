package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsSpec;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomGoodsSpecMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * SKU规格对照表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomGoodsSpecService extends ServiceImpl<TbEcomGoodsSpecMapper, TbEcomGoodsSpec> implements ITbEcomGoodsSpecService{

    @Override
    public TbEcomGoodsSpec getGoodsSpecByGoodsIdAndProductId(TbEcomGoodsSpec ecomGoodsSpec) {
        return baseMapper.getGoodsSpecByGoodsIdAndProductId(ecomGoodsSpec);
    }

    @Override
    public TbEcomGoodsSpec getGoodsSpecByProductId(String productId) {
        return baseMapper.getGoodsSpecByProductId(productId);
    }

    @Override
    public List<TbEcomGoodsSpec> getGoodsSpecByGoodsId(String goodsId) {
        return baseMapper.getGoodsSpecByGoodsId(goodsId);
    }
}
