package com.ebeijia.zl.core.rocketmq.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.domain.SmsVo;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.rocketmq.enums.RocketTopicEnums;
import com.ebeijia.zl.core.rocketmq.vo.WechatCustomerParam;
import com.ebeijia.zl.core.rocketmq.vo.WechatTemplateParam;
import com.maihaoche.starter.mq.annotation.MQProducer;
import com.maihaoche.starter.mq.base.AbstractMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.TreeMap;

/**
 * rocketmq 服务提供者
 */
@MQProducer
public class MQProducerService extends AbstractMQProducer {
    private Logger logger = LoggerFactory.getLogger(MQProducerService.class);


    @Autowired
    private JedisCluster jedisCluster;


    /**
     * @param channelOrderId 分销商话费充值订单号
     */
    public void sendRechargeMobileMsg(final String channelOrderId){
        logger.info("手机充值，渠道订单channelOrderId={}",channelOrderId);
        Message message=new Message(RocketTopicEnums.mobileRechangeTopic,RocketTopicEnums.mobileRechangeTag,channelOrderId.getBytes());
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
                Message message=new Message(RocketTopicEnums.smsTopic,RocketTopicEnums.smsTag,msg.getBytes());
                super.syncSend(message);
        }
    }

}
