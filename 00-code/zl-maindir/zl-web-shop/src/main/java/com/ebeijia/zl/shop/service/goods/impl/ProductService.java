package com.ebeijia.zl.shop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsDetailService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsGalleryService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsProductService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsService;
import com.ebeijia.zl.shop.service.category.ICategoryService;
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

    @Autowired
    ICategoryService categoryService;

    @Override
    public PageInfo<TbEcomGoods> listGoods(String billingType, Integer catid, String orderby, Integer start, Integer limit) {
        if (limit == null || limit > 100) {
            limit = Integer.valueOf(20);
        }
        if (start == null) {
            start = Integer.valueOf(0);
        }

        //TODO orderBy
        List<TbEcomGoods> goodsList = null;
        PageHelper.startPage(start, limit);
        TbEcomGoods queryBy = new TbEcomGoods();
        if (catid != null) {
        }
        queryBy.setMarketEnable("1");
        queryBy.setIsDisabled("0");
        QueryWrapper<TbEcomGoods> query = new QueryWrapper<>(queryBy);
        SpecAccountTypeEnum type = SpecAccountTypeEnum.findByBId(billingType);
        if (type != null) {
            //TODO 优化sql
            query.inSql("cat_id", "select cat_id from tb_ecom_categroy_billing where b_id=" + type.getbId());
        }

        goodsList = goodsDao.list(query);

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
    public PageInfo<TbEcomGoods> listGoods(String billingType, String orderby, Integer start, Integer limit) {
        if (limit == null || limit > 100) {
            limit = Integer.valueOf(20);
        }
        if (start == null) {
            start = Integer.valueOf(0);
        }

        //TODO orderBy
        List<TbEcomGoods> goodsList = null;
        PageHelper.startPage(start, limit);
        TbEcomGoods queryBy = new TbEcomGoods();

        queryBy.setMarketEnable("1");
        queryBy.setIsDisabled("0");
        QueryWrapper<TbEcomGoods> query = new QueryWrapper<>(queryBy);
        SpecAccountTypeEnum type = SpecAccountTypeEnum.findByBId(billingType);

        if (type != null) {
            //TODO 优化sql
            query.inSql("goods_id", "select goods_id from tb_ecom_goods_billing where b_id=\"" + type.getbId()+"\"");
        }

        List<TbEcomGoods> list = goodsDao.list(query);
        return new PageInfo<>(list);
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
    public List<TbEcomGoodsGallery> getGallery(String goodsId) {
        if (!vaildId(goodsId)) {
            return null;
        }
        TbEcomGoodsGallery gallery = new TbEcomGoodsGallery();
        gallery.setGoodsId(goodsId);
        return galleryDao.list(new QueryWrapper<>(gallery));
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
