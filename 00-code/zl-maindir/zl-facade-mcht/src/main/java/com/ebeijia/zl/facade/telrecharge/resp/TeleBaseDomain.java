package com.ebeijia.zl.facade.telrecharge.resp;

import lombok.Data;

/**
 * 手机充值入参公用参数
 * @author zhuqiuyou
 *
 */
@Data
public class TeleBaseDomain implements java.io.Serializable {

	private String channelId; //渠道标识
	
	private String channelToken; //渠道token
	
	private String method;//充值的类型
	
	private String v; //版本
	
	private String timestamp; //日期格式
	
	private String sign; //系统签名
}
