package com.cn.thinkx.diy.api.system.domain;

import com.cn.thinkx.diy.api.base.domain.BaseEntity;

public class UserRole extends BaseEntity implements java.io.Serializable {

	private String userId;
	private String roleId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}
