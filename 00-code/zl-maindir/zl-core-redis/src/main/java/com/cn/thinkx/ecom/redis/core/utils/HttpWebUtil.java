package com.cn.thinkx.ecom.redis.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * http工具类
 */
@Configuration
public class HttpWebUtil{
	
	@Autowired
	private RedisDictProperties redisDictProperties;
	
	private  Logger logger = LoggerFactory.getLogger(HttpWebUtil.class);
	
	/**
	 * 获取商户服务系统的路径
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public  String getMerchantDomainUrl(){
		return redisDictProperties.getdictValueByCode("MERCHANT_DOMAIN_URL");
	}
	
	/**
	 * 获取我的订单的路径
	 * @return
	 */
	public  String getOrderDomainUrl(){
		return redisDictProperties.getdictValueByCode("ORDER_DOMAIN_URL");
	}
	
	/**
	 * 获取商城对接的密钥
	 * @return
	 */
	public  String getReqAesKey(){
		return redisDictProperties.getdictValueByCode("40006001_REQ_AES_KEY");
	}
	
	/**获取汇卡宝图片服务器路径
	 * @return
	 */
	public  String getHkbUrlImg(){
		return redisDictProperties.getdictValueByCode("HKB_URL_IMG");
	}
	
	/**
	 * 获取客户系统的域名
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public  String getCustomerDomainUrl(){
		return redisDictProperties.getdictValueByCode("CUSTOMER_DOMAIN_URL");
	}
	
	/**
	 * websocket 连接域名解析
	 * @return
	 */
	public  String getMerchantWsUrl(){
		String wsurl=getMerchantDomainUrl();
		if(wsurl.contains("https://")){
			return StringUtils.substringAfterLast(wsurl,"https://");
		}else{
			return StringUtils.substringAfterLast(wsurl,"http://");
		}
	}
	
}
