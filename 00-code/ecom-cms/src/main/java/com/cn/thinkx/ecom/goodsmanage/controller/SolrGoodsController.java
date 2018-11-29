package com.cn.thinkx.ecom.goodsmanage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.goodsmanage.service.SolrGoodsService;
import com.cn.thinkx.ecom.solr.api.model.GoodsQueryParam;
import com.cn.thinkx.ecom.solr.api.model.SolrGoods;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "ecom/solrGoods")
public class SolrGoodsController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SolrGoodsService solrGoodsService;

//	@RequestMapping(value = "/getSolrGoodsList")
//	public ModelAndView getSolrGoodsList(HttpServletRequest req, HttpServletResponse resp,
//			GoodsQueryParam goodsQueryParam) {
//		ModelAndView mv = new ModelAndView("goodsManage/listSolrGoodsInfo");
//		PageInfo<SolrGoods> pageList = null;
//		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
//		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 100);
////		if (!StringUtil.isNullOrEmpty(goodsQueryParam.getKeyword())) {
//			try {
//				// 查询
//				pageList = solrGoodsService.listSolrGoods(startNum, pageSize, goodsQueryParam);
//			} catch (Exception e) {
//				logger.error("## 搜索商品信息出错", e);
//			}
////		}
//		mv.addObject("pageInfo", pageList);
//		mv.addObject("goodsQueryParam", goodsQueryParam);
//		return mv;
//	}

	@RequestMapping(value = "/deleteSolrGoodsByGoodsId")
	public BaseResult<Object> deleteSolrGoodsByGoodsId(HttpServletRequest req) {
		String goodsId = req.getParameter("goodsId");
		return solrGoodsService.deleteSolrGoodsByGoodsId(goodsId);
	}

	@RequestMapping(value = "/addSolrGoods")
	public BaseResult<Object> addSolrGoods(HttpServletRequest req) {
		return solrGoodsService.addSolrGoods();
	}

}
