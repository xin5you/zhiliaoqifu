package com.cn.thinkx.ecom.front.api.base.vo.goods;

import java.util.List;

public class APIItemTO {
	private String id;
	private String name;
	private String primarySkuId;
	private String primaryPicUrl;
	private String listPicUrl;
	private String simpleDesc;
	private List<APISkuTO> skuList;
	private APIItemDetailTO itemDetail;
	private List<APIItemAttributeTO> attrList; 
	private List<List<APICategoryPathTO>> categoryPathList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrimarySkuId() {
		return primarySkuId;
	}
	public void setPrimarySkuId(String primarySkuId) {
		this.primarySkuId = primarySkuId;
	}
	public String getPrimaryPicUrl() {
		return primaryPicUrl;
	}
	public void setPrimaryPicUrl(String primaryPicUrl) {
		this.primaryPicUrl = primaryPicUrl;
	}
	public String getListPicUrl() {
		return listPicUrl;
	}
	public void setListPicUrl(String listPicUrl) {
		this.listPicUrl = listPicUrl;
	}
	public String getSimpleDesc() {
		return simpleDesc;
	}
	public void setSimpleDesc(String simpleDesc) {
		this.simpleDesc = simpleDesc;
	}
	public List<APISkuTO> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<APISkuTO> skuList) {
		this.skuList = skuList;
	}
	public APIItemDetailTO getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(APIItemDetailTO itemDetail) {
		this.itemDetail = itemDetail;
	}
	public List<APIItemAttributeTO> getAttrList() {
		return attrList;
	}
	public void setAttrList(List<APIItemAttributeTO> attrList) {
		this.attrList = attrList;
	}
	public List<List<APICategoryPathTO>> getCategoryPathList() {
		return categoryPathList;
	}
	public void setCategoryPathList(List<List<APICategoryPathTO>> categoryPathList) {
		this.categoryPathList = categoryPathList;
	}
	
}
