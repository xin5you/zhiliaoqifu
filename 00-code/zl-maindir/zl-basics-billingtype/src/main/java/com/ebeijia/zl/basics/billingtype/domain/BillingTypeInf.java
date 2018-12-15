package com.ebeijia.zl.basics.billingtype.domain;

import java.math.BigDecimal;

import com.ebeijia.zl.common.utils.domain.BaseEntity;
 
public class BillingTypeInf extends BaseEntity {
	private String bId;
	private String bName;
	private String code;
	private BigDecimal loseFee;
	private BigDecimal buyFee;
	public BigDecimal getLoseFee() {
		return loseFee;
	}
	public void setLoseFee(BigDecimal loseFee) {
		this.loseFee = loseFee;
	}
	public BigDecimal getBuyFee() {
		return buyFee;
	}
	public void setBuyFee(BigDecimal buyFee) {
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
