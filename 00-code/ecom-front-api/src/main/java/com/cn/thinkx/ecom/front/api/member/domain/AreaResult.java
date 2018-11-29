package com.cn.thinkx.ecom.front.api.member.domain;

import java.util.List;

import com.cn.thinkx.ecom.basics.member.domain.CityInf;

public class AreaResult {

	/** 返回码 */
	private String code;
	/** 返回信息 */
	private String content;
	/** 返回对象 */
	private List<CityInf> data;
	public String getCode() {
		return code;
	}
	public String getContent() {
		return content;
	}
	public List<CityInf> getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setData(List<CityInf> data) {
		this.data = data;
	}
}
