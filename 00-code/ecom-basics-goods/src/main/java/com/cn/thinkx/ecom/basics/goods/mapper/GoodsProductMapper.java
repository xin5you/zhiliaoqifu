package com.cn.thinkx.ecom.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface GoodsProductMapper extends BaseDao<GoodsProduct> {
	
	/**
	 * 通过skuCode修改库存
	 * 
	 * @param goodsProduct
	 * @return
	 */
	int updateBySkuCode(GoodsProduct goodsProduct);
	
	/**
	 * 根据 spuCode & skuCode &ecomCode 查找货品
	 * @param spuCode
	 * @param skuCode
	 * @param ecomCode
	 * @return
	 */
	GoodsProduct getGoodsProductBySkuCode(@Param("spuCode") String spuCode,@Param("skuCode") String skuCode,@Param("ecomCode") String ecomCode);
	
	/**
	 * 查询某个商品的货品
	 * 
	 * @param goodsId
	 * @return
	 */
	public List<GoodsProduct> getProductlistByGoodsId(String goodsId);
	
	/**
	 * 库存列表
	 * 
	 * @param pro
	 * @return
	 */
	List<GoodsProduct>getInventoryList(GoodsProduct pro);
	
	/**
	 * 下单成功后修改货品的库存
	 * @param productId 货品 ID
	 * @param isStore 购买的数量
	 */
	int updateGoodsProductIsStore(GoodsProduct goodsProduct);
	
	List<GoodsProduct> getGoodsProductListByGoodsId(String goodsId);
	
	GoodsProduct getGoodsProductByPrimaryKey(String primaryKey);
}
