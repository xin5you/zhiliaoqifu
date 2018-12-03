package com.cn.thinkx.ecom.basics.goods.service;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

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
