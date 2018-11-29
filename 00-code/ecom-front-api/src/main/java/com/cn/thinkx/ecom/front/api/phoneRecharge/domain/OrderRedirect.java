package com.cn.thinkx.ecom.front.api.phoneRecharge.domain;

/**
 * 接收知了企服重定向参数
 * 
 * @author xiaomei
 *
 */
public class OrderRedirect {

	private String channel;
	private String respResult;
	private String userId;
	private String orderId;
	private String txnFlowNo;
	private String attach;
	private String sign;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRespResult() {
		return respResult;
	}

	public void setRespResult(String respResult) {
		this.respResult = respResult;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTxnFlowNo() {
		return txnFlowNo;
	}

	public void setTxnFlowNo(String txnFlowNo) {
		this.txnFlowNo = txnFlowNo;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "{respResult=[" + respResult + "], orderId=[" + orderId + "], channel=[" + channel + "], userId=["
				+ userId + "], txnFlowNo=[" + txnFlowNo + "], attach=[" + attach + "], sign=[" + sign + "]}";
	}

}
