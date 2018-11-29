package com.cn.thinkx.ecom.front.api.phoneRecharge.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cn.thinkx.ecom.common.constants.Constants.PhoneRechargeTransStat;
import com.cn.thinkx.ecom.common.constants.Constants.RechargeState;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;
import com.cn.thinkx.ecom.front.api.phoneRecharge.resp.PhoneRechargeNotifyResp;

/**
 * 交易订单 异步通知
 * 
 *
 */
@Configuration
public class PhoneRechargeOrderNotifyHttpClient {

	private static Logger logger = LoggerFactory.getLogger(PhoneRechargeOrderNotifyHttpClient.class);
	
	private static final ExecutorService pool = Executors.newCachedThreadPool();
	
	@Autowired
	private PhoneRechargeSignUtli phoneRechargeSignUtli;

	/**
	 * 订单通知
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doPhoneRechargeOrderNotifyRequest(PhoneRechargeOrder rechargeOrder) {
		
		try{
			StringBuffer paramData = new StringBuffer();
			PhoneRechargeNotifyResp resp = new PhoneRechargeNotifyResp();
			if(PhoneRechargeTransStat.TransStat2.getCode().equals(rechargeOrder.getTransStat())){
				resp.setCode(RechargeState.RechargeState01.getCode());
				resp.setMsg(RechargeState.RechargeState01.getValue());
			}else{
				resp.setCode(RechargeState.RechargeState09.getCode());
				resp.setMsg(RechargeState.RechargeState09.getValue());
			}
			resp.setOrderId(rechargeOrder.getrId());
			resp.setChannelOrderNo(rechargeOrder.getChannelOrderNo());
			resp.setUserId(rechargeOrder.getUserId());
			resp.setPhone(rechargeOrder.getPhone());
			resp.setTelephoneFace(rechargeOrder.getTelephoneFace());
			resp.setOrderType(rechargeOrder.getOrderType());
			resp.setAttach(rechargeOrder.getAttach());
			resp.setReqChannel(rechargeOrder.getReqChannel());
			resp.setRespTime(DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
			
			resp.setSign(phoneRechargeSignUtli.genSign(resp));
			
			paramData.append("code=").append(resp.getCode())
			.append("&msg=").append(resp.getMsg())
			.append("&orderId=").append(resp.getOrderId())
			.append("&channelOrderNo=").append(resp.getChannelOrderNo());
			
			if(!StringUtil.isNullOrEmpty(resp.getUserId())){
				paramData.append("&userId=").append(resp.getUserId());
			}
			paramData.append("&phone=").append(resp.getPhone())
			.append("&telephoneFace=").append(resp.getTelephoneFace())
			.append("&orderType=").append(resp.getOrderType());
			if(!StringUtil.isNullOrEmpty(resp.getAttach())){
				paramData.append("&attach=").append(resp.getAttach());
			}
			paramData.append("&reqChannel=").append(resp.getReqChannel())
			.append("&respTime=").append(resp.getRespTime())
			.append("&sign=").append(resp.getSign());
			
			String result = invokeExtTimer(rechargeOrder.getNotifyUrl(), paramData.toString(), 5);
			return result;
		} catch(Exception e){
			logger.error(" ## 订单异步通知请求发生异常：", e);
		}
			return null;
	}
	
	private static String invokeExtTimer(final String url, final String param, int timeOut) {
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				String msg = "";
				for (int i = 1; i <= 3; i++) {
					logger.info("话费充值订单 -->  异步回调接口， 第[{}]次回调 url[{}] param[{}]", i, url, param);
					msg = HttpClientUtil.sendPost(url, param);
					if (!StringUtil.isNullOrEmpty(msg))
						break;
				}
				return msg;
			}
		};
		Future<String> future = pool.submit(task);
		String threadResult = null;
		try {
			// 等待计算结果，最长等待timeout秒，timeout秒后中止任务
			threadResult = future.get(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error(" ##  话费充值订单 -->  异步回调接口，等待返回结果异常", e);
		}
		return threadResult;
	}
}
