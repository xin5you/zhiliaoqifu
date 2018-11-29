package com.cn.thinkx.ecom.front.api.solrGoods.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.front.api.solrGoods.service.SolrGoodsService;
import com.cn.thinkx.ecom.solr.api.model.GoodsQueryParam;
import com.cn.thinkx.ecom.solr.api.model.SolrGoods;

@RestController
@RequestMapping(value = "ecom/solrGoods")
public class SolrGoodsController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SolrGoodsService solrGoodsService;
	
	@RequestMapping("/listSolrGoods")
	public ModelAndView listSolrGoods(HttpServletRequest req, HttpServletResponse resp,GoodsQueryParam goodsQueryParam) {
		ModelAndView mv = new ModelAndView("ecom/goods/solrGoodsList");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 200);
		try {
			List<SolrGoods> solrGoodslist = new ArrayList<SolrGoods>();
			goodsQueryParam.setStarts(startNum);
			goodsQueryParam.setRows(pageSize);
			// 查询
			solrGoodslist = solrGoodsService.listSolrGoods(goodsQueryParam);
//			logger.info("## solrGoodslist.size()=：[{}]", solrGoodslist.size());
//			logger.info("## 商品信息实体查询结果[{}]", JSONObject.toJSON(solrGoodslist).toString());
			mv.addObject("goodsQueryParam", goodsQueryParam);
			mv.addObject("solrGoodslist", solrGoodslist);
		} catch (Exception e) {
			logger.error("## 搜索商品信息出错", e);
		}
		return mv;
	}
}
