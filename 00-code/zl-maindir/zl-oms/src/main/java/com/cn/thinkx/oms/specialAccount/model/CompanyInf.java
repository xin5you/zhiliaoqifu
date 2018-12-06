package com.cn.thinkx.oms.specialAccount.model;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class CompanyInf extends BaseEntity {
	private String cId;
	private String name;
	private String comCode;
	private String lawCode;
	private String flag;
	private String type;
	private String address;
	private String phoneNO;
	private String contacts;
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getLawCode() {
		return lawCode;
	}
	public void setLawCode(String lawCode) {
		this.lawCode = lawCode;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNO() {
		return phoneNO;
	}
	public void setPhoneNO(String phoneNO) {
		this.phoneNO = phoneNO;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	
}
