package com.cn.thinkx.ecom.basics.order.domain;

import java.util.List;

/**
 * 物流轨迹信息
 * 
 * @author zhuqiuyou
 *
 */
public class OrderDeliveryInfo implements java.io.Serializable {

	private static final long serialVersionUID = -7567204133874909260L;

	private String sOrderId;// 二级订单号

	private String packageId; // 包裹ID

	private String company; // 物流公司

	private String number; // 运单号

	private List<OrderDeliveryDetailInfo> content;

	public String getsOrderId() {
		return sOrderId;
	}

	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

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

	public List<OrderDeliveryDetailInfo> getContent() {
		return content;
	}

	public void setContent(List<OrderDeliveryDetailInfo> content) {
		this.content = content;
	}
}
