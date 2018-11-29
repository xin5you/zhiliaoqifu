package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 退货包裹确认收货信息表
 * 
 * @author kpplg
 *
 */
public class EcomExpressConfirm extends BaseEntity{

	private static final long serialVersionUID = -7734895837753984236L;
	
	private String retExpConId;
	private String returnsId;  //申请单Id
	private String trackingCompany; //物流公司
	private String trackingNum;  //物流单号
	private String trackingTime; //物流签收时间
	private String returnsState;	//退货包裹状态
	
	public String getRetExpConId() {
		return retExpConId;
	}
	public String getReturnsId() {
		return returnsId;
	}
	public String getTrackingCompany() {
		return trackingCompany;
	}
	public String getTrackingNum() {
		return trackingNum;
	}
	public String getTrackingTime() {
		return trackingTime;
	}
	public String getReturnsState() {
		return returnsState;
	}
	public void setRetExpConId(String retExpConId) {
		this.retExpConId = retExpConId;
	}
	public void setReturnsId(String returnsId) {
		this.returnsId = returnsId;
	}
	public void setTrackingCompany(String trackingCompany) {
		this.trackingCompany = trackingCompany;
	}
	public void setTrackingNum(String trackingNum) {
		this.trackingNum = trackingNum;
	}
	public void setTrackingTime(String trackingTime) {
		this.trackingTime = trackingTime;
	}
	public void setReturnsState(String returnsState) {
		this.returnsState = returnsState;
	}
	
}
