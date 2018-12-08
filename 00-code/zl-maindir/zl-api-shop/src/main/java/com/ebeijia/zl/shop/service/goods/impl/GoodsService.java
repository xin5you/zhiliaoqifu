package com.ebeijia.zl.shop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsDetailService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsGalleryService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsService;
import com.ebeijia.zl.shop.service.goods.IGoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    ITbEcomGoodsService goodsDao;

    @Autowired
    ITbEcomGoodsDetailService detailDao;

    @Autowired
    ITbEcomGoodsGalleryService galleryDao;


    @Override
    public PageInfo<TbEcomGoods> listGoods(Integer catid, String orderby, Integer start, Integer limit) {
        if (limit == null || limit > 100){
            limit = 20;
        }
        PageHelper.startPage(start, limit);
        List<TbEcomGoods> list = null;
        try {
            list = goodsDao.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<TbEcomGoods> page = new PageInfo<TbEcomGoods>(list);
//        page.getList().stream().filter(d ->{
//            if(!StringUtil.isNullOrEmpty(d.getGoodsDetail())){
//                goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
//            }
//            return true;
//        }).collect(Collectors.toList());
        return page;
    }

    @Override
    public TbEcomGoodsDetail getDetail(String goodsId) {
        if (StringUtils.isEmpty(goodsId)){
            return null;
        }
        TbEcomGoodsDetail detail = new TbEcomGoodsDetail();
        detail.setGoodsId(goodsId);
        return detailDao.getOne(new QueryWrapper<>(detail));
    }

    @Override
    public TbEcomGoodsGallery getGallery(String goodsId) {
        if (StringUtils.isEmpty(goodsId)){
            return null;
        }
        TbEcomGoodsGallery gallery = new TbEcomGoodsGallery();
        gallery.setGoodsId(goodsId);
        return galleryDao.getOne(new QueryWrapper<>(gallery));
    }
}
