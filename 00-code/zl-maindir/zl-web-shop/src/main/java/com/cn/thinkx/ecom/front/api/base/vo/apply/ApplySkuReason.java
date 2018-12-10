package com.cn.thinkx.ecom.front.api.base.vo.apply;

public class ApplySkuReason {

	/*
	 * 退换货原因，指定文案：无理由、质量问题，最大128位
	 */
	private String reason;
	
	/*
	 * 退换货详细原因，最大512位
	 */
	private String reasonDesc;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	
}
