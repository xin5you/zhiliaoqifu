package com.cn.thinkx.oms.specialAccount.model;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class BillingTypeInf extends BaseEntity {
	private String bId;
	private String bName;
	private String code;
	private Double loseFee;
	private Double buyFee;
	public Double getLoseFee() {
		return loseFee;
	}
	public void setLoseFee(Double loseFee) {
		this.loseFee = loseFee;
	}
	public Double getBuyFee() {
		return buyFee;
	}
	public void setBuyFee(Double buyFee) {
		this.buyFee = buyFee;
	}
	public String getbId() {
		return bId;
	}
	public void setbId(String bId) {
		this.bId = bId;
	}
	public String getbName() {
		return bName;
	}
	public void setbName(String bName) {
		this.bName = bName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
