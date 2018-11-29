package com.cn.thinkx.ecom.front;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cn.thinkx.ecom.FrontApp;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.solr.api.service.SolrGoodsApiService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrontApp.class) // 指定spring-boot的启动类
public class SolrTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());



	@Autowired
	private GoodsService goodsService;

	@Autowired
	private SolrGoodsApiService solrGoodsApiService;

	@Test
	public void addDoc() throws Exception {
		List<Goods> lists=goodsService.getAllGoodsSolr();
		solrGoodsApiService.addGoodsBatch(lists);
		
//		solrGoodsApiService.deleteGoodsIndex("100042");
	}

}
