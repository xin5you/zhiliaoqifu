package com.ebeijia.zl.service.telrecharge.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.req.AccountConsumeReqVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.service.telrecharge.mapper.ProviderOrderInfMapper;
import com.ebeijia.zl.service.telrecharge.service.ProviderOrderInfService;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * 供应商订单明细表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service()
public class ProviderOrderInfServiceImpl extends ServiceImpl<ProviderOrderInfMapper, ProviderOrderInf> implements ProviderOrderInfService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProviderOrderInfMapper providerOrderInfMapper;

	@Autowired
	private AccountTransactionFacade accountTransactionFacade;

	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public List<ProviderOrderInf> getProviderOrderInfList(ProviderOrderInf providerOrderInf) {
		return providerOrderInfMapper.getProviderOrderInfList(providerOrderInf);
	}

	@Override
	public boolean save(ProviderOrderInf entity) {
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setLockVersion(0);
		return super.save(entity);
	}

	@Override
	public boolean updateById(ProviderOrderInf entity){
		entity.setUpdateTime(System.currentTimeMillis());
		return super.updateById(entity);
	}


	/**
	 * 查找updateTime 10分钟以内，1分钟以上的订单
	 * @param providerOrderInf
	 * @return
	 */
	public List<ProviderOrderInf> getListByTimer(ProviderOrderInf providerOrderInf){
		return providerOrderInfMapper.getListByTimer(providerOrderInf);
	}

	@Override
	public ProviderOrderInf getOrderInfByChannelOrderId(String OrderId) {
		return providerOrderInfMapper.getOrderInfByChannelOrderId(OrderId);
	}

	/**
	 * 话费充值状态
	 * @param orderDetailInfo
	 */
	public void updateOrderRechargeState(ProviderOrderInf telProviderOrderInf,OrderDetailInfo orderDetailInfo,String respCode){

		if(orderDetailInfo==null){
			orderDetailInfo=new OrderDetailInfo();
		}
		//订单充值状态 0充值中 1成功 9撤销
		String rechargeState= StringUtils.isNotEmpty(orderDetailInfo.getRechargeState())? orderDetailInfo.getRechargeState():"";
		switch (rechargeState){
			case "0":
				telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode());
				break;
			case  "1":
				telProviderOrderInf.setItemCost(new BigDecimal(orderDetailInfo.getItemCost()));  //商品成本价(进价)，单位元，保留3位小数
				telProviderOrderInf.setTransCost(new BigDecimal(orderDetailInfo.getOrderCost())); //订单成本(进价)，单位元，保留3位小数，orderCost=itemCost*itemNum
				telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_1.getCode());
				break;
			default:
				telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_3.getCode());
				break;
		}
		telProviderOrderInf.setBillId(orderDetailInfo.getBillId());

		//订单付款状态 0 未付款1 已付款
		telProviderOrderInf.setPayState(StringUtils.isEmpty(orderDetailInfo.getPayState()) ? "0": orderDetailInfo.getPayState());
		telProviderOrderInf.setResv1(respCode); //记录充值渠道返回的结果信息

		telProviderOrderInf.setOperateTime(System.currentTimeMillis());
		this.updateById(telProviderOrderInf);
	}

	/**
	 *  调用供应商商接口，发起消费扣款
	 * @param providerInf
	 * @param telProviderOrderInf
	 * @return
	 */
	public boolean doMchntCustomerToProvider(ProviderInf providerInf, ProviderOrderInf telProviderOrderInf) throws Exception{


		AccountConsumeReqVo req=new AccountConsumeReqVo();
		req.setTransId(TransCode.MB10.getCode());
		req.setTransChnl(TransChnl.CHANNEL40011001.toString());
		req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		req.setUserChnlId(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,RedisDictKey.zlqf_mchnt_code));
		req.setUserType(UserType.TYPE200.getCode());
		req.setTransAmt(telProviderOrderInf.getRegTxnAmt());
		req.setUploadAmt(telProviderOrderInf.getRegTxnAmt());
		req.setDmsRelatedKey(telProviderOrderInf.getRegOrderId());
		req.setPriBId(SpecAccountTypeEnum.B06.getbId());
		req.setTransDesc("分销商话费充值");
		req.setTransNumber(1);
		req.setMchntCode(providerInf.getProviderId());
		BaseResult result=accountTransactionFacade.executeConsume(req);
		if(result !=null && "00".equals(result.getCode())){
			telProviderOrderInf.setOrderState(TeleConstants.ChannelOrderPayStat.ORDER_PAY_1.getCode()); //已扣款
			telProviderOrderInf.setItfPrimaryKey(String.valueOf(result.getObject()));
			this.updateById(telProviderOrderInf);
			return true;
		}else{
			logger.info("#平台请求供应商消费扣款失败：{}",JSONArray.toJSONString(result));
			throw new RuntimeException();
		}

	}
}
