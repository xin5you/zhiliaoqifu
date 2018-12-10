package com.cn.thinkx.ecom.front.api.base.vo.goods;

import java.util.List;

public class APISkuTO {
	private String id;
	private String yanxuanPrice;
	private String channelPrice;
	private String picUrl;
	private String displayString;
	private List<APIItemSkuSpecValueTO> itemSkuSpecValueList;
	private String newFlag;
	private String classification;
	private APISkuDetailTO skuDetailTO;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYanxuanPrice() {
		return yanxuanPrice;
	}
	public void setYanxuanPrice(String yanxuanPrice) {
		this.yanxuanPrice = yanxuanPrice;
	}
	public String getChannelPrice() {
		return channelPrice;
	}
	public void setChannelPrice(String channelPrice) {
		this.channelPrice = channelPrice;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getDisplayString() {
		return displayString;
	}
	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
	public List<APIItemSkuSpecValueTO> getItemSkuSpecValueList() {
		return itemSkuSpecValueList;
	}
	public void setItemSkuSpecValueList(List<APIItemSkuSpecValueTO> itemSkuSpecValueList) {
		this.itemSkuSpecValueList = itemSkuSpecValueList;
	}
	public String getNewFlag() {
		return newFlag;
	}
	public void setNewFlag(String newFlag) {
		this.newFlag = newFlag;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public APISkuDetailTO getSkuDetailTO() {
		return skuDetailTO;
	}
	public void setSkuDetailTO(APISkuDetailTO skuDetailTO) {
		this.skuDetailTO = skuDetailTO;
	}
	
}
