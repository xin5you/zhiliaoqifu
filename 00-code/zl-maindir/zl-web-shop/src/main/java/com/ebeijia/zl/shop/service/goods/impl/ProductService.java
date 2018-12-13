package com.ebeijia.zl.shop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.constants.GoodsType;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.ebeijia.zl.shop.dao.goods.service.*;
import com.ebeijia.zl.shop.service.goods.IProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ITbEcomGoodsService goodsDao;

    @Autowired
    private ITbEcomGoodsDetailService detailDao;

    @Autowired
    private ITbEcomGoodsGalleryService galleryDao;

    @Autowired
    private ITbEcomGoodsProductService productDao;


    @Override
    public PageInfo<TbEcomGoods> listGoods(Integer catid, String orderby, Integer start, Integer limit) {
        if (limit == null || limit > 100) {
            limit = Integer.valueOf(20);
        }
        if (start == null) {
            start = Integer.valueOf(0);
        }
        //TODO orderBy
        List<TbEcomGoods> goodsList = null;
        PageHelper.startPage(start, limit);
        if (catid != null) {

        }
        TbEcomGoods queryBy = new TbEcomGoods();
        queryBy.setGoodsType(GoodsType.NORMAL);
        queryBy.setMarketEnable("1");
        queryBy.setIsDisabled("0");
        QueryWrapper<TbEcomGoods> wrapper = new QueryWrapper<>(queryBy);
        goodsList = goodsDao.list(wrapper);

        PageInfo<TbEcomGoods> page = new PageInfo<TbEcomGoods>(goodsList);
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
        if (!vaildId(goodsId)) {
            return null;
        }
        TbEcomGoodsDetail detail = new TbEcomGoodsDetail();
        detail.setGoodsId(goodsId);
        return detailDao.getOne(new QueryWrapper<>(detail));
    }

    @Override
    public TbEcomGoodsGallery getGallery(String goodsId) {
        if (!vaildId(goodsId)) {
            return null;
        }
        TbEcomGoodsGallery gallery = new TbEcomGoodsGallery();
        gallery.setGoodsId(goodsId);
        return galleryDao.getOne(new QueryWrapper<>(gallery));
    }

    @Override
    public List<TbEcomGoodsProduct> listSkuByGoodsId(String goodsId) {
        if (!vaildId(goodsId)) {
            return null;
        }
        TbEcomGoodsProduct product = new TbEcomGoodsProduct();
        product.setGoodsId(goodsId);
        return productDao.list(new QueryWrapper<>(product));
    }

    private boolean vaildId(String id) {
        if (StringUtils.isEmpty(id) || id.length() != 36) {
            return false;
        }
        return true;
    }
}
