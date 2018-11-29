package com.cn.thinkx.ecom.basics.order.domain;

import java.util.List;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 电商平台订单表
 * 
 * @author xiaomei
 *
 */
public class PlatfOrder extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;
	
	/*
	 * 主键ID
	 */
	private String orderId;
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
	private String payStatus;
	/*
	 * 订单金额
	 */
	private String orderPrice;
	/*
	 * 配送总费用
	 */
	private String orderFreightAmt;
	/*
	 * 支付方式类型
	 */
	private String payType;
	/*
	 * 支付总额
	 */
	private String payAmt;
	
	private String payTime;
	
	private List<PlatfShopOrder> platfShopOrder;	//门店订单信息
	private String mobilePhoneNo;	//会员手机号
	private String personalName;	//会员昵称
	
	private String sOrderId;	//商城订单号
	private String ecomCode;	//商城代码
	
	private int platfShopOrderGoodsCount;	//订单的sku总数量
	
	private String payStatusValue;	//付款状态值
	
	
	private String beginTime;	//开始时间
	private String endTime;	//结束时间
	
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


	public String getPayStatus() {
		return payStatus;
	}


	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}


	public String getOrderPrice() {
		return orderPrice;
	}


	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}


	public String getOrderFreightAmt() {
		return orderFreightAmt;
	}


	public void setOrderFreightAmt(String orderFreightAmt) {
		this.orderFreightAmt = orderFreightAmt;
	}


	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}


	public String getPayAmt() {
		return payAmt;
	}


	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}


	public String getPayTime() {
		return payTime;
	}


	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}


	public List<PlatfShopOrder> getPlatfShopOrder() {
		return platfShopOrder;
	}


	public void setPlatfShopOrder(List<PlatfShopOrder> platfShopOrder) {
		this.platfShopOrder = platfShopOrder;
	}


	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}


	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}


	public String getPersonalName() {
		return personalName;
	}


	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}


	public String getsOrderId() {
		return sOrderId;
	}


	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}


	public String getEcomCode() {
		return ecomCode;
	}


	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}


	public int getPlatfShopOrderGoodsCount() {
		return platfShopOrderGoodsCount;
	}


	public void setPlatfShopOrderGoodsCount(int platfShopOrderGoodsCount) {
		this.platfShopOrderGoodsCount = platfShopOrderGoodsCount;
	}

	public String getPayStatusValue() {
		return payStatusValue;
	}


	public void setPayStatusValue(String payStatusValue) {
		this.payStatusValue = payStatusValue;
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
		return "PlatfOrder [orderId=" + orderId + ", memberId=" + memberId + ", dmsRelatedKey=" + dmsRelatedKey
				+ ", payStatus=" + payStatus + ", orderPrice=" + orderPrice + ", orderFreightAmt=" + orderFreightAmt
				+ ", payType=" + payType + ", payAmt=" + payAmt + ", payTime=" + payTime + ", platfShopOrder="
				+ platfShopOrder + ", mobilePhoneNo=" + mobilePhoneNo + ", personalName=" + personalName + ", sOrderId="
				+ sOrderId + ", ecomCode=" + ecomCode + ", platfShopOrderGoodsCount=" + platfShopOrderGoodsCount + "]";
	}
	
}
