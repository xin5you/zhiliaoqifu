package com.cn.thinkx.ecom.front.api.base.vo.order;

import java.math.BigDecimal;

public class OrderSkuVO {

	/***
	 * SKU ID
	 */
	private String skuId;

	/***
	 * 商品名称
	 */
	private String productName;

	/***
	 * 商品数量
	 */
	private int saleCount;

	/***
	 * 商品单价
	 */
	private BigDecimal originPrice;

	/***
	 * 金额小计
	 */
	private BigDecimal subtotalAmount;

	/***
	 * 优惠卷金额，可省略
	 */
	private BigDecimal couponTotalAmount;

	/***
	 * 活动优惠金额，可省略
	 */
	private BigDecimal activityTotalAmount;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}

	public BigDecimal getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(BigDecimal originPrice) {
		this.originPrice = originPrice;
	}

	public BigDecimal getSubtotalAmount() {
		return subtotalAmount;
	}

	public void setSubtotalAmount(BigDecimal subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}

	public BigDecimal getCouponTotalAmount() {
		return couponTotalAmount;
	}

	public void setCouponTotalAmount(BigDecimal couponTotalAmount) {
		this.couponTotalAmount = couponTotalAmount;
	}

	public BigDecimal getActivityTotalAmount() {
		return activityTotalAmount;
	}

	public void setActivityTotalAmount(BigDecimal activityTotalAmount) {
		this.activityTotalAmount = activityTotalAmount;
	}
}
