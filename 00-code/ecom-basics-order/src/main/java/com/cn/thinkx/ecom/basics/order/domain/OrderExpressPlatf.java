package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;
/*
 * 订单物流关联表
 */
public class OrderExpressPlatf extends BaseEntity{

	private static final long serialVersionUID = -7655982014086839784L;
	/*
	 * 主键
	 */
	private String oPackId;
	/*
	 * 订单商品明细id
	 */
	private String oItemId;
	/*
	 * SKU_CODE
	 */
	private String skuCode;
	/*
	 * 出售数量
	 */
	private String saleCount;
	/*
	 * 订单包裹id
	 */
	private String packId;
	
	public String getoPackId() {
		return oPackId;
	}
	public void setoPackId(String oPackId) {
		this.oPackId = oPackId;
	}
	public String getoItemId() {
		return oItemId;
	}
	public void setoItemId(String oItemId) {
		this.oItemId = oItemId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(String saleCount) {
		this.saleCount = saleCount;
	}
	public String getPackId() {
		return packId;
	}
	public void setPackId(String packId) {
		this.packId = packId;
	}
	@Override
	public String toString() {
		return "OrderExpressPlatf [oPackId=" + oPackId + ", oItemId=" + oItemId + ", skuCode=" + skuCode
				+ ", saleCount=" + saleCount + ", packId=" + packId + "]";
	}
	
}
