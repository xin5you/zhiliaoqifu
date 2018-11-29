package com.cn.thinkx.ecom.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface GoodsCategoryMapper extends BaseDao<GoodsCategory> {

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
	 *  查询商品分类
	 * @param catName 类目名称
	 * @param pathLevel 类目级别
	 * @param parentId 上级分类ID
	 * @param ecomCode 渠道CODE
	 * @return
	 */
	public GoodsCategory findGoodsCategoryByLevel(@Param("catName")String catName,
																				   @Param("catPath")String pathLevel,
																				   @Param("parentId")String parentId,
																				   @Param("ecomCode")String ecomCode);
	
	/**
	 * 查询商品二级分类
	 * @param goodsCategory
	 * @return
	 */
	List<GoodsCategory> getSecondGoodsCateGory(GoodsCategory goodsCategory);
	
	
	List<GoodsCategory> getGoodsCategoryList(GoodsCategory goodsCategory);
	
	GoodsCategory findGoodsCategoryByOuterCatId(@Param("outerCatId")String outerCatId,
			   @Param("catPath")String pathLevel,
			   @Param("parentId")String parentId,
			   @Param("ecomCode")String ecomCode);
}
