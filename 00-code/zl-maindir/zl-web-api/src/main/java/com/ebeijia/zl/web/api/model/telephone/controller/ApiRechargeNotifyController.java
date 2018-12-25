package com.ebeijia.zl.web.api.model.telephone.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.web.api.model.telephone.util.RequestJsonUtil;
import com.ebeijia.zl.web.api.model.telephone.vo.BMRechargeStateChangeVO;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;
import com.qianmi.open.api.response.BmRechargeMobilePayBillResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.core.redis.utils.RedisDictProperties;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.ebeijia.zl.web.api.model.telephone.vo.HKbCallBackResult;

@RestController
@RequestMapping("/api/recharge/notify")
public class ApiRechargeNotifyController {
	
	private Logger logger = LoggerFactory.getLogger(ApiRechargeNotifyController.class);
	
	@Autowired
	private RetailChnlInfFacade retailChnlInfFacade;
	
	@Autowired
	private RetailChnlOrderInfFacade retailChnlOrderInfFacade;
	
	@Autowired
	private ProviderOrderInfFacade providerOrderInfFacade;


	/**
	 * 手机充值 立方回调
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bm001CallBack",method=RequestMethod.POST)
	@ResponseBody
	public String bm001CallBack(HttpServletRequest request) throws Exception {

		BMRechargeStateChangeVO rechargeStateChangeVO=new BMRechargeStateChangeVO();
		rechargeStateChangeVO.setExtData(request.getParameter("ext_data"));
		rechargeStateChangeVO.setRechargeState(request.getParameter("recharge_state"));
		rechargeStateChangeVO.setOuterTid(request.getParameter("outer_tid"));
		rechargeStateChangeVO.setSign(request.getParameter("sign"));
		rechargeStateChangeVO.setTimestamp(request.getParameter("timestamp"));
		rechargeStateChangeVO.setUserId(request.getParameter("user_id"));
		rechargeStateChangeVO.setTid(request.getParameter("tid"));

		logger.info("bm001CallBack rechargeStateChangeVO-->{}",JSONObject.toJSONString(rechargeStateChangeVO));
		//供应商充值状态
		ProviderOrderInf providerOrderInf=providerOrderInfFacade.getProviderOrderInfById(rechargeStateChangeVO.getOuterTid());
		if("1".equals(rechargeStateChangeVO.getRechargeState())){
			providerOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_1.getCode());
		}else{
			providerOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_3.getCode());
		}
		RetailChnlOrderInf retailChnlOrderInf=retailChnlOrderInfFacade.getRetailChnlOrderInfById(providerOrderInf.getChannelOrderId());
		RetailChnlInf retailChnlInf =retailChnlInfFacade.getRetailChnlInfById(retailChnlOrderInf.getChannelId());

		//回调通知分銷商
		retailChnlOrderInfFacade.doTelRechargeBackNotify(retailChnlInf,retailChnlOrderInf,providerOrderInf);
		return "success";
	}
	
	/**
	 * 手机充值 汇卡宝商城回调
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bmHKbCallBack",method=RequestMethod.POST)
	@ResponseBody
	public String bmHKbCallBack(HttpServletRequest request) throws Exception {
		logger.info("bmHKbCallBack getRequestJsonString-->{}",RequestJsonUtil.getRequestJsonString(request));
		return "success";
	}
	
}
