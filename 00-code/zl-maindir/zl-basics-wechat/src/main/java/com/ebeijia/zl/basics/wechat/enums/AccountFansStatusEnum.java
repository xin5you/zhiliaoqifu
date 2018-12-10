package com.ebeijia.zl.basics.wechat.enums;

/**
 * 
* 
* @Description: 粉丝状态
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月8日 下午8:22:37 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月8日     zhuqi           v1.0.0
 */
public enum AccountFansStatusEnum{
	TRUE_STATUS(1),
	FALSE_STATUS(0);

	private Integer code;

	AccountFansStatusEnum(Integer code){
		this.code=code;
	}
	public Integer getCode() {
		return code;
	}
}