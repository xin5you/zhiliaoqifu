package com.ebeijia.zl.basics.goods.service;

import java.util.List;

import com.ebeijia.zl.basics.goods.domain.GoodsSpec;
import com.ebeijia.zl.common.core.service.BaseService;

/**
 * 商品货品规格对照service
 * 
 * @author zhuqiuyou
 *
 */
public interface GoodsSpecService extends BaseService<GoodsSpec> {

	/**
	 * 查找商品的规格
	 * @param goodsId
	 * @return
	 */
	List<GoodsSpec> getGoodsSpecByGoodsId(String goodsId);
}
