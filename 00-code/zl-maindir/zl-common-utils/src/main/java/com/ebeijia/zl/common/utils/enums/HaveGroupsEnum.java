package com.ebeijia.zl.common.utils.enums;

/**
 * 是否是组合商品
 * @author Administrator
 *
 */
public enum HaveGroupsEnum {

	HaveGroupsEnum_0("0", "单个商品"),
	HaveGroupsEnum_1("1", "组合商品");

	private String code;
	private String name;

	HaveGroupsEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static HaveGroupsEnum findByBId(String code) {
		for (HaveGroupsEnum t : HaveGroupsEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}