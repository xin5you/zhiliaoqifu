package com.cn.thinkx.ecom.eshoproutes.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class EshopRoutes extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 电商入口表主键
	 */
	private String routesId;
	/**
	 * 商城表主键
	 */
	private String eshopId;

	// 主页信息
	private String bannerId;
	private String imageUrl;
	private String bannerUrl;
	// 电商入口信息
	private String ecomName;
	private String ecomUrl;
	private String ecomLogo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoutesId() {
		return routesId;
	}

	public void setRoutesId(String routesId) {
		this.routesId = routesId;
	}

	public String getEshopId() {
		return eshopId;
	}

	public void setEshopId(String eshopId) {
		this.eshopId = eshopId;
	}

	public String getBannerId() {
		return bannerId;
	}

	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getEcomName() {
		return ecomName;
	}

	public void setEcomName(String ecomName) {
		this.ecomName = ecomName;
	}

	public String getEcomUrl() {
		return ecomUrl;
	}

	public void setEcomUrl(String ecomUrl) {
		this.ecomUrl = ecomUrl;
	}

	public String getEcomLogo() {
		return ecomLogo;
	}

	public void setEcomLogo(String ecomLogo) {
		this.ecomLogo = ecomLogo;
	}

}
