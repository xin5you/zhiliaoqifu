package com.ebeijia.zl.core.activemq.service;

import com.ebeijia.zl.core.activemq.vo.WechatCustomerParam;
import com.ebeijia.zl.core.activemq.vo.WechatTemplateParam;

import java.util.TreeMap;

/**
 * 
 * @author zqy
 * @description  队列消息生产者，发送消息到队列
 * 
 */

public interface MQProducerService {


	/**
	 * 发送微信客服消息
	 * @param param
	 */
	 void sendMessage(final WechatCustomerParam param);


	/**
	 * 发送微信客服消息
	 * @param acountName 公众号
	 * @param notice 消息内容
	 * @param toOpenId 用户openId
	 */
	 void sendWechatMessage(String acountName, String notice, String toOpenId);

	 /**
	  * 微信公众号 模板消息推送
	  * @param acountName 微信公众号
	  * @param touser 目标用户openId
	  * @param template_id 模板Id
	  * @param url 页面跳转url
	  * @param data 消息模板数据
	  */
	 void sendTemplateMsg(String acountName, String touser, String template_id, String url, TreeMap<String, TreeMap<String, String>> data);
	 
	/**
	 * 微信公众号 模板消息推送
	 * @param templateParam 
	 */
	 void sendTemplateMsg(final WechatTemplateParam templateParam);
	 
	 
	 /*话费充值 充值订单号，发送到activemq中
	 * @param regOrderId 分销商话费充值订单号
	 */
	void sendRechargeMobileMsg(final String channelOrderId);
}
