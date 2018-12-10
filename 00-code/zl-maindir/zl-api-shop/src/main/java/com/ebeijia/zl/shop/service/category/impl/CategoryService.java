package com.ebeijia.zl.shop.service.category.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.shop.constants.GoodsType;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsCategory;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsCategoryService;
import com.ebeijia.zl.shop.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    ITbEcomGoodsCategoryService goodsCategoryDao;


    @Override
    public List<TbEcomGoodsCategory> listCategory(String type) {
        TbEcomGoodsCategory category = new TbEcomGoodsCategory();
        if (GoodsType.NORMAL.equals(type)) {
            category.setListShow("1");
            return goodsCategoryDao.list(new QueryWrapper<>(category));
        }
        if (GoodsType.COUPON.equals(type)) {
            category.setListShow("2");
            return goodsCategoryDao.list(new QueryWrapper<>(category));
        }
        return null;
    }
}
