package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class Esetting extends BaseEntity{

	private static final long serialVersionUID = -4262982014086839784L;
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 电商名称
	 */
	private String ecomName;
	/**
	 * 电商代码
	 */
	private String ecomCode;
	/**
	 * 电商类别
	 */
	private String ecomType;
	/**
	 * 满减金额
	 */
	private String fullMoney;
	/**
	 * 运费
	 */
	private String ecomFreight;
	
	/**
	 * 购物描述
	 */
	private String shopDesc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEcomName() {
		return ecomName;
	}

	public void setEcomName(String ecomName) {
		this.ecomName = ecomName;
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

	public String getFullMoney() {
		return fullMoney;
	}

	public void setFullMoney(String fullMoney) {
		this.fullMoney = fullMoney;
	}

	public String getEcomFreight() {
		return ecomFreight;
	}

	public void setEcomFreight(String ecomFreight) {
		this.ecomFreight = ecomFreight;
	}

	public String getShopDesc() {
		return shopDesc;
	}

	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}
	
	
}
