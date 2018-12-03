package com.ebeijia.zl.common.utils.enums;
public enum AccountCardAttrEnum {

	OPER("开户", "OPEN"),
	/**
	 * 加款
	 */
	ADD("加款", "ADD"),

	/**
	 * 减款
	 */
	SUB("减款", "SUB"),
	/**
	 * 冻结
	 */
	FROZEN("冻结", "FROZEN"),

	/**
	 * 解冻
	 */
	UNFROZEN("解冻", "UNFROZEN");



	/** 枚举值 */
	private String value;

	/** 描述 */
	private String desc;

	private AccountCardAttrEnum(String desc, String value) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
