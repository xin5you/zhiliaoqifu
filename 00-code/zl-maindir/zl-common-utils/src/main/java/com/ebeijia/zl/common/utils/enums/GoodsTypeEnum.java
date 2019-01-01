package com.ebeijia.zl.common.utils.enums;

/**
 * 商品类型
 * @author Administrator
 *
 */
public enum GoodsTypeEnum {

	GoodsTypeEnum_0("0", "实物类"),
	GoodsTypeEnum_1("1", "服务类");

	private String code;
	private String name;

	GoodsTypeEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static GoodsTypeEnum findByBId(String code) {
		for (GoodsTypeEnum t : GoodsTypeEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}