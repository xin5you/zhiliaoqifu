package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecification;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomSpecificationMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomSpecificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 商品规格表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomSpecificationService extends ServiceImpl<TbEcomSpecificationMapper, TbEcomSpecification> implements ITbEcomSpecificationService{

    @Autowired
    private TbEcomSpecificationMapper ecomSpecificationMapper;

    @Override
    public List<TbEcomSpecification> getGoodsSpecList(TbEcomSpecification ecomSpecification) {
        return ecomSpecificationMapper.getGoodsSpecList(ecomSpecification);
    }
}
