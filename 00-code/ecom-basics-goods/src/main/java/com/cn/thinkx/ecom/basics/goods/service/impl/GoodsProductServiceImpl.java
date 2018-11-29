package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.mapper.GoodsProductMapper;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("goodsProductService")
public class GoodsProductServiceImpl extends BaseServiceImpl<GoodsProduct> implements GoodsProductService {

	@Autowired
	private GoodsProductMapper goodsProductMapper;

	@Override
	public int updateBySkuCode(GoodsProduct goodsProduct) {
		return goodsProductMapper.updateBySkuCode(goodsProduct);
	}

	@Override
	public GoodsProduct getGoodsProductBySkuCode(String spuCode, String skuCode, String ecomCode) {
		return goodsProductMapper.getGoodsProductBySkuCode(spuCode, skuCode, ecomCode);
	}
	
	/**
	 * 查询某个商品的货品
	 * 
	 * @param goodsId
	 * @return
	 */
	public List<GoodsProduct> getProductlistByGoodsId(String goodsId){
		return goodsProductMapper.getProductlistByGoodsId(goodsId);
	}

	@Override
	public List<GoodsProduct> getInventoryList(GoodsProduct pro) {
		return goodsProductMapper.getInventoryList(pro);
	}

	@Override
	public int updateGoodsProductIsStore(GoodsProduct goodsProduct) {
		return goodsProductMapper.updateGoodsProductIsStore(goodsProduct);
	}

	@Override
	public List<GoodsProduct> getGoodsProductListByGoodsId(String goodsId) {
		return goodsProductMapper.getGoodsProductListByGoodsId(goodsId);
	}

	@Override
	public GoodsProduct getGoodsProductByPrimaryKey(String primaryKey) {
		return goodsProductMapper.getGoodsProductByPrimaryKey(primaryKey);
	}
	
}
