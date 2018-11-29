package com.cn.thinkx.ecom.system.domain;

import java.util.Date;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class User extends BaseEntity {

	private static final long serialVersionUID = -4688626183004316392L;
	/**
	 * 主键ID
	 */
	private Integer id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 年龄
	 */
	private String age;
	/**
	 * 角色ID
	 */
	private String roleId;
	/**
	 * 用户类型
	 */
	private String userType;
	/**
	 * 是否默认
	 */
	private String isdefault;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 组织机构ID
	 */
	private String organizationId;
	/**
	 * 组织机构名称
	 */
	private String organizationName;
	/**
	 * 创建时间
	 */
	private Date createDateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

}