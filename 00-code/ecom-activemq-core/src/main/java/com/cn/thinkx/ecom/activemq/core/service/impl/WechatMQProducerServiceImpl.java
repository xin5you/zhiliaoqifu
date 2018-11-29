package com.cn.thinkx.ecom.activemq.core.service.impl;

import java.util.Map;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.activemq.core.service.WechatMQProducerService;
import com.cn.thinkx.ecom.activemq.core.vo.WechatCustomerParam;
import com.cn.thinkx.ecom.activemq.core.vo.WechatTemplateParam;

/**
 * 
 * @author zqy
 * @description 队列消息生产者，发送消息到队列
 * 
 */
@Service("wechatMQProducerService")
public class WechatMQProducerServiceImpl implements WechatMQProducerService {

	// private static Logger logger =
	// LoggerFactory.getLogger(WechatMQProducerServiceImpl.class);

	/**
	 * 微信客服消息
	 */
	@Autowired
	@Qualifier("consumerMsgJmsTemplate")
	private JmsTemplate consumerMsgJmsTemplate;

	/**
	 * 微信模板消息
	 */
	@Autowired
	@Qualifier("templateMsgJmsTemplate")
	private JmsTemplate templateMsgJmsTemplate;

	/**
	 * 发送微信客服消息
	 * 
	 * @param param
	 */
	public void sendMessage(final WechatCustomerParam param) {
		consumerMsgJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(JSONObject.toJSONString(param));
			}
		});
	}

	/**
	 * 发送微信客服消息
	 * 
	 * @param acountName
	 *            公众号
	 * @param notice
	 *            消息内容
	 * @param toOpenId
	 *            用户openId
	 */
	public void sendWechatMessage(String acountName, String notice, String toOpenId) {
		WechatCustomerParam wechantParam = new WechatCustomerParam();
		wechantParam.setAcountName(acountName);
		wechantParam.setToOpenId(toOpenId);
		wechantParam.setContent(notice);
		this.sendMessage(wechantParam);
	}

	public void setConsumerMsgJmsTemplate(JmsTemplate consumerMsgJmsTemplate) {
		this.consumerMsgJmsTemplate = consumerMsgJmsTemplate;
	}

	public JmsTemplate getConsumerMsgJmsTemplate() {
		return consumerMsgJmsTemplate;
	}

	public void setTemplateMsgJmsTemplate(JmsTemplate templateMsgJmsTemplate) {
		this.templateMsgJmsTemplate = templateMsgJmsTemplate;
	}

	/**
	 * 微信公众号 模板消息推送
	 * 
	 * @param acountName
	 *            微信公众号
	 * @param touser
	 *            目标用户openId
	 * @param template_id
	 *            模板Id
	 * @param url
	 *            页面跳转url
	 * @param Data
	 *            消息模板数据
	 */
	public void sendTemplateMsg(String acountName, String touser, String template_id, String url,
			TreeMap<String, TreeMap<String, String>> data) {
		WechatTemplateParam templateParam = new WechatTemplateParam();
		templateParam.setAcountName(acountName);
		templateParam.setTouser(touser);
		templateParam.setTemplate_id(template_id);
		templateParam.setData(data);
		templateParam.setUrl(url);
		this.sendTemplateMsg(templateParam);
	}

	/**
	 * 微信公众号 模板消息推送
	 * 
	 * @param templateParam
	 */
	public void sendTemplateMsg(final WechatTemplateParam templateParam) {
		templateMsgJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(JSONObject.toJSONString(templateParam));
			}
		});
	}
}
