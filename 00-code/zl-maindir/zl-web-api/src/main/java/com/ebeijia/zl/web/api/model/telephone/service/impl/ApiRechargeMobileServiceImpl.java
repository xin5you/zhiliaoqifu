package com.ebeijia.zl.web.api.model.telephone.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.activemq.core.service.RechargeMobileProducerService;
import com.cn.thinkx.wecard.facade.telrecharge.model.TelChannelInf;
import com.cn.thinkx.wecard.facade.telrecharge.model.TelChannelOrderInf;
import com.cn.thinkx.wecard.facade.telrecharge.resp.TeleReqVO;
import com.cn.thinkx.wecard.facade.telrecharge.resp.TeleRespDomain;
import com.cn.thinkx.wecard.facade.telrecharge.resp.TeleRespVO;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlInfFacade;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.cn.thinkx.wecard.facade.telrecharge.service.ProviderOrderInfFacade;
import com.cn.thinkx.wecard.facade.telrecharge.utils.ResultsUtil;
import com.cn.thinkx.wecard.facade.telrecharge.utils.TeleConstants;
import com.cn.thinkx.wecard.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.api.model.telephone.service.ApiRechargeMobileService;
import com.ebeijia.zl.web.api.model.telephone.util.TelePhoneCheckUtils;
import com.ebeijia.zl.web.api.model.telephone.valid.ApiRechangeMobileValid;

@Service("apiRechargeMobileService")
public class ApiRechargeMobileServiceImpl implements ApiRechargeMobileService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("telChannelInfFacade")
	private RetailChnlInfFacade telChannelInfFacade;

	@Autowired
	@Qualifier("telChannelOrderInfFacade")
	private RetailChnlOrderInfFacade telChannelOrderInfFacade;

	@Autowired
	@Qualifier("rechargeMobileProducerService")
	private RechargeMobileProducerService rechargeMobileProducerService;

	@Autowired
	@Qualifier("telProviderOrderInfFacade")
	private ProviderOrderInfFacade telProviderOrderInfFacade;

	@SuppressWarnings("rawtypes")
	@Override
	public TeleRespDomain payment(TeleReqVO reqVo) throws Exception {
		// step1:判断请求的数据是否完整
		if (!ApiRechangeMobileValid.rechargeValueValid(reqVo)) {
			return ResultsUtil.error("110101", "参数不合法");
		}

		// step2:验签
		TelChannelInf telChannelInf = null;
		try {
			telChannelInf = telChannelInfFacade.getTelChannelInfById(reqVo.getChannelId());
		} catch (Exception e) {
			logger.error("## 查询分销商异常", e);
		}
		try {
			if (telChannelInf == null || !ApiRechangeMobileValid.rechargeSignValid(reqVo, telChannelInf.getChannelKey())) {
				return ResultsUtil.error("110102", "token验证失败");
			}
		} catch (ParseException e) {
			logger.error("## 分销商签名验证异常 key[{}]", telChannelInf.getChannelKey(), e);
		}

		TelChannelOrderInf telChannelOrderInf = null;
		if (StringUtil.isNotEmpty(reqVo.getOuterTid())) {
			try {
				telChannelOrderInf = telChannelOrderInfFacade.getTelChannelOrderInfByOuterId(reqVo.getOuterTid(), reqVo.getChannelId());
			} catch (Exception e) {
				logger.error("## 查询外部订单[{}]异常", reqVo.getOuterTid(), e);
			}
		}

		if (telChannelOrderInf == null) {
			telChannelOrderInf = new TelChannelOrderInf();
			telChannelOrderInf.setChannelId(reqVo.getChannelId());
			telChannelOrderInf.setOuterTid(reqVo.getOuterTid());
			telChannelOrderInf.setRechargePhone(reqVo.getRechargePhone());
			telChannelOrderInf.setRechargeType(ReqMethodCode.findByValue(reqVo.getMethod()).getCode());
			telChannelOrderInf.setRechargeValue(new BigDecimal(reqVo.getRechargeAmount()).setScale(3, BigDecimal.ROUND_DOWN)); // 充值的金额
			telChannelOrderInf.setProductId(reqVo.getProductId());
			telChannelOrderInf.setNotifyUrl(reqVo.getCallback());
			telChannelOrderInf.setAppVersion(reqVo.getV());
		} else {
			return ResultsUtil.error("110103", "重复订单请求");
		}

		// step4:获取运营商类型  1:移动，2：联通，3：电信
	   	String operId = TelePhoneCheckUtils.isChinaMobilePhoneNum(reqVo.getRechargePhone());
		
		// step5:获取归属地
		// String areaName=TelePhoneCheckUtils.getPhoneMessage(reqVo.getRechargePhone());

		// step5:return 创建订单
		TeleRespDomain<TeleRespVO> respTxn = null;
		try {
			respTxn = telChannelOrderInfFacade.proChannelOrder(telChannelOrderInf, operId, "ALL");
		} catch (Exception ex) {
			logger.error("## 创建充值订单{}异常", telChannelOrderInf.toString(), ex);
		}
		logger.info("创建订单返回respTxn-->{}",JSONObject.toJSONString(respTxn));
		if (respTxn == null || !"00".equals(respTxn.getCode())) {
			TelChannelOrderInf orderInf = null;
			try {
				orderInf = telChannelOrderInfFacade.getTelChannelOrderInfByOuterId(reqVo.getOuterTid(), reqVo.getChannelId());
			} catch (Exception e) {
				logger.error("## 查询外部订单[{}]异常 channelId[{}]", reqVo.getOuterTid(), reqVo.getChannelId(), e);
			}
			if (orderInf != null) {
				TeleRespVO respVo = new TeleRespVO();
				respVo.setSaleAmount(orderInf.getPayAmt().toString());
				respVo.setChannelOrderId(orderInf.getChannelOrderId());
				respVo.setPayState(orderInf.getOrderStat());
				respVo.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode());
				respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date()));
				respVo.setFacePrice(orderInf.getRechargeValue().toString());
				respVo.setItemNum(orderInf.getItemNum());
				respVo.setOuterTid(orderInf.getOuterTid());
				respTxn.setCode("00");
				respTxn.setData(respVo);
			} else {
				return ResultsUtil.error("110603", "订单创建失败");
			}
		}

		if (respTxn != null && "00".equals(respTxn.getCode())) {
			// 同步返回数据验签
			TeleRespVO voResp = respTxn.getData();
			voResp.setChannelId(reqVo.getChannelId());
			voResp.setChannelToken(reqVo.getChannelToken());
			voResp.setV(reqVo.getV());
			voResp.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
			voResp.setMethod(reqVo.getMethod());
			String retSign = MD5SignUtils.genSign(voResp, "key", telChannelInf.getChannelKey(), new String[] { "sign", "serialVersionUID" }, null);
			voResp.setSign(retSign);
			respTxn.setData(voResp);
		}

		// 发送消息
		try {
			telChannelOrderInfFacade.doRechargeMobileMsg(respTxn.getData().getChannelOrderId());
		} catch (Exception ex) {
			logger.error("## 订单[{}]发送充值队列消息异常", respTxn.getData().getChannelOrderId(), ex);
		}
		
		return respTxn;
	}

}
