package com.ebeijia.zl.basics.goods.service.impl;

import com.ebeijia.zl.basics.goods.domain.GoodsProduct;
import com.ebeijia.zl.basics.goods.mapper.GoodsProductMapper;
import com.ebeijia.zl.basics.goods.service.GoodsProductService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
