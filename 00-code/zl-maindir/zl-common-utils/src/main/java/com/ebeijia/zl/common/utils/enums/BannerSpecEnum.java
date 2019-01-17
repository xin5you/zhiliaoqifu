package com.ebeijia.zl.common.utils.enums;

/**
 * Banner规格
 * @author Administrator
 *
 */
public enum BannerSpecEnum {

	BannerSpecEnum0("0", "默认"),
	BannerSpecEnum1("B01", "办公用品"),
	BannerSpecEnum2("B02", "差旅专用"),
	BannerSpecEnum3("B03", "体检专用"),
	BannerSpecEnum4("B04", "培训专用"),
	BannerSpecEnum5("B05", "食品专用"),
	BannerSpecEnum6("B06", "通讯专用"),
	BannerSpecEnum7("B07", "保险专用"),
	BannerSpecEnum8("B08", "交通专用");

	private String code;
	private String name;

	BannerSpecEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static BannerSpecEnum findByBId(String code) {
		for (BannerSpecEnum t : BannerSpecEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}