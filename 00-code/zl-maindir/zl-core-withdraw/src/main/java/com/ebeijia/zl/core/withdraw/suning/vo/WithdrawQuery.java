package com.ebeijia.zl.core.withdraw.suning.vo;

/**
 * 代付查询返回参数
 * 
 * @author zhuqi
 *
 */
public class WithdrawQuery implements java.io.Serializable {

	private static final long serialVersionUID = 8447362524153743371L;

	private String responseCode;
	private String responseMsg;
	private Content content;
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	
}
