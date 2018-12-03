package com.cn.thinkx.ecom.basics.goods.mapper;


import com.cn.thinkx.ecom.basics.goods.domain.CatGoodsRole;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

