package com.ebeijia.zl.service.telrecharge.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.*;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.req.AccountConsumeReqVo;
import com.ebeijia.zl.facade.account.req.AccountRefundReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.telrecharge.domain.*;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants;
import com.ebeijia.zl.service.telrecharge.mapper.RetailChnlOrderInfMapper;
import com.ebeijia.zl.service.telrecharge.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * 分销商订单信息表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class RetailChnlOrderInfServiceImpl extends ServiceImpl<RetailChnlOrderInfMapper,RetailChnlOrderInf> implements RetailChnlOrderInfService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RetailChnlOrderInfMapper retailChnlOrderInfMapper;
	
	@Autowired
	private RetailChnlInfService retailChnlInfService;
	
	@Autowired
	private RetailChnlProductInfService retailChnlProductInfService;
	
	@Autowired
	private ProviderOrderInfService providerOrderInfService;

	@Autowired
	private ProviderInfService providerInfService;

	@Autowired
	private AccountTransactionFacade accountTransactionFacade;

	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public boolean save(RetailChnlOrderInf entity) {
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setLockVersion(0);
		return super.save(entity);
	}

	@Override
	public boolean updateById(RetailChnlOrderInf entity){
		entity.setUpdateTime(System.currentTimeMillis());
		return super.updateById(entity);
	}
	
	/**
	 * 
	 * @param retailChnlOrderInf 分销商话费充值
	 * @param operId 运营商
	 * @param areaName 地区名称
	 * @return
	 * @throws Exception
	 */
	public BaseResult proTelChannelOrder(RetailChnlOrderInf retailChnlOrderInf, String operId, String areaName) throws Exception{
		
		//获取分销商信息
		RetailChnlInf retailChnlInf= retailChnlInfService.getById(retailChnlOrderInf.getChannelId());
		if(retailChnlInf ==null){
			return ResultsUtil.error("110001", "渠道号不存在");
		}
		
		//获取分销商的产品 & 折扣率
		Map maps=new HashMap<>();
		maps.put("productId", retailChnlOrderInf.getProductId());
		//maps.put("operId", operId);
		//maps.put("areaName", areaName);
		maps.put("bId", SpecAccountTypeEnum.B06.getbId()); //分销商ID
		maps.put("channelId", retailChnlInf.getChannelId()); //分销商ID
		maps.put("productAmt", retailChnlOrderInf.getRechargeValue().setScale(0).toString());
		maps.put("productType", retailChnlOrderInf.getRechargeType());
		
		RetailChnlProductInf telChannelProductInf =retailChnlProductInfService.getProductRateByMaps(maps);
		if(telChannelProductInf ==null){
			return ResultsUtil.error("110002", "产品不存在");
		}
		
		//产品售价 = 产品的价格 * 折扣率
		BigDecimal	payAmt= telChannelProductInf.getProductPrice().multiply(telChannelProductInf.getChannelRate()).setScale(3, BigDecimal.ROUND_DOWN);
		
//		//判断当前的备付金是否大于当前的充值的产品售价
//		if (RetailChnlInf.getChannelReserveAmt().compareTo(payAmt)== -1){
//			return ResultsUtil.error("110003", "备付金不足");
//		}
		
		retailChnlOrderInf.setPayAmt(payAmt);
		
		//扣减备付金 
	   //	int resOper=RetailChnlInfFacade.subChannelReserveAmt(RetailChnlInf.getChannelId(),payAmt);
		//保存订单o.toString()o.toString()o.toString()
		
		retailChnlOrderInf.setDataStat("0"); //
		retailChnlOrderInf.setOrderStat(TeleConstants.ChannelOrderPayStat.ORDER_PAY_0.getCode()); //待扣款
		retailChnlOrderInf.setItemNum(1);//产品数量
		retailChnlOrderInf.setChannelRate(telChannelProductInf.getChannelRate()); //折扣率
		retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_1.getCode());  //处理中
		if(telChannelProductInf !=null){
			retailChnlOrderInf.setProductId(telChannelProductInf.getProductId());
		}
		if(StringUtil.isNotEmpty(retailChnlOrderInf.getNotifyUrl())){
			retailChnlOrderInf.setNotifyFlag("0");
		}else{
			retailChnlOrderInf.setNotifyFlag("1");
		}
		retailChnlOrderInf.setTxnAmt(AmountUtil.RMBYuanToCent(retailChnlOrderInf.getPayAmt()));
		boolean resOper=this.save(retailChnlOrderInf); //保存分销商订单

		ProviderOrderInf providerOrderInf=null;
		if(resOper){


			providerOrderInf=new ProviderOrderInf();
			providerOrderInf.setRegOrderAmt(retailChnlOrderInf.getRechargeValue()); //充值面额
			providerOrderInf.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
			providerOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_8.getCode()); //待充值
			providerOrderInf.setPayState(TeleConstants.ChannelOrderPayStat.ORDER_PAY_0.getCode());
			providerOrderInf.setProviderId(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,RedisDictKey.zlqf_privoder_code+SpecAccountTypeEnum.B06.getbId())); //话费充值供应商Id
			providerOrderInf.setDataStat("0");

			ProviderInf providerInf = providerInfService.getById(providerOrderInf.getProviderId());
			providerOrderInf.setRegTxnAmt(AmountUtil.mul(AmountUtil.RMBYuanToCent(providerOrderInf.getRegOrderAmt()),providerInf.getProviderRate()));

			//分销商消费扣款
			resOper=doRetailCustomerToMchnt(retailChnlInf,retailChnlOrderInf,providerOrderInf);

		}
		if(!resOper){
			//操作不同步，则回退事物
			throw new RuntimeException();
		}
		
		//返回成功的封装数据
		TeleRespVO respVo=new TeleRespVO();
		respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
		respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
		respVo.setPayState(retailChnlOrderInf.getOrderStat());
		respVo.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode());
		respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date()));
		respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
		respVo.setItemNum(retailChnlOrderInf.getItemNum());
		respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
		return ResultsUtil.success(respVo);
	}
	
	public List<RetailChnlOrderInf> getRetailChnlOrderInfList(RetailChnlOrderInf retailChnlOrderInf) throws Exception {
		return retailChnlOrderInfMapper.getRetailChnlOrderInfList(retailChnlOrderInf);
	}
	
	public void doRechargeMobileMsg(String channelOrderId){
//		rechargeMobileProducerService.sendRechargeMobileMsg(channelOrderId);
	}
	/**
	 *  分销商 根据外部订单查询
	 * @param outerId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public RetailChnlOrderInf getRetailChnlOrderInfByOuterId(String outerId,String channelId) throws Exception{
		return retailChnlOrderInfMapper.getRetailChnlOrderInfByOuterId(outerId,channelId);
	}
	
	/**
	 * 分销商订单分页列表
	 * @param startNum
	 * @param pageSize
	 * @param RetailChnlOrderInf
	 * @return
	 * @throws Exception 
	 */
     public PageInfo<RetailChnlOrderInf> getRetailChnlOrderInfPage(int startNum, int pageSize, RetailChnlOrderInf RetailChnlOrderInf) throws Exception{
    		PageHelper.startPage(startNum, pageSize);
    		List<RetailChnlOrderInf> retailChnlOrderInfList = getRetailChnlOrderInfList(RetailChnlOrderInf);
    		for (RetailChnlOrderInf chnlOrderInf : retailChnlOrderInfList) {
				if(!StringUtil.isNullOrEmpty(chnlOrderInf.getRechargeType()))
					chnlOrderInf.setRechargeType(TelRechargeConstants.ShopType.findByCode(chnlOrderInf.getRechargeType()));
				if(!StringUtil.isNullOrEmpty(chnlOrderInf.getOrderStat()))
					chnlOrderInf.setOrderStat(TelRechargeConstants.ChannelOrderStat.findByCode(chnlOrderInf.getOrderStat()));
				if(!StringUtil.isNullOrEmpty(chnlOrderInf.getNotifyStat()))
					chnlOrderInf.setNotifyStat(TelRechargeConstants.ChannelOrderNotifyStat.findByCode(chnlOrderInf.getNotifyStat()));
			}
    		PageInfo<RetailChnlOrderInf> RetailChnlOrderInfPage = new PageInfo<RetailChnlOrderInf>(retailChnlOrderInfList);
    		return RetailChnlOrderInfPage;
     }

	@Override
	public PageInfo<RetailChnlOrderInf> getRetailChnlOrderInf(int startNum, int pageSize, RetailChnlOrderInf retailChnlOrderInf) throws Exception {
		PageHelper.startPage(startNum, pageSize);
		List<RetailChnlOrderInf> retailChnlOrderInfList = retailChnlOrderInfMapper.getRetailChnlOrderInfList(retailChnlOrderInf);
		for (RetailChnlOrderInf RetailChnlOrderInf2 : retailChnlOrderInfList) {
			if(!StringUtil.isNullOrEmpty(RetailChnlOrderInf2.getRechargeState()))
				RetailChnlOrderInf2.setRechargeState(TelRechargeConstants.providerOrderRechargeState.findByCode(RetailChnlOrderInf2.getRechargeState()));
		}
		PageInfo<RetailChnlOrderInf> RetailChnlOrderInfPage = new PageInfo<RetailChnlOrderInf>(retailChnlOrderInfList);
		return RetailChnlOrderInfPage;
	}

