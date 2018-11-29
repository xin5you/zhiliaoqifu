package com.cn.thinkx.ecom.basics.member.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class MemberAddress extends BaseEntity{

	private static final long serialVersionUID = -7655982014086839784L;
	
	private String addrId;
	/*
	 * 会员ID
	 */
	private String memberId;
	/*
	 * 收货人姓名
	 */
	private String userName;
	/*
	 * 所属省份ID
	 */
	private String provinceId;
	/*
	 * 所属城市ID
	 */
	private String cityId;
	/*
	 * 所属县(区)ID
	 */
	private String regionId;
	/*
	 * 所属城镇ID
	 */
	private String townId;
	/*
	 * 所属县(区)名称
	 */
	private String region;
	/*
	 * 所属城市名称
	 */
	private String city;
	/*
	 * 所属省份名称
	 */
	private String province;
	/*
	 * 所属城镇名称
	 */
	private String town;
	/*
	 * 详细地址
	 */
	private String addrDetail;
	/*
	 * 邮编
	 */
	private String addZip;
	/*
	 * 电话号
	 */
	private String tel;
	/*
	 * 手机号
	 */
	private String mobile;
	/*
	 * 是否为默认收货地址
	 */
	private String defAddr;
	/*
	 * 发货地址名称
	 */
	private String shipAddressName;
	
	public String getAddrId() {
		return addrId;
	}
	public String getMemberId() {
		return memberId;
	}
	public String getUserName() {
		return userName;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public String getCityId() {
		return cityId;
	}
	public String getRegionId() {
		return regionId;
	}
	public String getTownId() {
		return townId;
	}
	public String getRegion() {
		return region;
	}
	public String getCity() {
		return city;
	}
	public String getProvince() {
		return province;
	}
	public String getTown() {
		return town;
	}
	public String getAddrDetail() {
		return addrDetail;
	}
	public String getAddZip() {
		return addZip;
	}
	public String getTel() {
		return tel;
	}
	public String getMobile() {
		return mobile;
	}
	public String getDefAddr() {
		return defAddr;
	}
	public String getShipAddressName() {
		return shipAddressName;
	}
	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public void setTownId(String townId) {
		this.townId = townId;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}
	public void setAddZip(String addZip) {
		this.addZip = addZip;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setDefAddr(String defAddr) {
		this.defAddr = defAddr;
	}
	public void setShipAddressName(String shipAddressName) {
		this.shipAddressName = shipAddressName;
	}
}
