package com.cn.thinkx.ecom.front.api.base.vo.apply;

import java.math.BigDecimal;
import java.util.List;

public class ApplySkuVO implements java.io.Serializable {

	private static final long serialVersionUID = -4872579605957393753L;

	/*
	 * sku发货时得到的包裹号，最大64位
	 */
	private String packageId;
	
	/*
	 * 申请售后的skuId，最大32位
	 */
	private String skuId;
	
	/*
	 * 申请售后的sku数量
	 */
	private int count;
	
	/*
	 * 退换货原因
	 */
	private ApplySkuReason applySkuReason;
	
	/*
	 * 申请售后的图片信息，单张图片最大5M，最多10张，可省略
	 */
	private List<ApplyPicVO> applyPicList;
	
	/*
	 * sku单价
	 */
	private BigDecimal originalPrice;
	
	/*
	 * 实付金额小计
	 */
	private BigDecimal subtotalPrice;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ApplySkuReason getApplySkuReason() {
		return applySkuReason;
	}

	public void setApplySkuReason(ApplySkuReason applySkuReason) {
		this.applySkuReason = applySkuReason;
	}

	
	public List<ApplyPicVO> getApplyPicList() {
		return applyPicList;
	}

	public void setApplyPicList(List<ApplyPicVO> applyPicList) {
		this.applyPicList = applyPicList;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getSubtotalPrice() {
		return subtotalPrice;
	}

	public void setSubtotalPrice(BigDecimal subtotalPrice) {
		this.subtotalPrice = subtotalPrice;
	}
	
}
