package com.ebeijia.zl.shop.service.category.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.GoodsType;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsCategory;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsCategoryService;
import com.ebeijia.zl.shop.service.category.ICategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ITbEcomGoodsCategoryService goodsCategoryDao;

    @Autowired
    JedisUtilsWithNamespace jedis;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<TbEcomGoodsCategory> listCategory(String type) {
        TbEcomGoodsCategory category = new TbEcomGoodsCategory();
        String key = "GoodCategoryList";
        if (GoodsType.NORMAL.equals(type)) {
            category.setListShow("1");
            key = "GoodCategoryList";

        }else if (GoodsType.COUPON.equals(type)) {
            category.setListShow("2");
            key = "CouponCategoryList";
        }
        String goodsCategoryList = jedis.get(key);
        List<TbEcomGoodsCategory> list = getCache(goodsCategoryList);
        if (list != null) {
            return list;
        }
        list = goodsCategoryDao.list(new QueryWrapper<>(category));
        try {
            jedis.set(key,objectMapper.writeValueAsString(list),3000);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<TbEcomGoodsCategory> getCache(String categoryList) {
        if (StringUtils.isEmpty(categoryList)) {
            return null;
        }
        List<TbEcomGoodsCategory> list = new ArrayList<TbEcomGoodsCategory>();
        try {
            list = new ObjectMapper().readValue(categoryList, list.getClass());
        } catch (IOException e) {
            return null;
        }
        return list;

    }
}
