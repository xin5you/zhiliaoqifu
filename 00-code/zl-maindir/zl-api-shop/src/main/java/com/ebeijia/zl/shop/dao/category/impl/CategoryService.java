package com.ebeijia.zl.shop.dao.category.impl;

import com.ebeijia.zl.shop.dao.category.ICategoryService;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Override
    public List<TbEcomGoodsCategory> listCategory(int type) {
        return null;
    }
}
