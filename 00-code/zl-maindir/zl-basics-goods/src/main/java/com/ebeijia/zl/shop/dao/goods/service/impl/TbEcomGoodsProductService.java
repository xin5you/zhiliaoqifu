package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.GoodsProduct;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomGoodsProductMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 货品(SKU)信息表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomGoodsProductService extends ServiceImpl<TbEcomGoodsProductMapper, TbEcomGoodsProduct> implements ITbEcomGoodsProductService{

    @Override
    public List<TbEcomGoodsProduct> getGoodsProductList(TbEcomGoodsProduct ecomGoodsProduct) {
        return baseMapper.getGoodsProductList(ecomGoodsProduct);
    }

    @Override
    public List<TbEcomGoodsProduct> getProductlistByGoodsId(String goodsId) {
        return baseMapper.getProductlistByGoodsId(goodsId);
    }

    @Override
    public TbEcomGoodsProduct getGoodsProductBySkuCode(String skuCode) {
        return baseMapper.getGoodsProductBySkuCode(skuCode);
    }

}
