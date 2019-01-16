package com.ebeijia.zl.jms;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.rocketmq.enums.RocketTopicEnums;
import com.ebeijia.zl.core.withdraw.suning.config.YFBWithdrawConfig;
import com.ebeijia.zl.core.withdraw.suning.core.BatchWithdrawData;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawBodyVO;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawDetailDataVO;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.enums.WithDrawStatusEnum;
import com.ebeijia.zl.service.account.service.IAccountWithdrawDetailService;
import com.ebeijia.zl.service.account.service.IAccountWithdrawOrderService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class WithDrawSessionAwareMessageListener implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(WithDrawSessionAwareMessageListener.class);

    @Autowired
    private JedisClusterUtils jedisClusterUtils;

    @Autowired
    private YFBWithdrawConfig yfbWithdrawConfig;

    @Autowired
    private BatchWithdrawData batchWithdrawData;

    @Autowired
    private IAccountWithdrawDetailService accountWithdrawDetailService;

    @Autowired
    private IAccountWithdrawOrderService accountWithdrawOrderService;

    /**
     *  默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
     *  不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(msgs)){
            //接受到的消息为空，不处理，直接返回成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt messageExt = msgs.get(0);
        logger.info("接受到的消息为："+messageExt.toString());

        if(messageExt.getTopic().equals(RocketTopicEnums.withDrawTopic)){
            String batchNo=null;
            try {
                batchNo = new String(messageExt.getBody(), "UTF-8");
            } catch (UnsupportedEncodingException e){
                logger.error("body转字符串解析失败");
            }
            int reconsume = messageExt.getReconsumeTimes();
            if(reconsume ==1){//消息已经重试了1次，如果不需要再次消费，则返回成功
                logger.info("用户提现操作失败，重复交易,批次号->{},reconsumetims={}",batchNo,reconsume);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }else{
                jedisClusterUtils.set(RedisConstants.REDIS_HASH_BIZ_WITHDRAW_ACCEPTS+batchNo,batchNo,60*60);
            }

            //获取用户的提现批次数据
            AccountWithdrawOrder accountWithdrawOrder=accountWithdrawOrderService.getById(batchNo);
            //获取用户的提现数据明细
            List<AccountWithdrawDetail> detailList=accountWithdrawDetailService.getListByBatchNo(batchNo);

            if (accountWithdrawOrder==null || detailList==null || detailList.size()<=0){
                logger.info("用户提现操作失败，未找到订单数据,批次号->{}",batchNo);
                return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

            if (!WithDrawStatusEnum.Status00.getCode().equals(accountWithdrawOrder.getStatus())){
                logger.info("用户提现操作失败，重复交易,批次号->{},Status={}",batchNo,accountWithdrawOrder.getStatus());
                return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            //提现交易开关
            String switchFlag=jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WITHDRAW_SWITCH_FLAG");

            if("Y".equals(switchFlag)) {

                WithdrawBodyVO bodyVO = new WithdrawBodyVO();
                bodyVO.setBatchNo(accountWithdrawOrder.getBatchNo());
                bodyVO.setTotalNum(accountWithdrawOrder.getTotalNum());

                List<WithdrawDetailDataVO> list = new ArrayList<WithdrawDetailDataVO>();
                WithdrawDetailDataVO detailDataVO = null;
                for (AccountWithdrawDetail accountWithdrawDetail : detailList) { //遍历账户详情
                    detailDataVO = new WithdrawDetailDataVO();
                    detailDataVO.setSerialNo(accountWithdrawDetail.getSerialNo());
                    detailDataVO.setReceiverCardNo(accountWithdrawDetail.getReceivercardNo()); //收款卡号
                    detailDataVO.setReceiverName(accountWithdrawDetail.getReceiverName());
                    detailDataVO.setBankName(accountWithdrawDetail.getBankName());  //开户行
                    detailDataVO.setBankCode(accountWithdrawDetail.getBankCode());  //开户行编号
                    detailDataVO.setAmount(accountWithdrawDetail.getTransAmount().setScale(0).longValue());
                    detailDataVO.setRemark(accountWithdrawDetail.getRemarks());
                    detailDataVO.setOrderName(accountWithdrawDetail.getOrderName());
                    detailDataVO.setBankProvince(accountWithdrawDetail.getBankProvince());
                    detailDataVO.setBankCity(accountWithdrawDetail.getBankCity());
                    detailDataVO.setPayeeBankLinesNo(accountWithdrawDetail.getPayeeBankLinesNo());
                    list.add(detailDataVO);
                }
                try {
                    bodyVO.setDetailData(list);
                    String respStr = batchWithdrawData.batchWithDraw(bodyVO);
                    JSONObject json = JSONObject.parseObject(respStr);
                    if (json.containsKey("responseCode") && "0000".equals(json.get("responseCode"))) {// 易付宝受理成功
                        accountWithdrawOrder.setStatus(WithDrawStatusEnum.Status01.getCode());
                    }
                    accountWithdrawOrder.setErrorCode(json.getString("responseCode"));
                } catch (Exception ex) {
                    logger.error("苏宁代付请求失败->{}", ex);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

            }else{
                accountWithdrawOrder.setStatus(WithDrawStatusEnum.Status04.getCode());
            }
            accountWithdrawOrderService.updateById(accountWithdrawOrder);
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}