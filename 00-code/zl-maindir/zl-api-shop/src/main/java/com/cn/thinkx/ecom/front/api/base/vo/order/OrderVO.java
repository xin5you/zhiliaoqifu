package com.cn.thinkx.ecom.front.api.base.vo.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderVO {

	/***
	 * 订单号，最大128位
	 */
	private String orderId;

	/***
	 * 下单时间，yyyy-MM-dd HH:mm:ss
	 */
	private String submitTime;

	/***
	 * 支付时间，yyyy-MM-dd HH:mm:ss
	 */
	private String payTime;

	/***
	 * 买家用户名，可省略
	 */
	private String buyerAccount;

	/***
	 * 收件人姓名
	 */
	private String receiverName;

	/***
	 * 收件人手机，可省略
	 */
	private String receiverMobile;

	/***
	 * 收件人电话
	 */
	private String receiverPhone;

	/***
	 * 收件人省名称
	 */
	private String receiverProvinceName;

	/***
	 * 收件人市名称
	 */
	private String receiverCityName;

	/***
	 * 收件人区名称
	 */
	private String receiverDistrictName;

	/***
	 * 收件人详细地址
	 */
	private String receiverAddressDetail;

	/***
	 * 订单实付金额
	 */
	private BigDecimal realPrice;

	/***
	 * 邮费
	 */
	private BigDecimal expFee;

	/***
	 * 支付方式
	 */
	private String payMethod;

	/***
	 * 订单SKU
	 */
	private List<OrderSkuVO> orderSkus;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getBuyerAccount() {
		return buyerAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverProvinceName() {
		return receiverProvinceName;
	}

	public void setReceiverProvinceName(String receiverProvinceName) {
		this.receiverProvinceName = receiverProvinceName;
	}

	public String getReceiverCityName() {
		return receiverCityName;
	}

	public void setReceiverCityName(String receiverCityName) {
		this.receiverCityName = receiverCityName;
	}

	public String getReceiverDistrictName() {
		return receiverDistrictName;
	}

	public void setReceiverDistrictName(String receiverDistrictName) {
		this.receiverDistrictName = receiverDistrictName;
	}

	public String getReceiverAddressDetail() {
		return receiverAddressDetail;
	}

	public void setReceiverAddressDetail(String receiverAddressDetail) {
		this.receiverAddressDetail = receiverAddressDetail;
	}

	public BigDecimal getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(BigDecimal realPrice) {
		this.realPrice = realPrice;
	}

	public BigDecimal getExpFee() {
		return expFee;
	}

	public void setExpFee(BigDecimal expFee) {
		this.expFee = expFee;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public List<OrderSkuVO> getOrderSkus() {
		return orderSkus;
	}

	public void setOrderSkus(List<OrderSkuVO> orderSkus) {
		this.orderSkus = orderSkus;
	}

}
