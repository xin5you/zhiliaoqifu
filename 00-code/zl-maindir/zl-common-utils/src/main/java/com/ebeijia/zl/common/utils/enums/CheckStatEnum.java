package com.ebeijia.zl.common.utils.enums;

/**
 * 审核状态
 * @author Administrator
 *
 */
public enum CheckStatEnum {

	CHECK_TRUE("1", "已审核"),
	CHECK_FALSE("0", "未审核");

	private String code;
	private String name;

	CheckStatEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static CheckStatEnum findByBId(String code) {
		for (CheckStatEnum t : CheckStatEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}