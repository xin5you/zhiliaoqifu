package com.cn.thinkx.ecom.basics.goods.domain;

import java.util.List;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class GoodsProduct extends BaseEntity {
	
	private static final long serialVersionUID = -3209543443585542837L;
	
	private String productId;
	private String goodsId;
	private String spuCode;
	private String skuCode;
	private String ecomCode;
	private Long isStore;
	private Long enableStore;
	private String goodsPrice;
	private String goodsCost;
	private String mktprice;
	private String pageTitle;
	private String metaDescription;
	private String picurl;
	private String productEnable;
	
	/**货品规格值列表*/
	private List<String> specs;
	
	private String goodsName;
	private String marketType;
	private String marketEnable;
	private String ecomType;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getSpuCode() {
		return spuCode;
	}
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getEcomCode() {
		return ecomCode;
	}
	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}
	public Long getIsStore() {
		return isStore;
	}
	public void setIsStore(Long isStore) {
		this.isStore = isStore;
	}
	public Long getEnableStore() {
		return enableStore;
	}
	public void setEnableStore(Long enableStore) {
		this.enableStore = enableStore;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getGoodsCost() {
		return goodsCost;
	}
	public void setGoodsCost(String goodsCost) {
		this.goodsCost = goodsCost;
	}
	public String getMktprice() {
		return mktprice;
	}
	public void setMktprice(String mktprice) {
		this.mktprice = mktprice;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public List<String> getSpecs() {
		return specs;
	}
	public void setSpecs(List<String> specs) {
		this.specs = specs;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getMarketType() {
		return marketType;
	}
	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}
	public String getMarketEnable() {
		return marketEnable;
	}
	public void setMarketEnable(String marketEnable) {
		this.marketEnable = marketEnable;
	}
	public String getEcomType() {
		return ecomType;
	}
	public void setEcomType(String ecomType) {
		this.ecomType = ecomType;
	}
	public String getProductEnable() {
		return productEnable;
	}
	public void setProductEnable(String productEnable) {
		this.productEnable = productEnable;
	}
	
}
