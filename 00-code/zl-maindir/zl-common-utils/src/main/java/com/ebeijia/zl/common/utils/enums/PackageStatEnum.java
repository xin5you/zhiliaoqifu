package com.ebeijia.zl.common.utils.enums;

/**
 * 包裹状态
 * @author Administrator
 *
 */
public enum PackageStatEnum {

	packageStat_00("00", "待发货"),
	packageStat_10("10", "已出库"),
	packageStat_20("20", "已签收"),
	packageStat_90("90", "已完成");

	private String code;
	private String name;

	PackageStatEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static PackageStatEnum findByBId(String code) {
		for (PackageStatEnum t : PackageStatEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}