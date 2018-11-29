package com.cn.thinkx.ecom.front.api.phoneRecharge.req;

public class PhoneRechargeGetReq {

	private String channelOrderNo;
	private String reqChannel;
	private long timestamp; //时间戳
	private String sign;
	
	public String getChannelOrderNo() {
		return channelOrderNo;
	}
	public void setChannelOrderNo(String channelOrderNo) {
		this.channelOrderNo = channelOrderNo;
	}
	public String getReqChannel() {
		return reqChannel;
	}
	public void setReqChannel(String reqChannel) {
		this.reqChannel = reqChannel;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
