package com.ebeijia.zl.basics.wechat.enums;
/**
 * 用户操作菜单状态  00-无权限；10-获得权限 
 *
 */
public enum FansStatusEnum{
	
	Fans_STATUS_00("00"),
	Fans_STATUS_10("10");

	private String code;

	FansStatusEnum(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
}