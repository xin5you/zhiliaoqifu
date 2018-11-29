package com.cn.thinkx.ecom.front.api.solrGoods.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.front.api.solrGoods.service.SolrGoodsService;
import com.cn.thinkx.ecom.solr.api.model.GoodsQueryParam;
import com.cn.thinkx.ecom.solr.api.model.SolrGoods;
import com.cn.thinkx.ecom.solr.api.service.SolrGoodsApiService;

@Service("solrGoodsService")
public class SolrGoodsServiceImpl implements SolrGoodsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SolrGoodsApiService solrGoodsApiService;

	@Autowired
	private GoodsService goodsService;

	public List<SolrGoods> listSolrGoods(GoodsQueryParam params) {
		List<SolrGoods> solrGoodsList = null;
		try {
			solrGoodsList = solrGoodsApiService.query(params);
		} catch (Exception e) {
			logger.error("## 搜索商品信息异常", e);
		}
		return solrGoodsList;
	}

	@Override
	public void addGoodsBatch() {
		try {
			List<Goods> lists = goodsService.getAllGoodsSolr();
			if(lists != null && lists.size() > 0){
				for (Goods goods : lists) {
					goods.setPayRate(goodsService.getGoodsPayRateByGoodsId(goods.getGoodsId()));
				}
			}
			logger.info("## 定时向solr文档中插入集合lists:[{}]", JSONObject.toJSON(lists).toString());
			solrGoodsApiService.addGoodsBatch(lists);
		} catch (Exception e) {
			logger.error("## solr添加商品信息异常", e);
		}
	}
}
