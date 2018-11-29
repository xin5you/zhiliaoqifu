package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class GoodsCategory extends BaseEntity {
	
	private static final long serialVersionUID = -470396968386056299L;
	
	private String catId;
	private String catName;
	private String parentId;
	private String catPath;
	private Long catSolr;
	private String listShow;
	private String catImage;
	private String outerCatId;
	private String ecomCode;
	private String ecomType;
	
	private String listShowType;//前段展示状态
	private String parentName;
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCatPath() {
		return catPath;
	}
	public void setCatPath(String catPath) {
		this.catPath = catPath;
	}
	public Long getCatSolr() {
		return catSolr;
	}
	public void setCatSolr(Long catSolr) {
		this.catSolr = catSolr;
	}
	public String getListShow() {
		return listShow;
	}
	public void setListShow(String listShow) {
		this.listShow = listShow;
	}
	public String getCatImage() {
		return catImage;
	}
	public void setCatImage(String catImage) {
		this.catImage = catImage;
	}
	public String getOuterCatId() {
		return outerCatId;
	}
	public void setOuterCatId(String outerCatId) {
		this.outerCatId = outerCatId;
	}
	public String getEcomCode() {
		return ecomCode;
	}
	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}
	public String getEcomType() {
		return ecomType;
	}
	public void setEcomType(String ecomType) {
		this.ecomType = ecomType;
	}
	public String getListShowType() {
		return listShowType;
	}
	public void setListShowType(String listShowType) {
		this.listShowType = listShowType;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}