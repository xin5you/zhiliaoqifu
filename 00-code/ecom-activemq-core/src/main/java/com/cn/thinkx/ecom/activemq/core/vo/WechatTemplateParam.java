package com.cn.thinkx.ecom.activemq.core.vo;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @描述: 微信模板消息参数封装类 
 * @作者: zhuqiuyou
 * @创建时间: 2017-7-17,上午11:09:03
 * @版本号: V1.0
 */
public class WechatTemplateParam {
	
	/**
	 * 微信号
	 */
	private String acountName;
	
	private String touser;
	private String template_id;
	private String url;

	private TreeMap<String, TreeMap<String, String>> data;

	public String getAcountName() {
		return acountName;
	}

	public String getTouser() {
		return touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public String getUrl() {
		return url;
	}

	public TreeMap<String, TreeMap<String, String>> getData() {
		return data;
	}

	public void setAcountName(String acountName) {
		this.acountName = acountName;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setData(TreeMap<String, TreeMap<String, String>> data) {
		this.data = data;
	}


	
}
