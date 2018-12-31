package com.ebeijia.zl.common.utils.enums;

/**
 * 商品上下架状态
 * @author Administrator
 *
 */
public enum MarketEnableEnum {

	MarketEnableEnum_0("0", "已下架"),
	MarketEnableEnum_1("1", "已上架");

	private String code;
	private String name;

	MarketEnableEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static MarketEnableEnum findByBId(String code) {
		for (MarketEnableEnum t : MarketEnableEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}