package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 商品规格表
 * 
 * @author zhuqiuyou
 *
 */
public class Specification extends BaseEntity {

	private static final long serialVersionUID = 7974016699211834136L;

	private String specId;
	
	private String specName;
	
	private String specImg;
	
	private Integer specOrder;
	
	private String isDel;

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getSpecImg() {
		return specImg;
	}

	public void setSpecImg(String specImg) {
		this.specImg = specImg;
	}

	public Integer getSpecOrder() {
		return specOrder;
	}

	public void setSpecOrder(Integer specOrder) {
		this.specOrder = specOrder;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	
}
