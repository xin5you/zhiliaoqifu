package com.cn.thinkx.ecom.goodsmanage.service;

import com.cn.thinkx.ecom.basics.goods.domain.ApiSpu;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.github.pagehelper.PageInfo;

public interface GoodsManageService {

	PageInfo<ApiSpu> getApiSpuPage(int startNum, int pageSize, ApiSpu entity);
	
	PageInfo<Goods> getGoodsPage(int startNum, int pageSize, Goods entity);
	
	BaseResult<Object> updateGoods(Goods entity);
	
	PageInfo<GoodsProduct> getGoodsProductPage(int startNum, int pageSize, String goodsId);
	
	BaseResult<Object> updateGoodsProduct(GoodsProduct entity);
}
