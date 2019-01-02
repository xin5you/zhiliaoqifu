package com.ebeijia.zl.common.utils.enums;

/**
 * 商品单位
 * @author Administrator
 *
 */
public enum GoodsUnitEnum {

	GoodsUnitEnum_1("1", "g"),
	GoodsUnitEnum_2("2", "kg");

	private String code;
	private String name;

	GoodsUnitEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static GoodsUnitEnum findByBId(String code) {
		for (GoodsUnitEnum t : GoodsUnitEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}