//	@Override
//	public List<RetailChnlOrderInfUpload> getRetailChnlOrderInfListToUpload(RetailChnlOrderInf order) {
//		List<RetailChnlOrderInfUpload> list = retailChnlOrderInfMapper.getRetailChnlOrderInfListToUpload(order);
//		for (RetailChnlOrderInfUpload RetailChnlOrderInf2 : list) {
//			if(!StringUtil.isNullOrEmpty(RetailChnlOrderInf2.getRechargeState()))
//				RetailChnlOrderInf2.setRechargeState(TelRechargeConstants.providerOrderRechargeState.findByCode(RetailChnlOrderInf2.getRechargeState()));
//		}
//		return list;
//	}

	@Override
	public RetailChnlOrderInf getRetailChnlOrderInfCount(RetailChnlOrderInf order) {
		return retailChnlOrderInfMapper.getRetailChnlOrderInfCount(order);
	}

	public void doTelRechargeBackNotify(RetailChnlInf retailChnlInf,RetailChnlOrderInf retailChnlOrderInf,ProviderOrderInf telProviderOrderInf) {
     	if(telProviderOrderInf !=null){
     		if(TeleConstants.ProviderRechargeState.RECHARGE_STATE_3.getCode().equals(telProviderOrderInf.getRechargeState())
					|| TeleConstants.ProviderRechargeState.RECHARGE_STATE_9.getCode().equals(telProviderOrderInf.getRechargeState())){

				AccountRefundReqVo req=new AccountRefundReqVo();
				req.setTransId(TransCode.CW11.getCode());
				req.setTransChnl(TransChnl.CHANNEL40011001.toString());
				req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
				req.setUserChnlId(retailChnlInf.getChannelId());
				req.setUserType(UserType.TYPE400.getCode());
				req.setOrgItfPrimaryKey(retailChnlOrderInf.getItfPrimaryKey()); //交易流水

				AccountTxnVo vo=new AccountTxnVo();
				vo.setTxnBId(SpecAccountTypeEnum.B06.getbId());
				vo.setTxnAmt(retailChnlOrderInf.getTxnAmt());
				vo.setUpLoadAmt(retailChnlOrderInf.getTxnAmt());
				List list=new ArrayList();
				list.add(vo);
				req.setTransList(list);

				req.setDmsRelatedKey(telProviderOrderInf.getRegOrderId());
				try {
					BaseResult result = accountTransactionFacade.executeRefund(req);

					if(result !=null && "00".equals(result.getCode())){
						telProviderOrderInf.setPayState(TeleConstants.ChannelOrderPayStat.ORDER_PAY_2.getCode()); //已退款款
						telProviderOrderInf.setItfPrimaryKey(String.valueOf(result.getObject()));
						providerOrderInfService.updateById(telProviderOrderInf);

						retailChnlOrderInf.setOrderStat(TeleConstants.ChannelOrderPayStat.ORDER_PAY_2.getCode()); //已扣款
						this.updateById(retailChnlOrderInf);
					}
				}catch (Exception ex){
					logger.error("##话费充值失败，回调订单退款接口失败-->{}",ex);
				}
			}
		}
		if ("0".equals(retailChnlOrderInf.getNotifyFlag())) {
			try {
				//异步通知供应商
				TeleRespVO respVo = new TeleRespVO();
				respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
				respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
				respVo.setPayState(retailChnlOrderInf.getOrderStat());
				respVo.setRechargeState(telProviderOrderInf.getRechargeState()); //充值状态
				if (telProviderOrderInf.getOperateTime() != null) {
					respVo.setOperateTime(DateUtil.COMMON_FULL.getDateText(new Date(telProviderOrderInf.getOperateTime())));
				}
				respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date(retailChnlOrderInf.getCreateTime()))); //操作时间
				respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
				respVo.setItemNum(retailChnlOrderInf.getItemNum());
				respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
				respVo.setChannelId(retailChnlOrderInf.getChannelId());
				//respVo.setChannelToken(retailChnlInf.getChannelCode());
				respVo.setV(retailChnlOrderInf.getAppVersion());
				respVo.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
				respVo.setSubErrorCode(telProviderOrderInf.getResv1());
				if (TeleConstants.ReqMethodCode.R1.getCode().equals(retailChnlOrderInf.getRechargeType())) {
					respVo.setMethod(TeleConstants.ReqMethodCode.R1.getValue());
				} else if (TeleConstants.ReqMethodCode.R2.getCode().equals(retailChnlOrderInf.getRechargeType())) {
					respVo.setMethod(TeleConstants.ReqMethodCode.R2.getValue());
				}
				String psotToken = MD5SignUtils.genSign(respVo, "key", retailChnlInf.getChannelKey(), new String[]{"sign", "serialVersionUID"}, null);
				respVo.setSign(psotToken);

				//修改通知后 分销商的处理状态
				logger.info("##发起分销商回调[{}],返回参数:[{}]", retailChnlOrderInf.getNotifyUrl(), JSONObject.toJSONString(ResultsUtil.success(respVo)));
				String result = HttpClientUtil.sendPostReturnStr(retailChnlOrderInf.getNotifyUrl(), JSONObject.toJSONString(ResultsUtil.success(respVo)));
				if (result != null && "SUCCESS ".equals(result.toUpperCase())) {
					retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_3.getCode());
				} else {
					retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_2.getCode());
				}

			} catch (Exception e) {
				logger.error("##话费充值失败，回调分销商异常-->{}", e);
			}
			this.updateById(retailChnlOrderInf);
		}
	}

	/**
	 * 商户消费扣款
	 * @param retailChnlInf
	 * @param retailChnlOrderInf
	 * @param providerOrderInf 供应商订单
	 * @return
	 */
	public boolean doRetailCustomerToMchnt(RetailChnlInf retailChnlInf,RetailChnlOrderInf retailChnlOrderInf,ProviderOrderInf providerOrderInf) throws Exception{

		AccountConsumeReqVo req=new AccountConsumeReqVo();
		req.setTransId(TransCode.MB10.getCode());
		req.setTransChnl(TransChnl.CHANNEL40011001.toString());
		req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		req.setUserChnlId(retailChnlInf.getChannelId());
		req.setUserType(UserType.TYPE400.getCode());
		req.setDmsRelatedKey(retailChnlOrderInf.getChannelOrderId());
		req.setPriBId(SpecAccountTypeEnum.B06.getbId());

		AccountTxnVo vo=new AccountTxnVo();
		vo.setTxnAmt(retailChnlOrderInf.getTxnAmt()); //订单的交易金额是 订单金额 （元转分）* 折扣率
		vo.setUpLoadAmt(AmountUtil.RMBYuanToCent(retailChnlOrderInf.getPayAmt()));  //
		vo.setTxnBId(SpecAccountTypeEnum.B06.getbId());
		List transList=new ArrayList();
		transList.add(vo);
		req.setTransList(transList);

		req.setTransDesc("分销商消费");
		req.setTransNumber(1);
		req.setMchntCode(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,RedisDictKey.zlqf_mchnt_code));
		req.setTargetMchtCode(providerOrderInf.getProviderId());
		BaseResult result=accountTransactionFacade.executeConsume(req);
		if(result !=null && "00".equals(result.getCode())){
			retailChnlOrderInf.setOrderStat(TeleConstants.ChannelOrderPayStat.ORDER_PAY_1.getCode()); //已扣款
			retailChnlOrderInf.setItfPrimaryKey(String.valueOf(result.getObject()));
			this.updateById(retailChnlOrderInf);

			providerOrderInf.setPayState(TeleConstants.ChannelOrderPayStat.ORDER_PAY_1.getCode()); //已扣款
			return providerOrderInfService.save(providerOrderInf); //保存供应商订单

		}else{
			logger.info("#分销商消费扣款失败：{}",JSONArray.toJSONString(result));
			throw new RuntimeException();
		}
	}
}
