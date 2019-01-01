package com.ebeijia.zl.common.utils.enums;

/**
 * 是否禁用
 * @author Administrator
 *
 */
public enum IsDisabledEnum {

	IsDisabledEnum_0("0", "未禁用"),
	IsDisabledEnum_1("1", "已禁用");

	private String code;
	private String name;

	IsDisabledEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static IsDisabledEnum findByBId(String code) {
		for (IsDisabledEnum t : IsDisabledEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}