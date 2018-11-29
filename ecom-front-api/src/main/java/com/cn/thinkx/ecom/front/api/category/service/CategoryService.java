package com.cn.thinkx.ecom.front.api.category.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.github.pagehelper.PageInfo;

public interface CategoryService {

	PageInfo<Goods> getGoodsPage(int startNum, int pageSize, Goods entity);
	
	List<Goods> getGoodsByGoods(Goods entity);
}
