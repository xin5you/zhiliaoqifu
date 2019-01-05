package com.ebeijia.zl.jms;

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
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 立方话费充值
 *
 * @author zhuqiuyou
 * 
 * @since 2018-07-10 11:21:23
 *  
 */
@MQConsumer(topic = RocketTopicEnums.withDrawTopic,tag=RocketTopicEnums.withDrawTag, consumerGroup = "${spring.rocketmq.producer-group}")
public class WithDrawSessionAwareMessageListener extends AbstractMQPushConsumer {
	
	private Logger logger = LoggerFactory.getLogger(WithDrawSessionAwareMessageListener.class);

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

	@Override
	public boolean process(Object message, Map map) {

		logger.info("待发送的消息message={}", message);
		logger.info("待发送的消息map=>{}", JSONObject.toJSONString(map));


			String batchNo = String.valueOf(message); //获取批次号
			String redisKV=jedisClusterUtils.get(RedisConstants.REDIS_HASH_BIZ_WITHDRAW_ACCEPTS+batchNo);

			if(StringUtils.isNotEmpty(redisKV)){
				logger.info("用户提现操作失败，重复交易,批次号->{}",batchNo);
				return  true;
			}else{
				jedisClusterUtils.set(RedisConstants.REDIS_HASH_BIZ_WITHDRAW_ACCEPTS+batchNo,batchNo,60*60*12);
			}
			//获取用户的提现批次数据
			AccountWithdrawOrder accountWithdrawOrder=accountWithdrawOrderService.getById(batchNo);
			//获取用户的提现数据明细
			List<AccountWithdrawDetail> detailList=accountWithdrawDetailService.getListByBatchNo(batchNo);

			if (accountWithdrawOrder==null || detailList==null || detailList.size()<=0){
				logger.info("用户提现操作失败，未找到订单数据,批次号->{}",batchNo);
				return true;
			}

			if (!WithDrawStatusEnum.Status00.getCode().equals(accountWithdrawOrder.getStatus())){
				logger.info("用户提现操作失败，重复交易,批次号->{}",batchNo);
				return true;
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
					return true;
				}

			}else{
				accountWithdrawOrder.setStatus(WithDrawStatusEnum.Status04.getCode());
			}
			accountWithdrawOrderService.updateById(accountWithdrawOrder);
		//获取分销商订单
		return true;
	}
}