package com.ebeijia.zl.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.basics.goods.domain.GoodsSpec;
import com.ebeijia.zl.common.core.mapper.BaseDao;

@Mapper
public interface GoodsSpecMapper  extends BaseDao<GoodsSpec> {
	
	
	/**
	 * 查找商品的规格
	 * @param goodsId
	 * @return
	 */
	List<GoodsSpec> getGoodsSpecByGoodsId(String goodsId);

}
