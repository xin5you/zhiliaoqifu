package com.cn.thinkx.ecom.basics.goods.domain;

import java.util.List;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 电商楼层
 * 
 * @author kpplg
 *
 */
public class Floor extends BaseEntity {

	private static final long serialVersionUID = -4262982014086839784L;

	private String floorId; // 主键ID
	private String title; // 楼层标题
	private String parentId; // parent_id
	private String type;
	private String ecomCode; // 商城代码
	private String style;
	private String logo;
	private String isDisplay; // 是否显示
	private String sort; // 排序
	private String catId;

	private List<Goods> goods; // 楼层商品信息

	private String checkDefault; // 在首页判断是显示全部还是固定条数

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEcomCode() {
		return ecomCode;
	}

	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

	public String getCheckDefault() {
		return checkDefault;
	}

	public void setCheckDefault(String checkDefault) {
		this.checkDefault = checkDefault;
	}

}
