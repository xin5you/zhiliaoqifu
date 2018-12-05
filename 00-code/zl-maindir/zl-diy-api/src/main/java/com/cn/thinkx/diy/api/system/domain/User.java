package com.cn.thinkx.diy.api.system.domain;

import com.cn.thinkx.diy.api.base.domain.BaseEntity;

public class User extends BaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 6165960653428358106L;
	
	private String id;
	private String loginName; // 登录名
	private String password; // 密码
	private String userName; // 姓名
	private String phoneNo;
	private String loginType; // 登录类型
	private String isdefault; // 是否默认
	private String organizationId;
	
	private String roleId;
	private String roleName;
	private String organizationName;
	private String supplierId;
	private String supplierName;
	private String roleCheckflag; // 分销商角色标识 0:是 1：否
	
	private String sessionId;
	
	private Integer countNumber;
	
	private String seq;
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getCountNumber() {
		return countNumber;
	}
	public void setCountNumber(Integer countNumber) {
		this.countNumber = countNumber;
	}
	public String getRoleCheckflag() {
		return roleCheckflag;
	}
	public void setRoleCheckflag(String roleCheckflag) {
		this.roleCheckflag = roleCheckflag;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
}