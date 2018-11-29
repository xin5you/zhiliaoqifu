package com.cn.thinkx.ecom.basics.goods.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface GoodsCategoryService extends BaseService<GoodsCategory> {

	/**
	 * 添加商品分类
	 * @param goodsCategory
	 * @return
	 */
//	GoodsCategory insertGoodsCategory(GoodsCategory goodsCategory);
	
	/**
	 * 查询商品分类
	 * @param goodsCategory
	 * @return
	 */
	List<GoodsCategory> getGoodsCategory(GoodsCategory goodsCategory);
	
	/**
	 * 查询商品类目信息
	 * @param catName 类目名称
	 * @param pathLevel 类目级别
	 * @param parentId 上级分类ID
	 * @param ecomCode 渠道CODE
	 * @return
	 */
	GoodsCategory findGoodsCategoryByLevel(String catName,String pathLevel,String parentId,String ecomCode) throws Exception;
	
	
	/**
	 * 保存商品类目信息
	 * @param category
	 * @return
	 * @throws Exception
	 */
	GoodsCategory saveGoodsCategory(GoodsCategory category) throws Exception;
	
	/**
	 * 保存商品类目信息
	 * @param category
	 * @return
	 * @throws Exception
	 */
	GoodsCategory insertGoodsCategory(GoodsCategory category) throws Exception;
	
	/**
	 * 查询商品二级分类
	 * @param goodsCategory
	 * @return
	 */
	List<GoodsCategory> getSecondGoodsCateGory(GoodsCategory goodsCategory);
	
	List<GoodsCategory> getGoodsCategoryList(GoodsCategory goodsCategory);
	
//	GoodsCategory findGoodsCategoryByOuterCatId(String outerCatId, String pathLevel, String parentId, String ecomCode) throws Exception;
}
