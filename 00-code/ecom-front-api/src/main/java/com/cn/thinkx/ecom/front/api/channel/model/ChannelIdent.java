package com.cn.thinkx.ecom.front.api.channel.model;

/**
 * 渠道接入model
 * 
 * @author zhuqiuyou
 *
 */
public class ChannelIdent implements java.io.Serializable {

	private static final long serialVersionUID = -6710321084261767444L;
	
	private String channelCode;  //渠道号
	
	private String service;	//服务接口
	
	private String timestamp; //时间戳
	
	private String version; //版本号
	
	private String cUserId; //渠道商户主键
	
	private String mobile; //渠道用户手机号
	
	private String extandParams; //扩展字符串
	
	private String sign;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getExtandParams() {
		return extandParams;
	}

	public void setExtandParams(String extandParams) {
		this.extandParams = extandParams;
	}

	public String getcUserId() {
		return cUserId;
	}

	public void setcUserId(String cUserId) {
		this.cUserId = cUserId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
