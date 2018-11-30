package com.cn.thinkx.ecom.front.api.base.vo.goods;

import java.util.List;

public class APICategoryTO {
	private String id;
	private String name;
	private List<APICategoryTO> subCateList;
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
	public List<APICategoryTO> getSubCateList() {
		return subCateList;
	}
	public void setSubCateList(List<APICategoryTO> subCateList) {
		this.subCateList = subCateList;
	}
}
