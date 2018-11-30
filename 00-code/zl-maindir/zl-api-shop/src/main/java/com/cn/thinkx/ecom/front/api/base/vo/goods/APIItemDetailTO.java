package com.cn.thinkx.ecom.front.api.base.vo.goods;

import java.util.List;

public class APIItemDetailTO {
	private String detailHtml;
	private String picUrl1;
	private String picUrl2;
	private String picUrl3;
	private String picUrl4;
	private String originCountryName;
	private List<APIItemTagTO> manuTagList;
	private APIItemDeliveryAreaTO deliveryAreaList;
	public String getDetailHtml() {
		return detailHtml;
	}
	public void setDetailHtml(String detailHtml) {
		this.detailHtml = detailHtml;
	}
	public String getPicUrl1() {
		return picUrl1;
	}
	public void setPicUrl1(String picUrl1) {
		this.picUrl1 = picUrl1;
	}
	public String getPicUrl2() {
		return picUrl2;
	}
	public void setPicUrl2(String picUrl2) {
		this.picUrl2 = picUrl2;
	}
	public String getPicUrl3() {
		return picUrl3;
	}
	public void setPicUrl3(String picUrl3) {
		this.picUrl3 = picUrl3;
	}
	public String getPicUrl4() {
		return picUrl4;
	}
	public void setPicUrl4(String picUrl4) {
		this.picUrl4 = picUrl4;
	}
	public String getOriginCountryName() {
		return originCountryName;
	}
	public void setOriginCountryName(String originCountryName) {
		this.originCountryName = originCountryName;
	}
	public List<APIItemTagTO> getManuTagList() {
		return manuTagList;
	}
	public void setManuTagList(List<APIItemTagTO> manuTagList) {
		this.manuTagList = manuTagList;
	}
	public APIItemDeliveryAreaTO getDeliveryAreaList() {
		return deliveryAreaList;
	}
	public void setDeliveryAreaList(APIItemDeliveryAreaTO deliveryAreaList) {
		this.deliveryAreaList = deliveryAreaList;
	}
	
	
}
