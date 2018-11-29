package com.cn.thinkx.ecom.eshopbanner.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class EshopBanner extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * BANNER信息表主键
	 */
	private String bannerId;
	/**
	 * 商城信息表主键
	 */
	private String eshopId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBannerId() {
		return bannerId;
	}

	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}

	public String getEshopId() {
		return eshopId;
	}

	public void setEshopId(String eshopId) {
		this.eshopId = eshopId;
	}

}
