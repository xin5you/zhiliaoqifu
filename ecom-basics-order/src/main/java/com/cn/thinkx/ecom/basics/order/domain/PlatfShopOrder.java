package com.cn.thinkx.ecom.basics.order.domain;

import java.util.List;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 电商平台订单
 * 
 * @author xiaomei
 *
 */
public class PlatfShopOrder extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;
	
	/*
	 * 门店订单ID
	 */
	private String sOrderId;
	/*
	 * 订单ID
	 */
	private String orderId;  //一级订单Id
	/*
	 * 会员ID
	 */
	private String memberId;
	
	/*
	 * 外部流水
	 */
	private String dmsRelatedKey;
	/*
	 * 付款状态
	 */
	private String subOrderStatus;
	/*
	 * 订单备注
	 */
	private String orderRemark;
	/*
	 * 配送费用
	 */
	private String shippingFreightPrice;
	/*
	 * 订单支付金额
	 */
	private String payAmt;
	/*
	 * 商城门店
	 */
	private String ecomCode;
	/*
	 * 渠道订单金额
	 */
	private String chnlOrderPrice;
	/*
	 * 渠道订单邮费
	 */
	private String chnlOrderPostage;
	
	private List<OrderProductItem> orderProductItems;	//订单货品明细
	
	private int orderProductItemsCount;	//订单货品明细数量
	
	private String orderProductPrice;	//订单货品总价
	/** 拓展字段*/
	
	private String payStatus;	//一级订单支付状态
	private String payTime;	//支付时间
	private String mobilePhoneNo;	//会员手机号
	private String personalName;	//会员昵称
	
	private String subOrderStatusValue;	//付款状态值
	
	private String beginTime;	//开始时间
	private String endTime;	//结束时间
	
	public String getsOrderId() {
		return sOrderId;
	}


	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public String getDmsRelatedKey() {
		return dmsRelatedKey;
	}


	public void setDmsRelatedKey(String dmsRelatedKey) {
		this.dmsRelatedKey = dmsRelatedKey;
	}


	public String getSubOrderStatus() {
		return subOrderStatus;
	}


	public void setSubOrderStatus(String subOrderStatus) {
		this.subOrderStatus = subOrderStatus;
	}


	public String getOrderRemark() {
		return orderRemark;
	}


	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}


	public String getShippingFreightPrice() {
		return shippingFreightPrice;
	}


	public void setShippingFreightPrice(String shippingFreightPrice) {
		this.shippingFreightPrice = shippingFreightPrice;
	}


	public String getPayAmt() {
		return payAmt;
	}


	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}


	public String getEcomCode() {
		return ecomCode;
	}


	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}


	public String getChnlOrderPrice() {
		return chnlOrderPrice;
	}


	public void setChnlOrderPrice(String chnlOrderPrice) {
		this.chnlOrderPrice = chnlOrderPrice;
	}


	public String getChnlOrderPostage() {
		return chnlOrderPostage;
	}


	public void setChnlOrderPostage(String chnlOrderPostage) {
		this.chnlOrderPostage = chnlOrderPostage;
	}


	public List<OrderProductItem> getOrderProductItems() {
		return orderProductItems;
	}


	public void setOrderProductItems(List<OrderProductItem> orderProductItems) {
		this.orderProductItems = orderProductItems;
	}
	
	public String getPayStatus() {
		return payStatus;
	}


	public String getPayTime() {
		return payTime;
	}


	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}


	public String getPersonalName() {
		return personalName;
	}


	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}


	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}


	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}


	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}


	public int getOrderProductItemsCount() {
		return orderProductItemsCount;
	}


	public void setOrderProductItemsCount(int orderProductItemsCount) {
		this.orderProductItemsCount = orderProductItemsCount;
	}


	public String getSubOrderStatusValue() {
		return subOrderStatusValue;
	}


	public void setSubOrderStatusValue(String subOrderStatusValue) {
		this.subOrderStatusValue = subOrderStatusValue;
	}

	public String getOrderProductPrice() {
		return orderProductPrice;
	}


	public void setOrderProductPrice(String orderProductPrice) {
		this.orderProductPrice = orderProductPrice;
	}


	public String getBeginTime() {
		return beginTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	@Override
	public String toString() {
		return "PlatfShopOrder [sOrderId=" + sOrderId + ", orderId=" + orderId + ", memberId=" + memberId
				+ ", dmsRelatedKey=" + dmsRelatedKey + ", subOrderStatus=" + subOrderStatus + ", orderRemark="
				+ orderRemark + ", shippingFreightPrice=" + shippingFreightPrice + ", payAmt=" + payAmt + ", ecomCode="
				+ ecomCode + ", chnlOrderPrice=" + chnlOrderPrice + ", chnlOrderPostage=" + chnlOrderPostage
				+ ", orderProductItems=" + orderProductItems + ", orderProductItemsCount=" + orderProductItemsCount
				+ ", payStatus=" + payStatus + ", payTime=" + payTime + ", mobilePhoneNo=" + mobilePhoneNo
				+ ", personalName=" + personalName + "]";
	}
	
}
