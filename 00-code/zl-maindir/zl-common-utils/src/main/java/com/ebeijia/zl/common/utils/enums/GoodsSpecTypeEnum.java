package com.ebeijia.zl.common.utils.enums;

/**
 * 商品规格值类型
 * @author Administrator
 *
 */
public enum GoodsSpecTypeEnum {

	GoodsSpecTypeEnum_0("0", "文字"),
	GoodsSpecTypeEnum_1("1", "图片");

	private String code;
	private String value;

	GoodsSpecTypeEnum(String code, String value){
		this.code=code;
		this.value=value;
	}
	public String getCode() {
		return code;
	}
	public String getValue() {
		return value;
	}
	public static GoodsSpecTypeEnum findByBId(String code) {
		for (GoodsSpecTypeEnum t : GoodsSpecTypeEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}