package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 退货地址信息
 * 
 * @author kpplg
 */
public class EcomRefundAddress extends BaseEntity{
	
	private static final long serialVersionUID = -1332193862052141701L;

	private String retAddrId;	
	private String returnsId;	//退货申请ID
	private String provinceName;	//	 		省份名称 
	private String cityName;	// 	 	城市名称
	private String	districtName;	// 	 区域名称	
	private String	address;	// 	 	具体街道地址
	private String fullAddress;	//完整地址
	private String	zipCode;	//邮政编码
	private String name;	//收件人姓名 	
	private String mobile;	//收件人手机
	private String telephone;	//收件人座机电话
	private String returnsDesc;	//退货描述
	
	public String getRetAddrId() {
		return retAddrId;
	}
	public String getReturnsId() {
		return returnsId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public String getAddress() {
		return address;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public String getName() {
		return name;
	}
	public String getMobile() {
		return mobile;
	}
	public String getTelephone() {
		return telephone;
	}
	public String getReturnsDesc() {
		return returnsDesc;
	}
	public void setRetAddrId(String retAddrId) {
		this.retAddrId = retAddrId;
	}
	public void setReturnsId(String returnsId) {
		this.returnsId = returnsId;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public void setReturnsDesc(String returnsDesc) {
		this.returnsDesc = returnsDesc;
	}
	@Override
	public String toString() {
		return "RefundAddress [retAddrId=" + retAddrId + ", returnsId=" + returnsId + ", provinceName=" + provinceName
				+ ", cityName=" + cityName + ", districtName=" + districtName + ", address=" + address
				+ ", fullAddress=" + fullAddress + ", zipCode=" + zipCode + ", name=" + name + ", mobile=" + mobile
				+ ", telephone=" + telephone + ", returnsDesc=" + returnsDesc + "]";
	}

	
}
