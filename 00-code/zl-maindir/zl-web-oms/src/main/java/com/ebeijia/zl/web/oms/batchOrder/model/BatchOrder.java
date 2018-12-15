package com.ebeijia.zl.web.oms.batchOrder.model;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class BatchOrder extends BaseEntity {
	private String orderId;                                 //订单号
	private String orderName;                         //订单名称
	private String orderType;                          //订单类型
	private long orderDate;                           //订单日期
	private String orderStat;                          //订单状态
	private String companyId;              				//企业ID
	private String resv1;                             //备用字段1
	private String resv2;                             //备用字段2
	private String resv3;                             //备用字段3
	private String resv4;                             //备用字段4
	private String resv5;                             //备用字段5
	private String resv6;                             //备用字段6

	private String orderCount;                     //订单数量
	private String sumAmount;                   //总金额
	private String startTime;                       //开始时间
	private String endTime;                       //结束时间
	private String disposeWait;                  //未处理
	private String disposeSuccess;             //处理成功
	private String disposeFail;                   //处理失败
	
	private String companyName;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public long getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(long orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderStat() {
		return orderStat;
	}
	public void setOrderStat(String orderStat) {
		this.orderStat = orderStat;
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
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getSumAmount() {
		return sumAmount;
	}
	public void setSumAmount(String sumAmount) {
		this.sumAmount = sumAmount;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDisposeWait() {
		return disposeWait;
	}
	public void setDisposeWait(String disposeWait) {
		this.disposeWait = disposeWait;
	}
	public String getDisposeSuccess() {
		return disposeSuccess;
	}
	public void setDisposeSuccess(String disposeSuccess) {
		this.disposeSuccess = disposeSuccess;
	}
	public String getDisposeFail() {
		return disposeFail;
	}
	public void setDisposeFail(String disposeFail) {
		this.disposeFail = disposeFail;
	}
	
	
}
