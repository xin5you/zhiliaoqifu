package com.cn.thinkx.ecom.basics.goods.mapper;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsSpecMapper  extends BaseDao<GoodsSpec> {
	
	
	/**
	 * 查找商品的规格
	 * @param goodsId
	 * @return
	 */
	List<GoodsSpec> getGoodsSpecByGoodsId(String goodsId);

}
