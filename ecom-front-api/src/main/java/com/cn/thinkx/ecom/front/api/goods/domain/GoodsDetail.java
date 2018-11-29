package com.cn.thinkx.ecom.front.api.goods.domain;

import java.util.List;

import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemAttributeTO;

public class GoodsDetail {
	
	private List<String> imgStr;
	
	private String goodsDetail;
	
	private List<APIItemAttributeTO> attrList;

	public List<String> getImgStr() {
		return imgStr;
	}

	public void setImgStr(List<String> imgStr) {
		this.imgStr = imgStr;
	}

	public String getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	public List<APIItemAttributeTO> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<APIItemAttributeTO> attrList) {
		this.attrList = attrList;
	}
	
}
