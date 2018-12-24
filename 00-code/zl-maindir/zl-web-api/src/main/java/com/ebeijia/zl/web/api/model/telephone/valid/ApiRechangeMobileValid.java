package com.ebeijia.zl.web.api.model.telephone.valid;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import com.ebeijia.zl.common.utils.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.resp.TeleReqVO;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

@Component
public class ApiRechangeMobileValid {

	public  Logger logger = LoggerFactory.getLogger(ApiRechangeMobileValid.class);

	@Autowired
	private  JedisCluster jedisCluster; 

	/**
	 * 分销商请求充值 数据判断
	 * 
	 * @param req
	 * @return false
	 */
	public  boolean rechargeValueValid(TeleReqVO req) {
		logger.info("渠道请求参数{}", JSONObject.toJSON(req));
		
		if (StringUtil.isNullOrEmpty(req.getChannelId())) {
			logger.info("channelId为空");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getV())) {
			logger.info("v为空");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getTimestamp())) {
			logger.info("timestamp为空");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getOuterTid())) {// 充值的外部分销商ID
			logger.info("outerTid为空");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getRechargePhone())) {// 充值的手机号
			logger.info("rechargePhone为空");
			return false;
		}
		if (StringUtil.isNullOrEmpty(req.getRechargeAmount())) { // 充值金额
			logger.info("rechargeAmount为空");
			return false;
		} else {
			Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");
			if (!pattern.matcher(req.getRechargeAmount()).matches()) {
				logger.info("rechargeAmount不是数字");
				return false;
			}
		}
		if (StringUtil.isNullOrEmpty(req.getSign())) { // 签名
			logger.info("sign为空");
			return false;
		}
		return true;
	}

	/**
	 * 分销商请求充值 签名验证
	 * 
	 * @param reqVo
	 * @return false
	 * @throws ParseException
	 */
	public  boolean rechargeSignValid(TeleReqVO reqVo, String signKey) throws ParseException {
		Date reqTime = DateUtil.COMMON_FULL.getFormat().parse(reqVo.getTimestamp());
		Date currDate = new Date();
/*		long absTime = Math.abs((currDate.getTime() - reqTime.getTime()) / 1000);
		if (absTime >= 600) {
			return false;
		}*/
		// 签名验证
		String sginfor = MD5SignUtils.genSign(reqVo, "key", signKey, new String[] { "sign", "serialVersionUID" }, null);
		if (!sginfor.equals(reqVo.getSign())) {
			return false;
		}

		// 是否有2s内的重复
		String tokenV = jedisCluster.get("api.recharge.mobile.token:" + sginfor);
		if (StringUtil.isNotEmpty(tokenV)) {
			return false;
		} else {
			String key="api.recharge.mobile.token:" + sginfor;
			jedisCluster.set(key, sginfor);
			jedisCluster.expire(key, 2);
		}
		return true;
	}

	public  static void main(String[] args) {
		TeleReqVO t = new TeleReqVO();
		t.setChannelId("0e04cf948e2af629a334c7c71fa3f8888");
		t.setChannelToken("0e04cf948e2af629a334c7c71fa3f8888");
		t.setMethod("hkb.api.mobile.charge");
		t.setV("1.0");
		t.setTimestamp("2018-12-21 20:50:14");
		t.setRechargePhone("13501755206");
		t.setRechargeAmount("50");
		t.setOuterTid(IdUtil.getNextId());
		t.setCallback("http://19662nx311.iok.la:10066/recharge/hkbnotify");
		System.out.println(MD5SignUtils.genSign(t, "key", "0e04cf948e2af629a334c7c71fa3f8888", new String[] { "sign", "serialVersionUID" }, null));
	}

}
