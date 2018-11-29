package com.cn.thinkx.ecom.basics.goods.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.cn.thinkx.ecom.common.service.BaseService;

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
