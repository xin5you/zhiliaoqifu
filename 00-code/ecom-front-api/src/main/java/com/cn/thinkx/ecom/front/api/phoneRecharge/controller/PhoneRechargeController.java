package com.cn.thinkx.ecom.front.api.phoneRecharge.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.bm001.api.constants.BMConstants;
import com.cn.thinkx.ecom.bm001.api.service.BMOrderService;
import com.cn.thinkx.ecom.common.constants.Constants.PhoneRechargeTransStat;
import com.cn.thinkx.ecom.common.constants.Constants.RechargeState;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.phoneRecharge.constants.PhoneRechargeConstants;
import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.service.PhoneRechargeService;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;

@RestController
@RequestMapping(value = "ecom/recharge")
public class PhoneRechargeController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PhoneRechargeService phoneRechargeService;
	
	@Autowired
	private BMOrderService bmOrderService;
	
	 @Autowired
	 private JedisClusterUtils jedisClusterUtils;
	
	@Value("${BM_NOTIFY_URL}")
	private String BM_NOTIFY_URL;
	
	
	@RequestMapping(value = "/phoneRecharge", method = RequestMethod.POST)
	@ResponseBody
	public String phoneRecharge(HttpServletRequest req, HttpServletResponse resp,@RequestBody JSONObject  obj) {
		
		PhoneRechargeResp prResp = new PhoneRechargeResp();
		
		logger.info("话费充值 请求参数  [{}]",obj);
		
		PhoneRechargeOrder rechargeOrde = JSONObject.parseObject(obj.toString(), PhoneRechargeOrder.class);
		
		try {
			String PHONE_RECHARGE_SWITCH = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, PhoneRechargeConstants.PHONE_RECHARGE_SWITCH);
			if (PHONE_RECHARGE_SWITCH.equals("0")) {
				String accessToken = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, BMConstants.BM_ACCESS_TOKEN);
				BaseResult result = bmOrderService.handleHbkToBMPayBill(rechargeOrde.getPhone(), rechargeOrde.getTelephoneFace(), rechargeOrde.getrId(), BM_NOTIFY_URL, accessToken);
				if (ExceptionEnum.SUCCESS_CODE.equals(result.getCode())) {
					prResp.setCode(ExceptionEnum.SUCCESS_CODE);
					prResp.setMsg(ExceptionEnum.SUCCESS_MSG);
					OrderDetailInfo orderDetailInfo = (OrderDetailInfo) result.getObject();
					rechargeOrde.setSupplierOrdeNo(orderDetailInfo.getBillId());
					rechargeOrde.setTransStat(PhoneRechargeTransStat.TransStat4.getCode());
				} else {
					rechargeOrde.setTransStat(PhoneRechargeTransStat.TransStat3.getCode());
				}
			}
			if (PHONE_RECHARGE_SWITCH.equals("1")) {
				rechargeOrde.setTransStat(PhoneRechargeTransStat.TransStat3.getCode());
			}
			int i = phoneRechargeService.updatePhoneRechargeOrder(rechargeOrde);
			if (i != 1) {
				logger.error(" ## 修改订单表信息出错 ");
			}
			
			if(PhoneRechargeTransStat.TransStat3.getCode().equals(rechargeOrde.getTransStat())){
				phoneRechargeService.doPhoneRechargeRefund(rechargeOrde);
			}
		} catch (Exception e) {
			logger.error(" ##  话费充值出错 ", e);
			prResp.setCode(ExceptionEnum.ERROR_CODE);
			prResp.setMsg(ExceptionEnum.ERROR_MSG);
			return JSONObject.toJSONString(prResp);
		}
		return JSONObject.toJSONString(prResp);
	}
	

	@RequestMapping(value = "/mobileRechargeBack", method = RequestMethod.POST)
	public String mobileRechargeBack(HttpServletRequest req) {
		String rechargeStat = req.getParameter("recharge_state");
		String outerTid = req.getParameter("outer_tid");
		String timestamp  = req.getParameter("timestamp");
		String userId = req.getParameter("user_id");
		String tid = req.getParameter("tid");
		
		logger.info("话费充值订单回调返回参数，订单充值状态[{}]，外部订单编号[{}]，消息产生时间[{}]，授权的直销商编号[{}]，平台订单编号[{}]",
				rechargeStat,outerTid,timestamp,userId,tid);
		
		if (StringUtil.isNullOrEmpty(outerTid)) {
			return null;
		}
		if (StringUtil.isNullOrEmpty(rechargeStat)) {
			return null;
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
			int i = phoneRechargeService.updatePhoneRechargeOrder(rechargeOrde);
			
			if (i != 1) {
				
			}
			if(PhoneRechargeTransStat.TransStat3.getCode().equals(rechargeOrde.getTransStat())){
				PhoneRechargeOrder refundOrder = phoneRechargeService.getPhoneRechargeOrderByPrimaryKey(outerTid);
				phoneRechargeService.doPhoneRechargeRefund(refundOrder);
			}
		} catch (Exception e) {
			logger.error(" ## 更新话费充值订单异常-->", e);
		}
		return "success";
	}
}
