package com.ebeijia.zl.common.utils.enums;

/**
 * 是否默认
 * @author Administrator
 *
 */
public enum IsDefaultEnum {

	IsDefaultEnum_0("0", "是"),
	IsDefaultEnum_1("1", "否");

	private String code;
	private String name;

	IsDefaultEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static IsDefaultEnum findByBId(String code) {
		for (IsDefaultEnum t : IsDefaultEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}