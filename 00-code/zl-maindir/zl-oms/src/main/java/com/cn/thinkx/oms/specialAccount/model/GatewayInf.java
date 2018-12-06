package com.cn.thinkx.oms.specialAccount.model;

import java.util.List;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class GatewayInf extends BaseEntity {
	private String gId;
	private String name;
	private String code;
	private String desc;
	private List<BillingTypeInf> bList;
	
	
	public String getgId() {
		return gId;
	}
	public void setgId(String gId) {
		this.gId = gId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<BillingTypeInf> getbList() {
		return bList;
	}
	public void setbList(List<BillingTypeInf> bList) {
		this.bList = bList;
	}
	
	
	
	
}
