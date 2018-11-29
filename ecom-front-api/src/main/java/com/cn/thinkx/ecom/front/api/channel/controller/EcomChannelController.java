package com.cn.thinkx.ecom.front.api.channel.controller;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.service.MemberInfService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.front.api.channel.model.ChannelIdent;
import com.cn.thinkx.ecom.front.api.channel.util.EcomChannelSignUtil;
import com.cn.thinkx.ecom.front.api.channel.volid.EcomChannelReqValid;


@RestController
@RequestMapping(value = "ecom/channel")
public class EcomChannelController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MemberInfService memberInfService;

	/**
	 * 渠道接入点
	 * @param req
	 * @param sign 签名
	 * @param channelIdent 业务字段
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/ap")
	public ModelAndView apoint(HttpServletRequest req,
					ChannelIdent channelIdent) throws Exception {
		//测试数据开始
//		SortedMap<String, String> dataMap = new TreeMap<String, String>();
//		dataMap.put("channelCode", Constants.ChannelCode.CHANNEL1.toString());
//		dataMap.put("service", "h5.scene.ecom");
//		dataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
//		dataMap.put("version", "1");
//		dataMap.put("cUserId", "oppGJ00P7dNXpBStrw1hB2lTIUlU");
//		StringBuilder forSign = new StringBuilder();
//		for (String key : dataMap.keySet()) {
//			forSign.append(key).append("=").append(dataMap.get(key)).append("&");
//		}
//		forSign.append("key=").append("YPEgSbuyRcoDV49yHzx4wS4ZeKHFVQA84Hv4IunjHT7");
//		String sign = EcomChannelSignUtil.MD5Encode(forSign.toString());
//		dataMap.put("sign", sign);
//		channelIdent=new ChannelIdent();
//		channelIdent.setChannelCode(dataMap.get("channelCode"));
//		channelIdent.setService(dataMap.get("service"));
//		channelIdent.setTimestamp(dataMap.get("timestamp"));
//		channelIdent.setVersion(dataMap.get("version"));
//		channelIdent.setcUserId(dataMap.get("cUserId"));
//		channelIdent.setSign(dataMap.get("sign"));
		
		logger.info("ap-->[{}]",JSONObject.toJSON(channelIdent));
		BaseResult<Object> resp=ResultsUtil.error("-1", "");
		boolean valid=EcomChannelReqValid.apValid(channelIdent, resp);
		logger.info("valid-->[{}]",valid);
		
		if(valid){
			//注册会员信息
			MemberInf memberInf=memberInfService.RegMemberInfByOpenId(channelIdent.getcUserId());
			logger.info("memberInf-->[{}]",JSONObject.toJSON(memberInf));
			if(memberInf !=null){
				req.getSession().setAttribute(Constants.FRONT_MEMBER_ID_KEY, memberInf.getMembeId());
			}
		}
		return new ModelAndView("redirect:/ecom/hkbstore/index");
	}
}
