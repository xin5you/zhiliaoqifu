package com.ebeijia.zl.common.utils.enums;

/**
 * 卡券交易状态
 * @author Administrator
 *
 */
public enum CouponTransStatEnum {

	CouponTransStatEnum_0("0", "未使用"),
	CouponTransStatEnum_1("1", "已使用"),
	CouponTransStatEnum_2("2", "已回收");

	private String code;
	private String name;

	CouponTransStatEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static CouponTransStatEnum findByBId(String code) {
		for (CouponTransStatEnum t : CouponTransStatEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}