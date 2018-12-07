package com.ebeijia.zl.shop.dao.category;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsCategory;

import java.util.List;

public interface ICategoryService {
    List<TbEcomGoodsCategory> listCategory(int type);

}
