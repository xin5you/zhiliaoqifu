package com.cn.thinkx.ecom.basics.goods.service;

import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

public interface GoodsService extends BaseService<Goods> {

	/**
	 * 根据spu & ecomcode 查找商品
	 * @param spuCode
	 * @param ecomCode
	 * @return
	 */
	Goods getGoodsByEcomCode(String spuCode,String ecomCode) throws Exception;
	
	/**
	 * 查看商品信息列表
	 * @param goods
	 * @return
	 */
	List<Goods> getGoodsList(Goods goods);
	
	/**
	 * 查看商品信息
	 * @param goods
	 * @return
	 */
	Goods getGoods(Goods goods);
	
	/**
	 * 查看当前分类下的所有商品
	 * @param catId 二级分类id
	 * @return
	 */
	List<Goods> getGoodsByCategory(Goods goods);
	
	/**
	 * 查找商品信息包含默认sku的信息
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	Goods selectGoodsAndDefProductByGoodId(String goodsId) throws Exception;
	
	Goods selectGoodsByProductId(String productId);
	
	/**
	 * 搜索引擎文档列表
	 * @return
	 */
	List<Goods> getAllGoodsSolr();
	
	/**
	 * 通过商品id查询要更改搜索引擎信息
	 * 
	 * @return
	 */
	List<Goods> getGoodsSolrByGoodsId(String goodsId);
	
	/**
	 * 通过商品id查询该商品的购买数
	 * 
	 * @param goodsId
	 * @return
	 */
	int getGoodsPayRateByGoodsId(String goodsId);
}
