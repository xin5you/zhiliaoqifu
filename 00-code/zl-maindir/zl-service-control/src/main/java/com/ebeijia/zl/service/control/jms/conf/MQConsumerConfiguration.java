package com.ebeijia.zl.service.control.jms.conf;

import com.ebeijia.zl.core.rocketmq.enums.RocketTopicEnums;
import com.ebeijia.zl.core.rocketmq.exceptions.RocketMQException;
import com.ebeijia.zl.service.control.jms.listener.SMSTemplateMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConsumerConfiguration {

    public  Logger logger = LoggerFactory.getLogger(MQConsumerConfiguration.class);

    @Value("${spring.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${spring.rocketmq.groupName}")
    private String groupName;

    @Value("${spring.rocketmq.consumeThreadMin}")
    private int consumeThreadMin;

    @Value("${spring.rocketmq.consumeThreadMax}")
    private int consumeThreadMax;

    @Value("${spring.rocketmq.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    @Autowired
    private SMSTemplateMessageListener smsTemplateMessageListener;
    
    @Bean("smsTemplateMessageMQConsumer")
    public DefaultMQPushConsumer withDrawSessionAwareMQConsumer() throws RocketMQException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("zlqf_group");
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(smsTemplateMessageListener);

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        /**
         * 设置一次消费消息的条数，默认为1条
         */
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
            consumer.subscribe(RocketTopicEnums.smsTopic,"*");
            consumer.start();
            logger.info("consumer is start !!! groupName:{},namesrvAddr:{}",groupName,namesrvAddr);
        }catch (MQClientException e){
            logger.error("consumer is start !!! groupName:{},namesrvAddr:{}",groupName,namesrvAddr,e);
        }
        return consumer;
    }
}