package com.cn.thinkx.ecom.basics.goods.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.goods.domain.CatGoodsRole;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface CatGoodsRoleMapper extends BaseDao<CatGoodsRole> {


	/**
	 *  查询商品分类
	 * @param catName 类目名称
	 * @param pathLevel 类目级别
	 * @return
	 */
	public CatGoodsRole getCatGoodsByCarIdAndGoodsId(@Param("catId")String catId, @Param("goodsId")String goodsId);
	
	
}

