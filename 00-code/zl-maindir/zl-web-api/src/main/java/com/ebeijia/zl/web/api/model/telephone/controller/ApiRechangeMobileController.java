package com.ebeijia.zl.web.api.model.telephone.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.ebeijia.zl.core.activemq.service.MQProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleReqVO;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespDomain;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.utils.ResultsUtil;
import com.ebeijia.zl.web.api.model.telephone.service.ApiRechargeMobileService;
import com.ebeijia.zl.web.api.model.telephone.valid.ApiRechangeMobileValid;

@RestController
@RequestMapping("/api/recharge/mobile")
public class ApiRechangeMobileController {

	private Logger logger = LoggerFactory.getLogger(ApiRechangeMobileController.class);

	@Autowired
	@Qualifier("apiRechargeMobileService")
	private ApiRechargeMobileService apiRechargeMobileService;

	@Autowired
	@Qualifier("RetailChnlInfFacade")
	private RetailChnlInfFacade RetailChnlInfFacade;

	@Autowired
	@Qualifier("RetailChnlOrderInfFacade")
	private RetailChnlOrderInfFacade RetailChnlOrderInfFacade;

	@Autowired
	@Qualifier("mqProducerService")
	private MQProducerService mqProducerService;

	@Autowired
	@Qualifier("ProviderOrderInfFacade")
	private ProviderOrderInfFacade ProviderOrderInfFacade;
	
	@Autowired
	private ApiRechangeMobileValid apiRechangeMobileValid;

	/**
	 * 分销商发起手机充值
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	@ResponseBody
	public TeleRespDomain payment(HttpServletRequest request, TeleReqVO reqVo) {
		logger.info("分销商手机充值参数-->{}",JSONObject.toJSON(reqVo));
		try {
			return apiRechargeMobileService.payment(reqVo);
		} catch (Exception e) {
			logger.error("## 手机充值接口调用异常 订单{}", reqVo.toString(), e);
		}
		return ResultsUtil.error("110101", "参数不合法");
	}

	/**
	 * 分销商发起的充值订单状态
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getOrder", method = RequestMethod.POST)
	@ResponseBody
	public TeleRespDomain getOrder(HttpServletRequest request, TeleReqVO reqVo) {

		try {
			RetailChnlInf retailChnlInf = RetailChnlInfFacade.getRetailChnlInfById(reqVo.getChannelId());
			if (!apiRechangeMobileValid.rechargeSignValid(reqVo, retailChnlInf.getChannelKey())) {
				return ResultsUtil.error("110102", "token验证失败");
			}
			RetailChnlOrderInf retailChnlOrderInf = null;
			if (StringUtil.isNotEmpty(reqVo.getChannelOrderId())) {
				retailChnlOrderInf = RetailChnlOrderInfFacade.getRetailChnlOrderInfById(reqVo.getChannelOrderId());
			} else {
				retailChnlOrderInf = RetailChnlOrderInfFacade.getRetailChnlOrderInfByOuterId(reqVo.getOuterTid(),
						reqVo.getChannelId());
			}

			if (retailChnlOrderInf != null) {
				ProviderOrderInf providerOrderInf = ProviderOrderInfFacade.getTelOrderInfByChannelOrderId(retailChnlOrderInf.getChannelOrderId());
				TeleRespVO respVo = new TeleRespVO();
				respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
				respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
				respVo.setPayState(retailChnlOrderInf.getOrderStat());
				respVo.setRechargeState(providerOrderInf.getRechargeState());
				respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date(retailChnlOrderInf.getCreateTime())));
				if (providerOrderInf.getOperateTime() != null) {
					respVo.setOperateTime(DateUtil.COMMON_FULL.getDateText(new Date(providerOrderInf.getOperateTime())));
				}
				respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
				respVo.setItemNum(retailChnlOrderInf.getItemNum());
				respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
				respVo.setChannelId(reqVo.getChannelId());
				respVo.setChannelToken(reqVo.getChannelToken());
				respVo.setV(reqVo.getV());
				respVo.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
				respVo.setMethod(reqVo.getMethod());
				respVo.setSubErrorCode(providerOrderInf.getResv1());
				String retSign = MD5SignUtils.genSign(respVo, "key", retailChnlInf.getChannelKey(),
						new String[] { "sign", "serialVersionUID" }, null);
				respVo.setSign(retSign);
				return ResultsUtil.success(respVo);
			} else {
				return ResultsUtil.error("110200", "渠道订单不存在");
			}
		} catch (Exception ex) {
			return ResultsUtil.error("110101", "参数不合法");
		}

	}

}
