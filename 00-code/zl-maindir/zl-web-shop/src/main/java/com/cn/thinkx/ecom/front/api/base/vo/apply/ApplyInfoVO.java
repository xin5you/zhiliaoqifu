package com.cn.thinkx.ecom.front.api.base.vo.apply;

public class ApplyInfoVO {

	/*
	 * 售后申请Id，最大128位
	 */
	private String requestId;
	
	/*
	 * 订单号，最大128位
	 */
	private String orderId;
	
	/*
	 * 售后申请人
	 */
	private ApplyUserVO applyUser;
	
	/*
	 * 申请售后的sku信息
	 */
	private ApplySkuVO applySku;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public ApplyUserVO getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(ApplyUserVO applyUser) {
		this.applyUser = applyUser;
	}

	public ApplySkuVO getApplySku() {
		return applySku;
	}

	public void setApplySku(ApplySkuVO applySku) {
		this.applySku = applySku;
	}

}
