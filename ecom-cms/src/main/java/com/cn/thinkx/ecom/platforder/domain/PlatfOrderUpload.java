package com.cn.thinkx.ecom.platforder.domain;

public class PlatfOrderUpload {

	private String orderId;	//一级订单编号
	private String dmsRelatedKey;	//外部交易流水
	private String memberId;	//会员ID
	private String mobilePhoneNo;	//会员手机号
	private String personalName;	//会员昵称
	private String orderPrice;	//订单金额(元)
	private String orderFreightAmt;	//配送总费用(元)
	private String payType;	//支付方式类型
	private String payAmt;	//支付总额(元)
	private String payStatus;	//订单状态
	private String createTime;	//下单时间
	private String payTime;	//支付时间
	public String getOrderId() {
		return orderId;
	}
	public String getDmsRelatedKey() {
		return dmsRelatedKey;
	}
	public String getMemberId() {
		return memberId;
	}
	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}
	public String getPersonalName() {
		return personalName;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public String getOrderFreightAmt() {
		return orderFreightAmt;
	}
	public String getPayType() {
		return payType;
	}
	public String getPayAmt() {
		return payAmt;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setDmsRelatedKey(String dmsRelatedKey) {
		this.dmsRelatedKey = dmsRelatedKey;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public void setOrderFreightAmt(String orderFreightAmt) {
		this.orderFreightAmt = orderFreightAmt;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	
}
