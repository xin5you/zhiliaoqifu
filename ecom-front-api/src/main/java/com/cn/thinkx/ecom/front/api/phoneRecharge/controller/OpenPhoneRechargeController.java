package com.cn.thinkx.ecom.front.api.phoneRecharge.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.common.constants.Constants.PhoneRechargeTransStat;
import com.cn.thinkx.ecom.common.constants.Constants.RechargeState;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeGetReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeGetResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.service.PhoneRechargeService;
import com.cn.thinkx.ecom.front.api.phoneRecharge.vo.PhoneRechargeGetReqValid;
import com.cn.thinkx.ecom.front.api.phoneRecharge.vo.PhoneRechargeReqValid;

@RestController
@RequestMapping(value = "hkb/recharge")
public class OpenPhoneRechargeController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PhoneRechargeReqValid phoneRechargeReqValid;
	
	@Autowired
	private PhoneRechargeGetReqValid phoneRechargeGetReqValid;
	
	@Autowired
	private PhoneRechargeService phoneRechargeService;

	@RequestMapping(value = "/phoneRecharge", method = RequestMethod.POST)
	@ResponseBody
	public String  phoneRecharge(HttpServletRequest req, HttpServletResponse resp,@RequestBody JSONObject  obj) {
		PhoneRechargeResp prResp = new PhoneRechargeResp();
		
		logger.info("话费充值请求参数 [{}]",obj);
		
		PhoneRechargeReq prReq = JSONObject.parseObject(obj.toString(), PhoneRechargeReq.class);
		if (phoneRechargeReqValid.phoneRechargeReqValid(prReq, prResp)) {// 校验参数
			logger.error(" ## 话费充值校验参数出错,参数[{}],返回参数[{}]",JSONObject.toJSONString(prReq),JSONObject.toJSONString(prResp));
			prResp.setCode(ExceptionEnum.ERROR_CODE);
			return JSONObject.toJSONString(resp);
		}
		
		String result = null;
		
		try{
			result = phoneRechargeService.handlePhoneRechargeOrder(prReq, prResp);
		} catch(Exception e){
			logger.error(" ## 话费充值出错",e);
			prResp.setCode(ExceptionEnum.ERROR_CODE);
			prResp.setMsg(ExceptionEnum.ERROR_MSG);
			return JSONObject.toJSONString(prResp);
		}
		return result;
	}
	
	@RequestMapping(value = "/mobileRechargeBack", method = RequestMethod.POST)
	@ResponseBody
	public String mobileRechargeBack(HttpServletRequest req) {
		String rechargeStat = req.getParameter("recharge_state");
		String outerTid = req.getParameter("outer_tid");
		String timestamp  = req.getParameter("timestamp");
		String userId = req.getParameter("user_id");
		String tid = req.getParameter("tid");
		
		logger.info("话费充值订单回调返回参数，订单充值状态[{}]，外部订单编号[{}]，消息产生时间[{}]，授权的直销商编号[{}]，平台订单编号[{}]",
				rechargeStat,outerTid,timestamp,userId,tid);
		
		if (StringUtil.isNullOrEmpty(outerTid)) {
			logger.error(" ## 话费充值回调返回 ： 外部订单编号为空");
		}
		if (StringUtil.isNullOrEmpty(rechargeStat)) {
			logger.error(" ## 话费充值回调返回 ： 订单充值状态为空");
		}
		PhoneRechargeOrder rechargeOrde = new PhoneRechargeOrder();
		rechargeOrde.setrId(outerTid);
		if (RechargeState.RechargeState01.getCode().equals(rechargeStat)) {
			rechargeOrde.setTransStat(PhoneRechargeTransStat.TransStat2.getCode());
		}
		if (RechargeState.RechargeState09.getCode().equals(rechargeStat)) {
			rechargeOrde.setTransStat(PhoneRechargeTransStat.TransStat3.getCode());
		}

		try {
			phoneRechargeService.doPhoneRechargeOrder(rechargeOrde);
		} catch (Exception e) {
			logger.error(" ## 更新话费充值订单异常-->", e);
		}
		return "success";
	}
	
	@RequestMapping(value = "/getPhoneRechargeOrder", method = RequestMethod.POST)
	@ResponseBody
	public String getPhoneRechargeOrder(HttpServletRequest req, HttpServletResponse resp,@RequestBody JSONObject  obj){

		PhoneRechargeGetResp prResp = new PhoneRechargeGetResp();
		
		logger.info("手机充值订单查询请求参数 [{}]",obj);
		
		PhoneRechargeGetReq prReq = JSONObject.parseObject(obj.toString(), PhoneRechargeGetReq.class);
		if (phoneRechargeGetReqValid.findPhoneRechargeOrderReqValid(prReq, prResp)) {// 校验参数
			prResp.setCode(ExceptionEnum.ERROR_CODE);
			return JSONObject.toJSONString(prResp);
		}
		String result = null;
		try {
			
			result = phoneRechargeService.handleGetPhoneRechargeOrder(prReq, prResp);
		} catch (Exception e) {
			logger.error(" ## 渠道为[{}]订单号[{}]查询手机充值订单异常-->", prReq.getReqChannel(), prReq.getChannelOrderNo(), e);
		}
		return result;
	}
	
	@RequestMapping(value = "/getPhoneInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getPhoneInfo(HttpServletRequest req, HttpServletResponse resp,@RequestBody JSONObject  obj){
		String phone = obj.getString("phone");
		String result = null;
		try {
			result = phoneRechargeService.doGetPhoneInfo(phone);
		} catch (Exception e) {
			logger.error("  ##  查询手机信息异常-->", e);
		}
		return result;
	}
}
