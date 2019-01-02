package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomGoodsDetailMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 商品详情表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomGoodsDetailService extends ServiceImpl<TbEcomGoodsDetailMapper, TbEcomGoodsDetail> implements ITbEcomGoodsDetailService{

    @Autowired
    private TbEcomGoodsDetailMapper ecomGoodsDetailMapper;

    @Override
    public List<TbEcomGoodsDetail> getGoodsDetailList(TbEcomGoodsDetail ecomGoodsDetail) {
        return ecomGoodsDetailMapper.getGoodsDetailList(ecomGoodsDetail);
    }
}
