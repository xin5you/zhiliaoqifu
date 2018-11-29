package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.basics.goods.mapper.GoodsCategoryMapper;
import com.cn.thinkx.ecom.basics.goods.service.GoodsCategoryService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl extends BaseServiceImpl<GoodsCategory> implements GoodsCategoryService {

	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	
//	@Override
//	public GoodsCategory insertGoodsCategory(GoodsCategory goodsCategory) {
//		return goodsCategoryMapper.insertGoodsCategory(goodsCategory);
//	}

	@Override
	public List<GoodsCategory> getGoodsCategory(GoodsCategory goodsCategory) {
		return goodsCategoryMapper.getGoodsCategory(goodsCategory);
	}
	
	/**
	 * 查询商品类目信息
	 * @param catName 类目名称
	 * @param pathLevel 类目级别
	 * @param parentId 上级分类ID
	 * @param ecomCode 渠道CODE
	 * @return
	 */
	public GoodsCategory findGoodsCategoryByLevel(String catName,String pathLevel,String parentId,String ecomCode){
		return goodsCategoryMapper.findGoodsCategoryByLevel(catName, pathLevel, parentId, ecomCode);
	}
	
	/**
	 * 保存商品类目信息
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public GoodsCategory saveGoodsCategory(GoodsCategory category)throws Exception{
		GoodsCategory record= goodsCategoryMapper.findGoodsCategoryByLevel(category.getCatName(), 
																														   category.getCatPath(), 
																														   category.getParentId(), 
																														   category.getEcomCode());
		if(record ==null){
			goodsCategoryMapper.insert(category);
			record=category;
		}else{
			record.setOuterCatId(category.getOuterCatId());
			goodsCategoryMapper.updateByPrimaryKey(record);
		}
		return record;
	}
	
	/**
	 * 保存商品类目信息
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public GoodsCategory insertGoodsCategory(GoodsCategory category)throws Exception{
		GoodsCategory record= goodsCategoryMapper.findGoodsCategoryByOuterCatId(category.getOuterCatId(),
																														   category.getCatPath(), 
																														   category.getParentId(), 
																														   category.getEcomCode());
		if(record ==null){
			goodsCategoryMapper.insert(category);
			record=category;
		}
		return record;
	}

	@Override
	public List<GoodsCategory> getSecondGoodsCateGory(GoodsCategory goodsCategory) {
		return goodsCategoryMapper.getSecondGoodsCateGory(goodsCategory);
	}

	@Override
	public List<GoodsCategory> getGoodsCategoryList(GoodsCategory goodsCategory) {
		return goodsCategoryMapper.getGoodsCategoryList(goodsCategory);
	}

//	@Override
//	public GoodsCategory findGoodsCategoryByOuterCatId(String outerCatId, String pathLevel, String parentId,
//			String ecomCode) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
