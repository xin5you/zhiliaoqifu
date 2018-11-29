package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class SettingBanner extends BaseEntity{

	private static final long serialVersionUID = -4262982014086839784L;
	
	private String id;
	private String settingId;	//商城服务_ID
	private String bannerId;	//BANNER_ID
	
	/**
	 * 拓展字段
	 */
	private String imageUrl;	//图片地址
	private String bannerUrl;	//图文链接
	private String spec;	//规格
	private String bannerRemarks;	//banner备注说明
	private String ecomCode;	//电商代码
	public String getId() {
		return id;
	}
	public String getSettingId() {
		return settingId;
	}
	public String getBannerId() {
		return bannerId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public String getSpec() {
		return spec;
	}
	public String getBannerRemarks() {
		return bannerRemarks;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}
	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public void setBannerRemarks(String bannerRemarks) {
		this.bannerRemarks = bannerRemarks;
	}
	public String getEcomCode() {
		return ecomCode;
	}
	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}
	@Override
	public String toString() {
		return "SettingBanner [id=" + id + ", settingId=" + settingId + ", bannerId=" + bannerId + ", imageUrl="
				+ imageUrl + ", bannerUrl=" + bannerUrl + ", spec=" + spec + ", bannerRemarks=" + bannerRemarks + "]";
	}
	
}
