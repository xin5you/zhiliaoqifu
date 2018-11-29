package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 订单货品明细
 * 
 * @author xiaomei
 *
 */
public class OrderProductItem extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;
	
	/*
	 * ID
	 */
	private String oItemId;
	/*
	 * 门店订单ID
	 */
	private String sOrderId;
	/*
	 * 货品ID
	 */
	private String productId;
	/*
	 * 购物车ID
	 */
	private String cartId;
	/*
	 * 货品售价
	 */
	private String productPrice;
	/*
	 * 货品销售量
	 */
	private String productNum;
	/*
	 * 货品名称
	 */
	private String productName;
	/*
	 * 获得积分
	 */
	private String gainedPoint;
	
	/*
	 * 扩展字段
	 */
	private String addon;
	/*
	 * 换货货品名
	 */
	private String changeProductName;
	/*
	 * 换货货品Id
	 */
	private String changeProductId;
	
	/**业务数据 begin*/
	
	private String skuCode; //货品的SKUID
	
	private String goodsName; //商品名称
	
	private String picUrl;	//货品图片
	
	private String isStore;	//商品可用库存
	private String enableStore;	//商品总库存
	private String goodsId;	//商品id
	
	private String refundflag;	//货品发起售后标识（0:可以发起售后，1：不可以发起售后）
	
	public String getoItemId() {
		return oItemId;
	}
	public String getsOrderId() {
		return sOrderId;
	}
	public String getProductId() {
		return productId;
	}
	public String getCartId() {
		return cartId;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public String getProductNum() {
		return productNum;
	}
	public String getProductName() {
		return productName;
	}
	public String getGainedPoint() {
		return gainedPoint;
	}
	public String getAddon() {
		return addon;
	}
	public String getChangeProductName() {
		return changeProductName;
	}
	public String getChangeProductId() {
		return changeProductId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public String getIsStore() {
		return isStore;
	}
	public String getEnableStore() {
		return enableStore;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setoItemId(String oItemId) {
		this.oItemId = oItemId;
	}
	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setGainedPoint(String gainedPoint) {
		this.gainedPoint = gainedPoint;
	}
	public void setAddon(String addon) {
		this.addon = addon;
	}
	public void setChangeProductName(String changeProductName) {
		this.changeProductName = changeProductName;
	}
	public void setChangeProductId(String changeProductId) {
		this.changeProductId = changeProductId;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public void setIsStore(String isStore) {
		this.isStore = isStore;
	}
	public void setEnableStore(String enableStore) {
		this.enableStore = enableStore;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	
	public String getRefundflag() {
		return refundflag;
	}
	public void setRefundflag(String refundflag) {
		this.refundflag = refundflag;
	}
	@Override
	public String toString() {
		return "OrderProductItem [oItemId=" + oItemId + ", sOrderId=" + sOrderId + ", productId=" + productId
				+ ", cartId=" + cartId + ", productPrice=" + productPrice + ", productNum=" + productNum
				+ ", productName=" + productName + ", gainedPoint=" + gainedPoint + ", addon=" + addon
				+ ", changeProductName=" + changeProductName + ", changeProductId=" + changeProductId + ", skuCode="
				+ skuCode + ", goodsName=" + goodsName + ", picUrl=" + picUrl + ", isStore=" + isStore
				+ ", enableStore=" + enableStore + ", goodsId=" + goodsId + ", refundflag=" + refundflag + "]";
	}
	
}
