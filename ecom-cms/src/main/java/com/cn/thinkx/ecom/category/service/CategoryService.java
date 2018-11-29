package com.cn.thinkx.ecom.category.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.you163.api.vo.goods.APICategoryTO;
import com.github.pagehelper.PageInfo;

public interface CategoryService {

	
	PageInfo<GoodsCategory> getCategoryPage(int startNum, int pageSize, GoodsCategory category);
	
	PageInfo<GoodsCategory> getSecondCateGoryPage(int startNum, int pageSize, GoodsCategory category);
	
	GoodsCategory getGoodsCategoryByPrimaryKey(String primaryKey);
	
	int updateGoodsCategory(GoodsCategory category);
	
	PageInfo<GoodsCategory> getGoodsCategoryListPage(int startNum, int pageSize, GoodsCategory category);
	
	void saveGoodsCategory(List<APICategoryTO> list, String ecomCode) throws Exception;
}
