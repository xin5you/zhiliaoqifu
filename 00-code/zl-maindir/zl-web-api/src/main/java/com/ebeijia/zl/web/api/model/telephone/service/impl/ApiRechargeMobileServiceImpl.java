package com.ebeijia.zl.web.api.model.telephone.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.core.activemq.service.MQProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleReqVO;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespDomain;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.ebeijia.zl.web.api.model.telephone.service.ApiRechargeMobileService;
import com.ebeijia.zl.web.api.model.telephone.valid.ApiRechangeMobileValid;

@Service("apiRechargeMobileService")
public class ApiRechargeMobileServiceImpl implements ApiRechargeMobileService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("retailChnlInfFacade")
	private RetailChnlInfFacade retailChnlInfFacade;

	@Autowired
	@Qualifier("retailChnlOrderInfFacade")
	private RetailChnlOrderInfFacade retailChnlOrderInfFacade;

	@Autowired
	@Qualifier("mqProducerService")
	private MQProducerService mqProducerService;

	@Autowired
	@Qualifier("providerOrderInfFacade")
	private ProviderOrderInfFacade providerOrderInfFacade;

	@Autowired
	private ApiRechangeMobileValid apiRechangeMobileValid;

	@SuppressWarnings("rawtypes")
	@Override
	public BaseResult payment(TeleReqVO reqVo) throws Exception {
		// step1:判断请求的数据是否完整
		if (!apiRechangeMobileValid.rechargeValueValid(reqVo)) {
			return ResultsUtil.error("110101", "参数不合法");
		}

		// step2:验签
		RetailChnlInf retailChnlInf = null;
		try {
			retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(reqVo.getChannelId());
		} catch (Exception e) {
			logger.error("## 查询分销商异常", e);
		}
		try {
			if (retailChnlInf == null || !apiRechangeMobileValid.rechargeSignValid(reqVo, retailChnlInf.getChannelKey())) {
				return ResultsUtil.error("110102", "token验证失败");
			}
		} catch (ParseException e) {
			logger.error("## 分销商签名验证异常 key[{}]", retailChnlInf.getChannelKey(), e);
		}

		RetailChnlOrderInf retailChnlOrderInf = null;
		if (StringUtil.isNotEmpty(reqVo.getOuterTid())) {
			try {
				retailChnlOrderInf = retailChnlOrderInfFacade.getRetailChnlOrderInfByOuterId(reqVo.getOuterTid(), reqVo.getChannelId());
			} catch (Exception e) {
				logger.error("## 查询外部订单[{}]异常", reqVo.getOuterTid(), e);
			}
		}

		if (retailChnlOrderInf == null) {
			retailChnlOrderInf = new RetailChnlOrderInf();
			retailChnlOrderInf.setChannelId(reqVo.getChannelId());
			retailChnlOrderInf.setOuterTid(reqVo.getOuterTid());
			retailChnlOrderInf.setRechargePhone(reqVo.getRechargePhone());
			retailChnlOrderInf.setRechargeType(ReqMethodCode.findByValue(reqVo.getMethod()).getCode());
			retailChnlOrderInf.setRechargeValue(new BigDecimal(reqVo.getRechargeAmount()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 充值的金额
			retailChnlOrderInf.setProductId(reqVo.getProductId());
			retailChnlOrderInf.setNotifyUrl(reqVo.getCallback());
			retailChnlOrderInf.setAppVersion(reqVo.getV());
		} else {
			return ResultsUtil.error("110103", "重复订单请求");
		}

		// step4:获取运营商类型  1:移动，2：联通，3：电信
	   //	String operId = TelePhoneCheckUtils.isChinaMobilePhoneNum(reqVo.getRechargePhone());
		
		// step5:获取归属地
		// String areaName=TelePhoneCheckUtils.getPhoneMessage(reqVo.getRechargePhone());

		// step5:return 创建订单
		BaseResult<TeleRespVO> respTxn = null;
		try {
			respTxn = retailChnlOrderInfFacade.proChannelOrder(retailChnlOrderInf, null, "ALL");
		} catch (Exception ex) {
			logger.error("## 创建充值订单{}异常", retailChnlOrderInf.toString(), ex);
		}
		logger.info("创建订单返回respTxn-->{}",JSONObject.toJSONString(respTxn));
		if (respTxn == null || !"00".equals(respTxn.getCode())) {
			RetailChnlOrderInf orderInf = null;
			try {
				orderInf = retailChnlOrderInfFacade.getRetailChnlOrderInfByOuterId(reqVo.getOuterTid(), reqVo.getChannelId());
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
				respTxn.setObject(respVo);
			} else {
				return ResultsUtil.error("110603", "订单创建失败");
			}
		}

		if (respTxn != null && "00".equals(respTxn.getCode())) {
			// 同步返回数据验签
			TeleRespVO voResp = respTxn.getObject();
			voResp.setChannelId(reqVo.getChannelId());
			voResp.setChannelToken(reqVo.getChannelToken());
			voResp.setV(reqVo.getV());
			voResp.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
			voResp.setMethod(reqVo.getMethod());
			String retSign = MD5SignUtils.genSign(voResp, "key", retailChnlInf.getChannelKey(), new String[] { "sign", "serialVersionUID" }, null);
			voResp.setSign(retSign);
			respTxn.setObject(voResp);
		}

		// 发送消息
		try {
			retailChnlOrderInfFacade.doRechargeMobileMsg(respTxn.getObject().getChannelOrderId());
		} catch (Exception ex) {
			logger.error("## 订单[{}]发送充值队列消息异常{},error :{}", respTxn.getObject().getChannelOrderId(), ex);
		}
		
		return respTxn;
	}

}
