package com.cn.thinkx.ecom.front.api.base.vo.order;

public class ExpressInfoVO {

	/*
	 * 物流公司，最大32位
	 */
	private String trackingCompany;
	/*
	 * 物流单号
	 */
	private String trackingNum;	
	
	public String getTrackingCompany() {
		return trackingCompany;
	}
	public void setTrackingCompany(String trackingCompany) {
		this.trackingCompany = trackingCompany;
	}
	public String getTrackingNum() {
		return trackingNum;
	}
	public void setTrackingNum(String trackingNum) {
		this.trackingNum = trackingNum;
	}
	
	
}
