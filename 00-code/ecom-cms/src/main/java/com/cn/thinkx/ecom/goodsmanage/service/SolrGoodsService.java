package com.cn.thinkx.ecom.goodsmanage.service;

import com.cn.thinkx.ecom.common.domain.BaseResult;

public interface SolrGoodsService {

	/**
	 * 根据商品id删除搜索商品信息
	 * 
	 * @param goodsId
	 * @return
	 */
	BaseResult<Object> deleteSolrGoodsByGoodsId(String goodsId);
	
	/**
	 * 同步搜索商品信息
	 * 
	 * @return
	 */
	BaseResult<Object> addSolrGoods(); 
}
