package com.ebeijia.zl.common.utils.enums;

import com.ebeijia.zl.common.utils.constants.Constants;

/**
 * 一级门店支付状态
 *
 * @author xiaomei
 *
 */
public enum PlatfOrderPayStatEnum {

	PayStat00("0","未付款"),
	PayStat01("1","已付款待确认"),
	PayStat02("2","已付款"),
	PayStat03("3","已退款"),
	PayStat04("4","部分退款"),
	PayStat05("5","部分付款"),
	PayStat08("8","已取消"),
	PayStat09("9","已完成");

	private String code;
	private String value;

	PlatfOrderPayStatEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static PlatfOrderPayStatEnum findByCode(String code) {
		for (PlatfOrderPayStatEnum t : PlatfOrderPayStatEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}