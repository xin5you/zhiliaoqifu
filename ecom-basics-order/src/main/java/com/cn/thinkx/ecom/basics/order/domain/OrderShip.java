package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 电商订单收货地址
 * 
 * @author xiaomei
 *
 */
public class OrderShip extends BaseEntity{

	private static final long serialVersionUID = -7655982014086839784L;
	
	/*
	 * ID
	 */
	private String id;
	/*
	 * 订单ID
	 */
	private String orderId;
	/*
	 * 收货人姓名
	 */
	private String shipName;
	/*
	 * 收货地址
	 */
	private String shipAddr;
	/*
	 * 收货人邮编
	 */
	private String shipZipCode;
	/*
	 * 收货人邮箱
	 */
	private String shipEmail;
	/*
	 * 收货人手机
	 */
	private String shipMobile;
	/*
	 * 收货人电话
	 */
	private String shipTelephone;
	/*
	 * 收货地区-省份ID
	 */
	private String shipProvinceId;
	/*
	 * 收货地区-城市ID
	 */
	private String shipCityId;
	/*
	 * 收货地区-区(县)ID
	 */
	private String shipRegionId;
	
	/**业务扩展字段*/
	private String shipProvinceName; //省份名称
	
	private String shipCityName; //市区名称
	
	private String shipRegionName; //地区-区(县)
	
	private String personalName;	//会员昵称
	private String mobilePhoneNo;	//会员手机号
	private String memberId;	//会员id
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getShipName() {
		return shipName;
	}
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	public String getShipAddr() {
		return shipAddr;
	}
	public void setShipAddr(String shipAddr) {
		this.shipAddr = shipAddr;
	}
	public String getShipZipCode() {
		return shipZipCode;
	}
	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}
	public String getShipEmail() {
		return shipEmail;
	}
	public void setShipEmail(String shipEmail) {
		this.shipEmail = shipEmail;
	}
	public String getShipMobile() {
		return shipMobile;
	}
	public void setShipMobile(String shipMobile) {
		this.shipMobile = shipMobile;
	}
	public String getShipTelephone() {
		return shipTelephone;
	}
	public void setShipTelephone(String shipTelephone) {
		this.shipTelephone = shipTelephone;
	}
	public String getShipProvinceId() {
		return shipProvinceId;
	}
	public void setShipProvinceId(String shipProvinceId) {
		this.shipProvinceId = shipProvinceId;
	}
	public String getShipCityId() {
		return shipCityId;
	}
	public void setShipCityId(String shipCityId) {
		this.shipCityId = shipCityId;
	}
	public String getShipRegionId() {
		return shipRegionId;
	}
	public void setShipRegionId(String shipRegionId) {
		this.shipRegionId = shipRegionId;
	}

	public String getShipProvinceName() {
		return shipProvinceName;
	}
	public void setShipProvinceName(String shipProvinceName) {
		this.shipProvinceName = shipProvinceName;
	}
	public String getShipCityName() {
		return shipCityName;
	}
	public void setShipCityName(String shipCityName) {
		this.shipCityName = shipCityName;
	}
	public String getShipRegionName() {
		return shipRegionName;
	}
	public void setShipRegionName(String shipRegionName) {
		this.shipRegionName = shipRegionName;
	}
	
	public String getPersonalName() {
		return personalName;
	}
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}
	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}
	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	@Override
	public String toString() {
		return "OrderShip [id=" + id + ", orderId=" + orderId + ", shipName=" + shipName + ", shipAddr=" + shipAddr
				+ ", shipZipCode=" + shipZipCode + ", shipEmail=" + shipEmail + ", shipMobile=" + shipMobile
				+ ", shipTelephone=" + shipTelephone + ", shipProvinceId=" + shipProvinceId + ", shipCityId="
				+ shipCityId + ", shipRegionId=" + shipRegionId + ", shipProvinceName=" + shipProvinceName
				+ ", shipCityName=" + shipCityName + ", shipRegionName=" + shipRegionName + "]";
	}
}
