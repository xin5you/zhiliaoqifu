package com.cn.thinkx.ecom.order.domain;

/**
 * 交易流水明细导出表格类
 * 
 * @author xiaomei
 *
 */
public class OrderDetailUpload{

	private String orderNo;			//电商流水
	private String routerOrderNo;	//外部流水
	private String settleDate;		//清算日期
	private String personalName;	//用户名称
	private String eshopName;		//商城名称
	private String channelName;		//电商名称
	private String txnAmount;		//交易金额
	private String transTime;		//交易时间
	private String transType;		//交易状态

	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getRouterOrderNo() {
		return routerOrderNo;
	}
	
	public void setRouterOrderNo(String routerOrderNo) {
		this.routerOrderNo = routerOrderNo;
	}
	
	public String getSettleDate() {
		return settleDate;
	}
	
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	
	public String getPersonalName() {
		return personalName;
	}
	
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}
	
	public String getEshopName() {
		return eshopName;
	}
	
	public void setEshopName(String eshopName) {
		this.eshopName = eshopName;
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	public String getTxnAmount() {
		return txnAmount;
	}
	
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	
	public String getTransTime() {
		return transTime;
	}
	
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	
	public String getTransType() {
		return transType;
	}
	
	public void setTransType(String transType) {
		this.transType = transType;
	}

}
