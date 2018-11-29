package com.cn.thinkx.ecom.goodsmanage.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.goodsmanage.service.SolrGoodsService;
import com.cn.thinkx.ecom.solr.api.service.SolrGoodsApiService;

@Service(value = "solrGoodsService")
public class SolrGoodsServiceImpl implements SolrGoodsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SolrGoodsApiService solrGoodsApiService;
	
	@Autowired
	private GoodsService goodsService;

	@Override
	public BaseResult<Object> deleteSolrGoodsByGoodsId(String goodsId) {
		if(StringUtil.isNullOrEmpty(goodsId)){
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		try {
			solrGoodsApiService.deleteGoodsIndex(goodsId);
		} catch (Exception e) {
			logger.error("## 删除搜索商品异常，id：[{}]", goodsId, e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> addSolrGoods() {
		try {
			List<Goods> lists=goodsService.getGoodsSolrByGoodsId(null);
			if(lists != null && lists.size() > 0){
				for (Goods goods : lists) {
					goods.setPayRate(goodsService.getGoodsPayRateByGoodsId(goods.getGoodsId()));
				}
			}
			logger.info("## 同步搜索商品list列表：[{}]",JSONObject.toJSON(lists).toString());
			solrGoodsApiService.addGoodsBatch(lists);
		} catch (Exception e) {
			logger.error("## 同步搜索商品异常", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}
	

}
