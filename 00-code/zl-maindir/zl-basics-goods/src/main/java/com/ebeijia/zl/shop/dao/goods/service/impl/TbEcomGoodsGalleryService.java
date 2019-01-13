package com.ebeijia.zl.shop.dao.goods.service.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.ebeijia.zl.shop.dao.goods.mapper.TbEcomGoodsGalleryMapper;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsGalleryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 商品相册表 Service 实现类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Service
public class TbEcomGoodsGalleryService extends ServiceImpl<TbEcomGoodsGalleryMapper, TbEcomGoodsGallery> implements ITbEcomGoodsGalleryService{

    @Override
    public List<TbEcomGoodsGallery> getGoodsGalleryList(TbEcomGoodsGallery ecomGoodsGallery) {
        return baseMapper.getGoodsGalleryList(ecomGoodsGallery);
    }

    @Override
    public TbEcomGoodsGallery getGoodsGalleryByIsDefault(String isDefault) {
        return baseMapper.getGoodsGalleryByIsDefault(isDefault);
    }

    @Override
    public TbEcomGoodsGallery getGoodsGalleryBySort(TbEcomGoodsGallery ecomGoodsGallery) {
        return baseMapper.getGoodsGalleryBySort(ecomGoodsGallery);
    }
}
