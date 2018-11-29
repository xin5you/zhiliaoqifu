package com.cn.thinkx.ecom.front.api.eshop.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class EshopInf extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;

	/**
	 * 商城主键
	 */
	private String id;
	/**
	 * 商户号
	 */
	private String mchntCode;
	/**
	 * 门店号
	 */
	private String shopCode;
	/**
	 * 商城名称
	 */
	private String eshopName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMchntCode() {
		return mchntCode;
	}

	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getEshopName() {
		return eshopName;
	}

	public void setEshopName(String eshopName) {
		this.eshopName = eshopName;
	}

}
