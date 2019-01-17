package com.ebeijia.zl.core.rocketmq.service;

import com.alibaba.fastjson.JSONArray;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMQProducer {

    private Logger logger = LoggerFactory.getLogger(AbstractMQProducer.class);

    public AbstractMQProducer() { }

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    /**
     * 同步发送消息
     *
     * @param message 消息体
     * @throws RuntimeException 消息异常
     */
    public void syncSend(Message message) throws RuntimeException {
        try {
            SendResult sendResult = defaultMQProducer.send(message);
            logger.debug("send rocketmq message sendResult : {}", JSONArray.toJSONString(sendResult));
        } catch (Exception e) {
            logger.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
            throw new RuntimeException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
        }
    }
}
