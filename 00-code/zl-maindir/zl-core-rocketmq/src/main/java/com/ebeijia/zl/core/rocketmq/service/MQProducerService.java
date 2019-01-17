package com.ebeijia.zl.core.rocketmq.service;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.domain.SmsVo;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.rocketmq.enums.RocketTopicEnums;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisCluster;

import java.util.TreeMap;

/**
 * rocketmq 服务提供者
 */
@Configuration
public class MQProducerService extends AbstractMQProducer{
    private Logger logger = LoggerFactory.getLogger(MQProducerService.class);

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private JedisCluster jedisCluster;


    /**
     * @param channelOrderId 分销商话费充值订单号
     */
    public void sendRechargeMobileMsg(final String channelOrderId){
        logger.info("手机充值，渠道订单channelOrderId={}",channelOrderId);
        Message message=new Message(RocketTopicEnums.mobileRechangeTopic,RocketTopicEnums.mobileRechangeTag,channelOrderId,channelOrderId.getBytes());
        super.syncSend(message);
    }

    /**
     * 短信发送
     * @param smsVo
     */
    public void sendSMS(final SmsVo smsVo){
        String msg=JSONArray.toJSONString(smsVo);
        logger.info("短信发送 sendSMS={}",msg);
        //短信开关 Y：可发短信; N: 不可发短信
        if("Y".equals(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"SMS_SWITCH_FLAG"))){
                Message message=new Message(RocketTopicEnums.smsTopic,RocketTopicEnums.smsTag,smsVo.getMsgId(),msg.getBytes());
                super.syncSend(message);
        }
    }

    /**
     * @param batchNo 提现操作批次号
     */
    public void sendWithDrawBatchNo(final String batchNo){
        logger.info("提现操作，batchNo={}",batchNo);
        Message message=new Message(RocketTopicEnums.withDrawTopic,RocketTopicEnums.withDrawTag,batchNo,batchNo.getBytes());
        message.setDelayTimeLevel(2);
        super.syncSend(message);
    }

}
