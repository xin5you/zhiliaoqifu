package com.ebeijia.zl.web.api.model.telephone.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.ebeijia.zl.common.utils.tools.ResultsUtil;
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
	
	@Autowired
	private RedisDictProperties  redisDictProperties;
	
	/**
	 * 手机充值 汇卡宝商城回调
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bmHKbCallBack",method=RequestMethod.POST)
	@ResponseBody
	public String bmHKbCallBack(HttpServletRequest request) throws Exception {
		
		HKbCallBackResult reqResult=new HKbCallBackResult();
		reqResult.setCode(request.getParameter("code"));
		reqResult.setMsg(request.getParameter("msg"));
		reqResult.setOrderId(request.getParameter("orderId"));
		reqResult.setChannelOrderNo(request.getParameter("channelOrderNo"));
		reqResult.setUserId(request.getParameter("userId"));
		reqResult.setPhone(request.getParameter("phone"));
		reqResult.setTelephoneFace(request.getParameter("telephoneFace"));
		reqResult.setOrderType(request.getParameter("orderType"));
		reqResult.setAttach(request.getParameter("attach"));
		reqResult.setReqChannel(request.getParameter("reqChannel"));
		reqResult.setRespTime(request.getParameter("respTime"));
		reqResult.setSign(request.getParameter("sign"));
		
		logger.info(JSONObject.toJSONString(reqResult));
		
		String retSign=MD5SignUtils.genSign(reqResult, "key",
				redisDictProperties.getdictValueByCode("P1003_SIGN_KEY"),
				new String[]{"sign","serialVersionUID"}, null);
		if(retSign.equals(reqResult.getSign())){
			//获取供应商订单 && 修改供应商订单
			ProviderOrderInf providerOrderInf =providerOrderInfFacade.getProviderOrderInfById(reqResult.getChannelOrderNo());
			try{
				if("1".equals(reqResult.getCode())){
					providerOrderInf.setResv1("00"); //存储返回code
					providerOrderInf.setOperateTime(System.currentTimeMillis());
					providerOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_1.getCode());
				}else{
					providerOrderInf.setResv1(reqResult.getCode()); //存储返回错误的code
					providerOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_3.getCode());
				}
				 providerOrderInfFacade.updateProviderOrderInf(providerOrderInf);
			}catch(Exception ex){
				logger.error("#手机充值 修改供应商订单异常-->{}",ex);
			}
			
			//回调通知分销商
			try{
				RetailChnlOrderInf retailChnlOrderInf=retailChnlOrderInfFacade.getRetailChnlOrderInfById(providerOrderInf.getChannelOrderId());
				if(!"1".equals(reqResult.getCode())){
					//修改订单状态 为申请退款，处理状态 为失败
					retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_2.getCode());  //处理失败
					retailChnlOrderInf.setResv1(reqResult.getCode());
					retailChnlOrderInfFacade.updateRetailChnlOrderInf(retailChnlOrderInf);
				}else{
					retailChnlOrderInf.setOrderStat(TeleConstants.ChannelOrderPayStat.ORDER_PAY_1.getCode());  //已经付款
					retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_3.getCode());  //处理成功
					 retailChnlOrderInfFacade.updateRetailChnlOrderInf(retailChnlOrderInf);
				}
				
			 if("0".equals(retailChnlOrderInf.getNotifyFlag())){
				RetailChnlInf retailChnlInf=retailChnlInfFacade.getRetailChnlInfById(retailChnlOrderInf.getChannelId());
					//异步通知供应商
					TeleRespVO respVo=new TeleRespVO();
					respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
					respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
					respVo.setPayState(retailChnlOrderInf.getOrderStat());
					respVo.setRechargeState(providerOrderInf.getRechargeState()); //充值状态
					if(providerOrderInf.getOperateTime() !=null){
						respVo.setOperateTime(DateUtil.COMMON_FULL.getDateText(new Date(providerOrderInf.getOperateTime())));
					}
					respVo.setOrderTime(String.valueOf(retailChnlOrderInf.getCreateTime())); //操作时间
					respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
					respVo.setItemNum(retailChnlOrderInf.getItemNum());
					respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
					respVo.setChannelId(retailChnlOrderInf.getChannelId());
					respVo.setChannelToken(retailChnlInf.getChannelCode());
					respVo.setV(retailChnlOrderInf.getAppVersion());
					respVo.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
					if(!"1".equals(reqResult.getCode())){
						respVo.setSubErrorCode(providerOrderInf.getResv1());
						respVo.setSubErrorMsg(reqResult.getMsg());
					}else{
						respVo.setSubErrorCode(providerOrderInf.getResv1());
					}
					if("1".equals(retailChnlOrderInf.getRechargeType())){
						respVo.setMethod(ReqMethodCode.R1.getValue());
					}else if("2".equals(retailChnlOrderInf.getRechargeType())){
						respVo.setMethod(ReqMethodCode.R2.getValue());
					}
					String psotToken=MD5SignUtils.genSign(respVo, "key", retailChnlInf.getChannelKey(), new String[]{"sign","serialVersionUID"}, null);
					respVo.setSign(psotToken);
					
					//修改通知后 分销商的处理状态
					logger.info("##发起分销商回调[{}],返回参数:[{}]",retailChnlOrderInf.getNotifyUrl(),JSONObject.toJSONString(ResultsUtil.success(respVo)));
					String result=HttpClientUtil.sendPostReturnStr(retailChnlOrderInf.getNotifyUrl(),JSONObject.toJSONString(ResultsUtil.success(respVo)));
					if(result !=null && "SUCCESS ".equals(result.toUpperCase() )){
						retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_3.getCode());
					}else{
						retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_2.getCode());
					}
					retailChnlOrderInfFacade.updateRetailChnlOrderInf(retailChnlOrderInf);
				}
			}catch(Exception ex){
				logger.error("##手机充值 回调通知分销商异常-->",ex);
			}
		}else{
			logger.info("#手机充值 回调验签失败-->{}",retSign);
			return "FAIL";
		}
		
		return "SUCCESS";
	}
	
}
