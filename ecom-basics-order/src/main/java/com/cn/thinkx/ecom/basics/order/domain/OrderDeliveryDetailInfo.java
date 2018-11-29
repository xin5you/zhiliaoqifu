package com.cn.thinkx.ecom.basics.order.domain;

/**
 * 轨迹信息
 * 
 * @author zhuqiuyou
 *
 */
public class OrderDeliveryDetailInfo implements java.io.Serializable {

	private String time; // 时间

	private String desc; // 描述

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
