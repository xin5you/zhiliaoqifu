package com.cn.thinkx.ecom.front.api.base.vo.apply;

import java.util.List;

public class OrderRefundApplySkuQualityVO implements java.io.Serializable {

	private static final long serialVersionUID = 1835491561100211923L;
	
	private String skuId; // skuId String 最大32位
	
	private String defectiveType; // 次品类型
	
	private int treatmentMethod; // 处理方式
	
	private String responsibilityDesc; // 权责问题
	
	private List<String> picList; // 可选，不一定有该项 图片规格
	
	private String specDesc;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getDefectiveType() {
		return defectiveType;
	}

	public void setDefectiveType(String defectiveType) {
		this.defectiveType = defectiveType;
	}

	public int getTreatmentMethod() {
		return treatmentMethod;
	}

	public void setTreatmentMethod(int treatmentMethod) {
		this.treatmentMethod = treatmentMethod;
	}

	public String getResponsibilityDesc() {
		return responsibilityDesc;
	}

	public void setResponsibilityDesc(String responsibilityDesc) {
		this.responsibilityDesc = responsibilityDesc;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public String getSpecDesc() {
		return specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}
}
