package com.cn.thinkx.ecom.front.api.solrGoods.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.solr.api.model.GoodsQueryParam;
import com.cn.thinkx.ecom.solr.api.model.SolrGoods;

public interface SolrGoodsService {

	/**
	 * 搜索商品查询结果列表
	 * 
	 * @param params
	 * @return
	 */
	List<SolrGoods> listSolrGoods(GoodsQueryParam params);
	
	/**
	 * 批量将商品信息插入到solr中（一天内修改的商品）
	 * 
	 * @param list
	 * @throws IOException
	 * @throws SolrServerException
	 */
	void addGoodsBatch();
}
