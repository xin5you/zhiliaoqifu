package com.ebeijia.zl.common.utils.enums;

import com.ebeijia.zl.common.utils.constants.Constants;

/**
 * 商城 接入渠道
 *
 * @author xiaomei
 *
 */
public enum GoodsEcomCodeTypeEnum {

	ECOM00("66661000", "知了企服商城"),
	ECOM01("66661001", "网易严选"),
	ECOM02("66661002", "苏宁易购"),
	ECOM03("66661003", "京东商城");

	private String code;
	private String value;

	GoodsEcomCodeTypeEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static GoodsEcomCodeTypeEnum findByCode(String code) {
		for (GoodsEcomCodeTypeEnum t : GoodsEcomCodeTypeEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}