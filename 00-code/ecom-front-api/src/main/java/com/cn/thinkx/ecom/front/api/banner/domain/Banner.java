package com.cn.thinkx.ecom.front.api.banner.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class Banner extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 图片地址
	 */
	private String imageUrl;
	/**
	 * 图片链接
	 */
	private String bannerUrl;
	/**
	 * 规格
	 */
	private String spec;
	
	//渠道号
	private String channel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
