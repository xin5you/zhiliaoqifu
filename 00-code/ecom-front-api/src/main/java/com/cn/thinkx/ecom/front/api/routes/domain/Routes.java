package com.cn.thinkx.ecom.front.api.routes.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class Routes extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 电商名称
	 */
	private String ecomName;
	/**
	 * 电商代码
	 */
	private String ecomCode;
	/**
	 * 入口链接
	 */
	private String ecomUrl;
	/**
	 * 电商类别
	 */
	private String ecomType;
	/**
	 * 电商logo
	 */
	private String ecomLogo;
	/**
	 * 订单链接
	 */
	private String orderUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEcomName() {
		return ecomName;
	}

	public void setEcomName(String ecomName) {
		this.ecomName = ecomName;
	}

	public String getEcomCode() {
		return ecomCode;
	}

	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}

	public String getEcomUrl() {
		return ecomUrl;
	}

	public void setEcomUrl(String ecomUrl) {
		this.ecomUrl = ecomUrl;
	}

	public String getEcomType() {
		return ecomType;
	}

	public void setEcomType(String ecomType) {
		this.ecomType = ecomType;
	}

	public String getEcomLogo() {
		return ecomLogo;
	}

	public void setEcomLogo(String ecomLogo) {
		this.ecomLogo = ecomLogo;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}

}
