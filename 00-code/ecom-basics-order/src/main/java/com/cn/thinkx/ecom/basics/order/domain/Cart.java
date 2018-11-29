package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 购物车表
 * 
 * @author xiaomei
 *
 */
public class Cart extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;
	/*
	 * 主键ID
	 */
	private String cartId;
	/*
	 * 商品ID
	 */
	private String productId;
	/*
	 * 商城代码
	 */
	private String ecomCode;
	/*
	 * 商品类型
	 */
	private String productType;
	/*
	 * 商品数量
	 */
	private String productNum;
	/*
	 * 重量
	 */
	private String weight;
	/*
	 * session_id
	 */
	private String sessionId;
	/*
	 *备用字段
	 */
	private String productPrice;
	/*
	 * 优惠价格
	 */
	private String preferentialPrice;
	/*
	 * 会员ID
	 */
	private String memberId;
	/*
	 * 商品有效标识
	 */
	private String isCheck;
	/*
	 * 商品状态标识
	 */
	private String isChange;
	/*
	 * 促销活动结束时间
	 */
	private String activityEndTime;
	/*
	 * 促销活动ID
	 */
	private String activityId;
	/*
	 * 促销活动详细
	 */
	private String activityDetail;
	
	/**业务数据*/
	private String picurl; //产品图片
	
	private String goodsName; //商品名称
	
	private String marketEnable;//是否上下架 0代表已下架，1代表已上架
	
	private String goodsPrice; //商品价格
	
	private String pageTitle; //货品描述
	
	private Integer isStore; //库存
	
	
	public String getCartId() {
		return cartId;
	}
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	public String getEcomCode() {
		return ecomCode;
	}
	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getPreferentialPrice() {
		return preferentialPrice;
	}
	public void setPreferentialPrice(String preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getIsChange() {
		return isChange;
	}
	public void setIsChange(String isChange) {
		this.isChange = isChange;
	}
	public String getActivityEndTime() {
		return activityEndTime;
	}
	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getActivityDetail() {
		return activityDetail;
	}
	public void setActivityDetail(String activityDetail) {
		this.activityDetail = activityDetail;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getMarketEnable() {
		return marketEnable;
	}
	public void setMarketEnable(String marketEnable) {
		this.marketEnable = marketEnable;
	}
	public Integer getIsStore() {
		return isStore;
	}
	public void setIsStore(Integer isStore) {
		this.isStore = isStore;
	}
}
