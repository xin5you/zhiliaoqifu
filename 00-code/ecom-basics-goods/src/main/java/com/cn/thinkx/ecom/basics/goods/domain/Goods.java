package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 商品
 * 
 * @author zhupan
 *
 */
public class Goods extends BaseEntity {

	private static final long serialVersionUID = 3028855457630002260L;
	private String goodsId;
	private String goodsName;
	private String spuCode;
	private String ecomCode;
	private String catId;
	private String goodsType;
	private String unit;
	private String weight;
	private String defaultSkuCode;
	private String marketEnable;
	private String brief;
	private String haveParams;
	private String haveSpec;
	private String isDisabled;
	private Long ponumber;
	private Long goodsSord;
	private String goodsWeight;
	private String grade;
	private String isHot;
	private String goodsImg;
	
	private String skuCode;
	
	private String productId; //货品Id
	private String pageTitle; //货品名称
	private String ecomType;
	private String marketType;
	private String catName; //分类名称
	private String picUrl;//图片
	private String goodsPrice;//商品价格
	private String specName;
	private String specValue;
	private String parentCatId;
	private String ecomName;
	private String parentCatName;
	private Integer payRate;	//商品的购买数
	
	

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public String getEcomCode() {
		return ecomCode;
	}

	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getDefaultSkuCode() {
		return defaultSkuCode;
	}

	public void setDefaultSkuCode(String defaultSkuCode) {
		this.defaultSkuCode = defaultSkuCode;
	}

	public String getMarketEnable() {
		return marketEnable;
	}

	public void setMarketEnable(String marketEnable) {
		this.marketEnable = marketEnable;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getHaveParams() {
		return haveParams;
	}

	public void setHaveParams(String haveParams) {
		this.haveParams = haveParams;
	}

	public String getHaveSpec() {
		return haveSpec;
	}

	public void setHaveSpec(String haveSpec) {
		this.haveSpec = haveSpec;
	}

	public String getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Long getPonumber() {
		return ponumber;
	}

	public void setPonumber(Long ponumber) {
		this.ponumber = ponumber;
	}

	public Long getGoodsSord() {
		return goodsSord;
	}

	public void setGoodsSord(Long goodsSord) {
		this.goodsSord = goodsSord;
	}

	public String getGoodsWeight() {
		return goodsWeight;
	}

	public void setGoodsWeight(String goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getEcomType() {
		return ecomType;
	}

	public void setEcomType(String ecomType) {
		this.ecomType = ecomType;
	}

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getSpecValue() {
		return specValue;
	}

	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}

	public String getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}

	public String getEcomName() {
		return ecomName;
	}

	public void setEcomName(String ecomName) {
		this.ecomName = ecomName;
	}


	public String getParentCatName() {
		return parentCatName;
	}

	public void setParentCatName(String parentCatName) {
		this.parentCatName = parentCatName;
	}

	public Integer getPayRate() {
		return payRate;
	}

	public void setPayRate(Integer payRate) {
		this.payRate = payRate;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

}
