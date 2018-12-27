package com.ebeijia.zl.common.utils.enums;

import com.ebeijia.zl.common.utils.constants.Constants;

/**
 * 电商平台门店订单中：订单状态
 * @author Administrator
 *
 */
public enum SubOrderStatusEnum {

	SOS00("00","未付款"),
	SOS10("10", "待发货"),
	SOS11("11", "已出库"),
	SOS12("12", "已发货"),
	SOS13("13", "已收货"),
	SOS14("14", "已完成"),
	SOS15("15", "作废"),
	SOS20("20", "申请换货"),
	SOS21("21", "已换货"),
	SOS22("22", "申请退货"),
	SOS23("23", "已退货"),
	SOS24("24", "换货被拒绝"),
	SOS25("25", "退货被拒绝"),
	SOS26("26", "申请取消"),
	SOS27("27", "已取消"),
	SOS28("28", "取消被拒"),
	SOS44("44", "外部渠道发货失败");
	private String code;
	private String value;

	SubOrderStatusEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static SubOrderStatusEnum findByCode(String code) {
		for (SubOrderStatusEnum t : SubOrderStatusEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}