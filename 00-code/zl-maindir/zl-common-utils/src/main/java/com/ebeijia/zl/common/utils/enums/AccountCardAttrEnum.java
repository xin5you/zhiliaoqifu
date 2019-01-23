package com.ebeijia.zl.common.utils.enums;
public enum AccountCardAttrEnum {

	OPER("开户", "0"),

	/**
	 * 加款
	 */
	ADD("加款", "1"),

	/**
	 * 减款
	 */
	SUB("减款", "2"),

	/**
	 * 冻结
	 */
	FROZEN("冻结", "3"),

	/**
	 * 解冻撤销
	 */
	UNFROZEN("解冻撤销", "4"),

	/**
	 * 解冻提交
	 */
	COMMINFROZEN("解冻提交", "5");


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

	public static AccountCardAttrEnum findByValue(String value) {
		for (AccountCardAttrEnum t : AccountCardAttrEnum.values()) {
			if (t.value.equalsIgnoreCase(value)) {
				return t;
			}
		}
		return null;
	}

}
