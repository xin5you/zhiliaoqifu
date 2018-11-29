package com.cn.thinkx.ecom.front.api.phoneRecharge.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeGetReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeGetResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.util.PhoneRechargeSignUtli;

@Configuration
public class PhoneRechargeGetReqValid {

	private static Logger logger = LoggerFactory.getLogger(PhoneRechargeGetReqValid.class);
	
	@Autowired
	private PhoneRechargeSignUtli phoneRechargeSignUtli;
	
	public  boolean findPhoneRechargeOrderReqValid(PhoneRechargeGetReq req,PhoneRechargeGetResp resp) {
		
		logger.info("手机充值订单查询请求参数[{}]", req.toString());
		
		if (StringUtil.isNullOrEmpty(req.getChannelOrderNo())) {
			resp.setMsg("外部订单号为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(req.getReqChannel())) {
			resp.setMsg("请求渠道为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(req.getTimestamp())) {
			resp.setMsg("时间戳为空为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(req.getSign())) {
			resp.setMsg("签名为空");
			return true;
		}
		
		if (!phoneRechargeSignUtli.genSign(req).equals(req.getSign())) {
			resp.setMsg("签名错误");
			return true;
		}
		
		if (phoneRechargeSignUtli.isSignExpired(System.currentTimeMillis(), req.getTimestamp())) {
			resp.setMsg("签名过期");
			return true;
		}
		return false;
	}
}
