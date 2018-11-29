package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.mapper.GoodsMapper;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("goodsService")
public class GoodsServiceImpl extends BaseServiceImpl<Goods> implements GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;
	
	/**
	 * 根据spu & ecomcode 查找商品
	 * @param spuCode
	 * @param ecomCode
	 * @return
	 */
	public Goods getGoodsByEcomCode(String spuCode,String ecomCode) throws Exception{
		return goodsMapper.getGoodsByEcomCode(spuCode, ecomCode);
	}

	@Override
	public List<Goods> getGoodsList(Goods goods) {
		return goodsMapper.getGoodsList(goods);
	}

	@Override
	public Goods getGoods(Goods goods) {
		return goodsMapper.getGoods(goods);
	}

	@Override
	public List<Goods> getGoodsByCategory(Goods goods) {
		return goodsMapper.getGoodsByCategory(goods);
	}
	
	/**
	 * 查找商品信息包含默认sku的信息
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public Goods selectGoodsAndDefProductByGoodId(String goodsId) throws Exception{
		return goodsMapper.selectGoodsAndDefProductByGoodId(goodsId);
	}
	
	public Goods selectGoodsByProductId(String productId){
		return goodsMapper.selectGoodsByProductId(productId);
	}
	
	/**
	 * 搜索引擎文档列表
	 * @return
	 */
	public List<Goods> getAllGoodsSolr(){
		return goodsMapper.getAllGoodsSolr();
	}

	@Override
	public List<Goods> getGoodsSolrByGoodsId(String goodsId) {
		return goodsMapper.getGoodsSolrByGoodsId(goodsId);
	}

	@Override
	public int getGoodsPayRateByGoodsId(String goodsId) {
		return goodsMapper.getGoodsPayRateByGoodsId(goodsId);
	}
}
