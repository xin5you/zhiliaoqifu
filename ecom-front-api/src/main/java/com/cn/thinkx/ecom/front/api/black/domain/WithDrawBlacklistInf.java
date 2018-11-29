package com.cn.thinkx.ecom.front.api.black.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class WithDrawBlacklistInf extends BaseEntity {
	
	private static final long serialVersionUID = -3451747498001400235L;
	
	private String id;
	private String userId;
	private String userName;
	private String userPhone;
	private String openId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
