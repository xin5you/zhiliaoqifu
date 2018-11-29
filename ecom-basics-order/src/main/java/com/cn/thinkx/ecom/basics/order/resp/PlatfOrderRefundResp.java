package com.cn.thinkx.ecom.basics.order.resp;

public class PlatfOrderRefundResp {
	
	private String code;	//返回码（00：代表退款成功）
	private String msg;		//返回信息
	private String txnFlowNo;	//知了企服退款流水
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public String getTxnFlowNo() {
		return txnFlowNo;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setTxnFlowNo(String txnFlowNo) {
		this.txnFlowNo = txnFlowNo;
	}
	
}
