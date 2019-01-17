package com.ebeijia.zl.common.utils.enums;

/**
 * Banner位置
 * @author Administrator
 *
 */
public enum BannerPositionEnum {

	BannerPositionEnum1("1", "商品列表"),
	BannerPositionEnum2("2", "卡券集市");

	private String code;
	private String name;

	BannerPositionEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static BannerPositionEnum findByBId(String code) {
		for (BannerPositionEnum t : BannerPositionEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}