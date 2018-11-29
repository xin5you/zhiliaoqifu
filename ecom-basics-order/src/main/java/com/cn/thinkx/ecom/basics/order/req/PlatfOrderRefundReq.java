package com.cn.thinkx.ecom.basics.order.req;

public class PlatfOrderRefundReq {
	
	private String oriOrderId;		//外部原交易订单号
	private String refundOrderId;	//外部退款订单号（唯一）
	private String refundAmount;	//退款金额
	private String channel;			//渠道号	
	private String refundFlag;		//退款标志（1：代表系统自动发起退款；2：代表用户端发起退款）
	private long timestamp;			//时间戳
	private String sign;			//签名
	public String getOriOrderId() {
		return oriOrderId;
	}
	public String getRefundOrderId() {
		return refundOrderId;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public String getChannel() {
		return channel;
	}
	public String getRefundFlag() {
		return refundFlag;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setOriOrderId(String oriOrderId) {
		this.oriOrderId = oriOrderId;
	}
	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public void setRefundFlag(String refundFlag) {
		this.refundFlag = refundFlag;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
