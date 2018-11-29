package com.cn.thinkx.ecom.basics.member.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class UserInf extends BaseEntity {
	
	private String userId; //用户信息_id
	private String userName; //用户名  微信渠道 openid
	private String userType; //用户类型
	private transient String userPasswd; //密码
	private String productCode; //产品号
	
	//个人信息Id
	private String personalId;
	/**
	 * 外部渠道ID
	 */
	private String externalId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserPasswd() {
		return userPasswd;
	}
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}
}