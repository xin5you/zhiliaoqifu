package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.Goods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomGoodsMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 商品表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomGoodsService extends ServiceImpl<TbEcomGoodsMapper, TbEcomGoods> implements ITbEcomGoodsService{


    @Override
    public List<Goods> getGoodsList(Goods goods) {
        return baseMapper.getGoodsList(goods);
    }



    @Override
    public Goods getGoods(Goods goods) {
        return baseMapper.getGoods(goods);
    }

    @Override
    public List<Goods> getGoodsByCategory(Goods goods) {
        return baseMapper.getGoodsByCategory(goods);
    }

    @Override
    public Goods selectGoodsAndDefProductByGoodId(String goodsId) throws Exception {
        return baseMapper.selectGoodsAndDefProductByGoodId(goodsId);
    }

    @Override
    public Goods selectGoodsByProductId(String productId) {
        return baseMapper.selectGoodsByProductId(productId);
    }

    @Override
    public List<TbEcomGoods> getGoodsInfList(TbEcomGoods ecomGoods) {
        return baseMapper.getGoodsInfList(ecomGoods);
    }
}
