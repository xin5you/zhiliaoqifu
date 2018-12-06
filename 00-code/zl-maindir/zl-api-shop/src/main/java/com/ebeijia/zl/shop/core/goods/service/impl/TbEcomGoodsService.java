package com.ebeijia.zl.shop.core.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.shop.core.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.core.goods.mapper.TbEcomGoodsMapper;
import com.ebeijia.zl.shop.core.goods.service.ITbEcomGoodsService;
import org.springframework.stereotype.Service;


/**
 *
 * 商品表 Service 实现类
 *
 * @User J
 * @Date 2018-12-03
 */
@Service
public class TbEcomGoodsService extends ServiceImpl<TbEcomGoodsMapper, TbEcomGoods> implements ITbEcomGoodsService{

    @Override
    public boolean save(TbEcomGoods entity) {
        return super.save(entity);
    }
}
