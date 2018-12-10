package com.cn.thinkx.ecom.front.api.base.vo.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * 渠道订单信息查询接口(yanxuan.order.paid.get)
 * @author zhuqiuyou
 *
 * 严选系统中订单信息
 */
public class OrderOutVO implements java.io.Serializable {

	private static final long serialVersionUID = 3870713749255833988L;

	private String orderId;  //是 	最大128位 
	
	private String submitTime; //下单时间
	
	private String payTime; //支付时间
	
	private String buyerAccount;  //买家用户名
	
	private String receiverName; //收件人姓名
	
	private String receiverMobile;//收件人手机
	
	private String receiverPhone;//收件人电话
	
	private String receiverProvinceName;	//收件人省名称
	
	private String receiverCityName;	//收件人市名称
	
	private String receiverDistrictName;	//收件人区名称
	
	private String receiverAddressDetail;	//收件人详细地址
	
	private BigDecimal realPrice;	//订单实付金额
	
	private BigDecimal expFee;	//邮费
	
	private String payMethod;	//支付方式
	
	private String orderStatus; //订单状态（PAYED：已付款、KF_CANCEL：客服取消 、CANCELLING：取消待审核 ）
	
	private List<OrderPackageVO> orderPackages;	//订单包裹

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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderPackageVO> getOrderPackages() {
		return orderPackages;
	}

	public void setOrderPackages(List<OrderPackageVO> orderPackages) {
		this.orderPackages = orderPackages;
	}
}
