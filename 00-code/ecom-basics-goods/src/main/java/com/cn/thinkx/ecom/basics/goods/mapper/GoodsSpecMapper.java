package com.cn.thinkx.ecom.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface GoodsSpecMapper  extends BaseDao<GoodsSpec>  {
	
	
	/**
	 * 查找商品的规格
	 * @param goodsId
	 * @return
	 */
	List<GoodsSpec> getGoodsSpecByGoodsId(String goodsId);

}
