package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecValues;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomSpecValuesMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomSpecValuesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 商品规格明细 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomSpecValuesService extends ServiceImpl<TbEcomSpecValuesMapper, TbEcomSpecValues> implements ITbEcomSpecValuesService{

    @Autowired
    private TbEcomSpecValuesMapper eTbEcomSpecValuesMapper;

    @Override
    public List<TbEcomSpecValues> getGoodsSpecValuesList(TbEcomSpecValues entity) {
        return eTbEcomSpecValuesMapper.getGoodsSpecValuesList(entity);
    }
}
