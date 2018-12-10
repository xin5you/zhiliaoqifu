package com.cn.thinkx.oms.specialAccount.model;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class SpeAccountBatchOrderList extends BaseEntity {
	private String orderListId;                     	 //订单明细_id
	private String orderId;                              //订单号
	private String userName;                         //用户名
	private String phoneNo;                         //手机号
	private String userCardNo;                     //身份证号
	private String amount;                          //金额
	private String orderStat;                      //订单状态
	private String orderDesc;                    //订单描述
	private String resv1;                             //备用字段1
	private String resv2;                             //备用字段2
	private String resv3;                             //备用字段3
	private String resv4;                             //备用字段4
	private String resv5;                             //备用字段5
	private String resv6;                             //备用字段6
	
	private String puId;
	
	private String companyId;
	private String orderStat2;

	public String getOrderStat2() {
		return orderStat2;
	}
	public void setOrderStat2(String orderStat2) {
		this.orderStat2 = orderStat2;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getOrderListId() {
		return orderListId;
	}
	public void setOrderListId(String orderListId) {
		this.orderListId = orderListId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getUserCardNo() {
		return userCardNo;
	}
	public void setUserCardNo(String userCardNo) {
		this.userCardNo = userCardNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOrderStat() {
		return orderStat;
	}
	public void setOrderStat(String orderStat) {
		this.orderStat = orderStat;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public String getResv1() {
		return resv1;
	}
	public void setResv1(String resv1) {
		this.resv1 = resv1;
	}
	public String getResv2() {
		return resv2;
	}
	public void setResv2(String resv2) {
		this.resv2 = resv2;
	}
	public String getResv3() {
		return resv3;
	}
	public void setResv3(String resv3) {
		this.resv3 = resv3;
	}
	public String getResv4() {
		return resv4;
	}
	public void setResv4(String resv4) {
		this.resv4 = resv4;
	}
	public String getResv5() {
		return resv5;
	}
	public void setResv5(String resv5) {
		this.resv5 = resv5;
	}
	public String getResv6() {
		return resv6;
	}
	public void setResv6(String resv6) {
		this.resv6 = resv6;
	}
	public String getPuId() {
		return puId;
	}
	public void setPuId(String puId) {
		this.puId = puId;
	}
}
