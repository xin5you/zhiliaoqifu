package com.ebeijia.zl.common.utils.enums;

/**
 * 签收状态
 * @author Administrator
 *
 */
public enum IsSignEnum {

	packageStat_0("0", "未签收"),
	packageStat_1("1", "已签收");

	private String code;
	private String name;

	IsSignEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static IsSignEnum findByBId(String code) {
		for (IsSignEnum t : IsSignEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}