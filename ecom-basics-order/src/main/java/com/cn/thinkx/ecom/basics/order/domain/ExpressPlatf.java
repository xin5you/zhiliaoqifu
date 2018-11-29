package com.cn.thinkx.ecom.basics.order.domain;

import java.util.List;

import com.cn.thinkx.ecom.common.domain.BaseEntity;
/*
 * 订单包裹物流信息
 */
public class ExpressPlatf extends BaseEntity{
	
	private static final long serialVersionUID = -7655982014086839784L;
	/*
	 * package_id
	 */
	private String packId;
	/*
	 * 门店订单号
	 */
	private String sOrderId;
	/*
	 * 包裹号
	 */
	private String packageNo;
	/*
	 * 发货时间
	 */
	private String deliveryTime;
	/*
	 * 包裹状态
	 */
	private String packageStat;
	/*
	 * 包裹状态描述
	 */
	private String packageStatDesc;
	/*
	 * 物流公司ID
	 */
	private String expressCompanyId;
	/*
	 * 物流公司
	 */
	private String expressCompanyName;
	/*
	 * 物流单号
	 */
	private String expressNo;
	/*
	 * 是否签收
	 */
	private String isSign;
	/*
	 * 签收时间
	 */
	private String signTime;
	
	/**
	 * 商城标识
	 */
	private String ecomCode;
	
	/*
	 * 拓展字段
	 */
	private String orderId;
	private String subOrderStatus;
	private String personalName;
	private String mobilePhoneNo;
	private String memberId;	//会员id
	
	private String goodsName;	//商品名称
	private String skuCode;	
	private String saleCount;	//销售量
	private String productId;	//货品id	
	private String goodsId;	//商品id
	private String goodsPrice;	//货品价格
	private String picUrl;	//货品图片
	
	private String oItemId;	//货品明细id
	
	private List<OrderProductItem> orderProductItem;	//货品信息
	
	private OrderDeliveryInfo orderDeliveryInfo;	//物流轨迹
	
	
	public String getPackId() {
		return packId;
	}


	public String getsOrderId() {
		return sOrderId;
	}


	public String getPackageNo() {
		return packageNo;
	}


	public String getDeliveryTime() {
		return deliveryTime;
	}


	public String getPackageStat() {
		return packageStat;
	}


	public String getPackageStatDesc() {
		return packageStatDesc;
	}


	public String getExpressCompanyId() {
		return expressCompanyId;
	}


	public String getExpressCompanyName() {
		return expressCompanyName;
	}


	public String getExpressNo() {
		return expressNo;
	}


	public String getIsSign() {
		return isSign;
	}


	public String getSignTime() {
		return signTime;
	}


	public String getEcomCode() {
		return ecomCode;
	}


	public String getOrderId() {
		return orderId;
	}


	public String getSubOrderStatus() {
		return subOrderStatus;
	}


	public String getPersonalName() {
		return personalName;
	}


	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}


	public String getMemberId() {
		return memberId;
	}


	public String getGoodsName() {
		return goodsName;
	}


	public String getSkuCode() {
		return skuCode;
	}


	public String getSaleCount() {
		return saleCount;
	}


	public String getProductId() {
		return productId;
	}


	public String getGoodsId() {
		return goodsId;
	}


	public String getGoodsPrice() {
		return goodsPrice;
	}


	public String getPicUrl() {
		return picUrl;
	}


	public String getoItemId() {
		return oItemId;
	}


	public List<OrderProductItem> getOrderProductItem() {
		return orderProductItem;
	}


	public OrderDeliveryInfo getOrderDeliveryInfo() {
		return orderDeliveryInfo;
	}


	public void setPackId(String packId) {
		this.packId = packId;
	}


	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}


	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}


	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}


	public void setPackageStat(String packageStat) {
		this.packageStat = packageStat;
	}


	public void setPackageStatDesc(String packageStatDesc) {
		this.packageStatDesc = packageStatDesc;
	}


	public void setExpressCompanyId(String expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}


	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}


	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}


	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}


	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}


	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public void setSubOrderStatus(String subOrderStatus) {
		this.subOrderStatus = subOrderStatus;
	}


	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}


	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}


	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}


	public void setSaleCount(String saleCount) {
		this.saleCount = saleCount;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}


	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public void setoItemId(String oItemId) {
		this.oItemId = oItemId;
	}


	public void setOrderProductItem(List<OrderProductItem> orderProductItem) {
		this.orderProductItem = orderProductItem;
	}


	public void setOrderDeliveryInfo(OrderDeliveryInfo orderDeliveryInfo) {
		this.orderDeliveryInfo = orderDeliveryInfo;
	}


	@Override
	public String toString() {
		return "ExpressPlatf [packId=" + packId + ", sOrderId=" + sOrderId + ", packageNo=" + packageNo
				+ ", deliveryTime=" + deliveryTime + ", packageStat=" + packageStat + ", packageStatDesc="
				+ packageStatDesc + ", expressCompanyId=" + expressCompanyId + ", expressCompanyName="
				+ expressCompanyName + ", expressNo=" + expressNo + ", isSign=" + isSign + ", signTime=" + signTime
				+ ", ecomCode=" + ecomCode + "]";
	}
}
