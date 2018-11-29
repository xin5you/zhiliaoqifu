package com.cn.thinkx.ecom.platforder.domain;

/**
 * 电商二级订单导出数据实体类
 * 
 * @author kpplg
 *
 */
public class PlatfShopOrderUpload {

	private String sOrderId;	//二级订单编号
	private String orderId;	//一级订单编号
	private String memberId;	//会员Id
	private String personalName;	//会员昵称
	private String mobilePhoneNo;	//会员手机号
	private String ecomCode;	//电商名称
	private String payAmt;	//订单金额(元)
	private String shippingFreightPrice;	//配送费用(元)
	private String chnlOrderPrice;	//渠道订单金额
	private String chnlOrderPostage;	//渠道订单邮费
	private String subOrderStatusValue;	//订单状态
	private String createTime;	//下单时间
	private String payStatus;	//支付状态
	private String payTime;		//支付时间
	public String getSOrderId() {
		return sOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public String getMemberId() {
		return memberId;
	}
	public String getPersonalName() {
		return personalName;
	}
	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}
	public String getEcomCode() {
		return ecomCode;
	}
	public String getPayAmt() {
		return payAmt;
	}
	public String getShippingFreightPrice() {
		return shippingFreightPrice;
	}
	public String getChnlOrderPrice() {
		return chnlOrderPrice;
	}
	public String getChnlOrderPostage() {
		return chnlOrderPostage;
	}
	public String getSubOrderStatusValue() {
		return subOrderStatusValue;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setSOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}
	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}
	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}
	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}
	public void setShippingFreightPrice(String shippingFreightPrice) {
		this.shippingFreightPrice = shippingFreightPrice;
	}
	public void setChnlOrderPrice(String chnlOrderPrice) {
		this.chnlOrderPrice = chnlOrderPrice;
	}
	public void setChnlOrderPostage(String chnlOrderPostage) {
		this.chnlOrderPostage = chnlOrderPostage;
	}
	public void setSubOrderStatusValue(String subOrderStatusValue) {
		this.subOrderStatusValue = subOrderStatusValue;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
}
