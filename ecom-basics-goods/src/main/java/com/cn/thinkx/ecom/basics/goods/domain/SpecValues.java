package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 商品规格值表
 * @author zhuqiuyou
 *
 */
public class SpecValues extends BaseEntity {

	private static final long serialVersionUID = -6514061660777119446L;
	private String   specValueId;
	private String   specId;
	private String   specValue;
	private String   specImage;
	private Integer  specOrder;
	private String   specType;
	private Integer  inherentOrAdd;
	
	
	/***业务字段**/
	private String productId;//货品ID
	
	
	public String getSpecValueId() {
		return specValueId;
	}
	public void setSpecValueId(String specValueId) {
		this.specValueId = specValueId;
	}
	public String getSpecId() {
		return specId;
	}
	public void setSpecId(String specId) {
		this.specId = specId;
	}
	public String getSpecValue() {
		return specValue;
	}
	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}
	public String getSpecImage() {
		return specImage;
	}
	public void setSpecImage(String specImage) {
		this.specImage = specImage;
	}
	public Integer getSpecOrder() {
		return specOrder;
	}
	public void setSpecOrder(Integer specOrder) {
		this.specOrder = specOrder;
	}
	public String getSpecType() {
		return specType;
	}
	public void setSpecType(String specType) {
		this.specType = specType;
	}
	public Integer getInherentOrAdd() {
		return inherentOrAdd;
	}
	public void setInherentOrAdd(Integer inherentOrAdd) {
		this.inherentOrAdd = inherentOrAdd;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
}
