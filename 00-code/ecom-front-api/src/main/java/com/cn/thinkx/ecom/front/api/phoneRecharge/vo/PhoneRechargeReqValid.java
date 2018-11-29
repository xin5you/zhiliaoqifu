package com.cn.thinkx.ecom.front.api.phoneRecharge.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.phoneRecharge.req.PhoneRechargeReq;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeResp;
import com.cn.thinkx.ecom.front.api.phoneRecharge.util.PhoneRechargeSignUtli;

@Configuration
public class PhoneRechargeReqValid {
	
	
	private static Logger logger = LoggerFactory.getLogger(PhoneRechargeReqValid.class);
	
	@Autowired
	private PhoneRechargeSignUtli phoneRechargeSignUtli;
	
	public  boolean phoneRechargeReqValid(PhoneRechargeReq prReq,PhoneRechargeResp resp) {
		
		logger.info("话费充值请求参数[{}]", prReq.toString());
		
		if (StringUtil.isNullOrEmpty(prReq.getChannelOrderNo())) {
			resp.setMsg("渠道订单号为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(prReq.getAccessToken())) {
			resp.setMsg("accessToken为空");
			return true;
		}
	
		if (StringUtil.isNullOrEmpty(prReq.getPhone())) {
			resp.setMsg("手机号码为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(prReq.getTelephoneFace())) {
			resp.setMsg("话费面额为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(prReq.getOrderType())) {
			resp.setMsg("订单类型为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(prReq.getReqChannel())) {
			resp.setMsg("请求渠道号为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(prReq.getTimestamp())) {
			resp.setMsg("时间戳为空为空");
			return true;
		}
		
		if (StringUtil.isNullOrEmpty(prReq.getSign())) {
			resp.setMsg("签名为空");
			return true;
		}
		
		if (!phoneRechargeSignUtli.genSign(prReq).equals(prReq.getSign())) {
			resp.setMsg("签名错误");
			return true;
		}
		
		if (phoneRechargeSignUtli.isSignExpired(System.currentTimeMillis(), prReq.getTimestamp())) {
			resp.setMsg("签名过期");
			return true;
		}
		return false;
	}

}
