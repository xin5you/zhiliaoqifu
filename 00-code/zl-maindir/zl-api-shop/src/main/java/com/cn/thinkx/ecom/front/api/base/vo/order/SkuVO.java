package com.cn.thinkx.ecom.front.api.base.vo.order;

import java.math.BigDecimal;

/**
 * 订单中SKU数据
 * @author zhuqiuyou
 *
 */
public class SkuVO implements java.io.Serializable {

	private static final long serialVersionUID = 2316474943581615342L;

	private String skuId;
	
	private String productName;
	
	private Integer saleCount;
	
	private BigDecimal originPrice;
	
	private BigDecimal subtotalAmount;
	
	private BigDecimal couponTotalAmount;
	
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

	public Integer getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(Integer saleCount) {
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
