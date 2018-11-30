package com.cn.thinkx.ecom.front.api.base.vo;

/**
 * 轨迹信息
 * @author zhuqiuyou
 *
 */
public class DeliveryDetailInfoVO  implements java.io.Serializable{

	private static final long serialVersionUID = -5632633439179698610L;
	
	private String time;
	
	private String desc;

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
