package com.ebeijia.zl.basics.wechat.enums;

/**
 * 公账号分组 0,1,2系统默认 第一次关注状态为 0
 *
 */
public enum GroupsIdStatEnum{
	
	groupdefauls_stat("0");

	private String code;

	GroupsIdStatEnum(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
}