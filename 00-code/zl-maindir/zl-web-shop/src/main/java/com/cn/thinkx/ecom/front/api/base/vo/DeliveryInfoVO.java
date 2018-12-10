package com.cn.thinkx.ecom.front.api.base.vo;

import java.util.List;

/**
 * 	物流轨迹信息 
 * @author zhuqiuyou
 *
 */
public class DeliveryInfoVO implements java.io.Serializable {

	private static final long serialVersionUID = 8312094419322104272L;

	private String company;
	
	private String number;
	
	private List<DeliveryDetailInfoVO> content;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<DeliveryDetailInfoVO> getContent() {
		return content;
	}

	public void setContent(List<DeliveryDetailInfoVO> content) {
		this.content = content;
	}
}
