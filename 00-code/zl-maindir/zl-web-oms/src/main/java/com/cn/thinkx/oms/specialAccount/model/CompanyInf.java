package com.cn.thinkx.oms.specialAccount.model;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class CompanyInf extends BaseEntity {
	
	private String companyId;
	private String lawCode;
	private String transFlag;
	private String name;
	private String address;
	private String phoneNo;
	private String contacts;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getLawCode() {
		return lawCode;
	}
	public void setLawCode(String lawCode) {
		this.lawCode = lawCode;
	}
	public String getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
}
