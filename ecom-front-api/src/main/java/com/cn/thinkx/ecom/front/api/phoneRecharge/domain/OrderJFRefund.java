package com.cn.thinkx.ecom.front.api.phoneRecharge.domain;

public class OrderJFRefund {
	
	private String trade_no;		//退款流水号（平台退款流水号）
	private String old_trade_no;	//原平台支付流水号
	private String total_amount;	//原平台支付金额
	private String refund_amount;	//退款金额
	private String timestamp;		//时间戳
	private String sign_type;		//签名类型
	private String sign;			//签名
	
	private String out_trade_id;	//渠道退款流水号
	private String code;			//支付返回码
	private String errmsg;			//错误描述
	
	private String out_trade_no;	//渠道交易号
	private String refund_falg;		//是否退款
	public String getTrade_no() {
		return trade_no;
	}
	
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	
	public String getOld_trade_no() {
		return old_trade_no;
	}
	
	public void setOld_trade_no(String old_trade_no) {
		this.old_trade_no = old_trade_no;
	}
	
	public String getTotal_amount() {
		return total_amount;
	}
	
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	
	public String getRefund_amount() {
		return refund_amount;
	}
	
	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getSign_type() {
		return sign_type;
	}
	
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
	public String getSign() {
		return sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getOut_trade_id() {
		return out_trade_id;
	}
	
	public void setOut_trade_id(String out_trade_id) {
		this.out_trade_id = out_trade_id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getErrmsg() {
		return errmsg;
	}
	
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	public String getRefund_falg() {
		return refund_falg;
	}

	public void setRefund_falg(String refund_falg) {
		this.refund_falg = refund_falg;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	@Override
	public String toString() {
		return "OrderJFRefund [trade_no=" + trade_no + ", old_trade_no=" + old_trade_no + ", total_amount="
				+ total_amount + ", refund_amount=" + refund_amount + ", timestamp=" + timestamp + ", sign_type="
				+ sign_type + ", sign=" + sign + ", out_trade_id=" + out_trade_id + ", code=" + code + ", errmsg="
				+ errmsg + ", out_trade_no=" + out_trade_no + ", refund_falg=" + refund_falg + "]";
	}

}
