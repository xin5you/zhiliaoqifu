package com.ebeijia.zl.web.oms.phoneRecharge.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlItemList;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.utils.ResultsUtil;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.ebeijia.zl.web.oms.phoneRecharge.service.TelChannelInfService;
import com.ebeijia.zl.web.oms.sys.model.User;

@Service("telChannelInfService")
public class TelChannelInfServiceImpl implements TelChannelInfService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Reference(check=false)
	private RetailChnlOrderInfFacade telChannelOrderInfFacade;

	@Reference(check=false)
	private RetailChnlInfFacade telChannelInfFacade;

	@Reference(check=false)
	private ProviderOrderInfFacade telProviderOrderInfFacade;
	
	@Reference(check=false)
	private RetailChnlItemListFacade telChannelItemListFacade;

	@Override
	public ModelMap doCallBackNotifyChannel(String channelOrderId) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		// 回调通知分销商
		try {
			if (StringUtil.isNullOrEmpty(channelOrderId)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "该订单回调异常，请联系管理员");
				logger.error("## 该分销商订单回调异常，分销商订单号channelOrderId:[{}]是空", channelOrderId);
				return resultMap;
			}
			RetailChnlOrderInf telChannelOrderInf = telChannelOrderInfFacade.getRetailChnlOrderInfById(channelOrderId);
			if (StringUtil.isNullOrEmpty(telChannelOrderInf.getNotifyUrl())) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "该订单不能回调，回调地址是空");
				logger.error("## 该分销商订单不能回调，回调地址是空NotifyUrl:[{}]", telChannelOrderInf.getNotifyUrl());
				return resultMap;
			}
			RetailChnlInf telChannelInf = telChannelInfFacade.getRetailChnlInfById(telChannelOrderInf.getChannelId());

			ProviderOrderInf telProviderOrderInf = telProviderOrderInfFacade
					.getTelOrderInfByChannelOrderId(channelOrderId);
			// 异步通知供应商
			TeleRespVO respVo = new TeleRespVO();
			respVo.setSaleAmount(telChannelOrderInf.getPayAmt().toString());
			respVo.setChannelOrderId(telChannelOrderInf.getChannelOrderId());
			respVo.setPayState(telChannelOrderInf.getOrderStat());
			respVo.setRechargeState(telProviderOrderInf.getRechargeState()); // 充值状态
			if (telProviderOrderInf.getOperateTime() != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    String operateTimeStr = format.format(telProviderOrderInf.getOperateTime());  
			    Date operateTimeDate = format.parse(operateTimeStr);
				respVo.setOperateTime(DateUtil.COMMON_FULL.getDateText(operateTimeDate));
			}
			respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date(telChannelOrderInf.getCreateTime()))); // 操作时间
			respVo.setFacePrice(telChannelOrderInf.getRechargeValue().toString());
			respVo.setItemNum(telChannelOrderInf.getItemNum());
			respVo.setOuterTid(telChannelOrderInf.getOuterTid());
			respVo.setChannelId(telChannelOrderInf.getChannelId());
			respVo.setChannelToken(telChannelInf.getChannelCode());
			respVo.setV(telChannelOrderInf.getAppVersion());
			respVo.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
			respVo.setSubErrorCode(telProviderOrderInf.getResv1());

			if ("1".equals(telChannelOrderInf.getRechargeType())) {
				respVo.setMethod(ReqMethodCode.R1.getValue());
			} else if ("2".equals(telChannelOrderInf.getRechargeType())) {
				respVo.setMethod(ReqMethodCode.R2.getValue());
			}
			String psotToken = MD5SignUtils.genSign(respVo, "key", telChannelInf.getChannelKey(),
					new String[] { "sign", "serialVersionUID" }, null);
			respVo.setSign(psotToken);

			// 修改通知后 分销商的处理状态
			logger.info("##发起分销商回调[{}],返回参数:[{}]", telChannelOrderInf.getNotifyUrl(),
					JSONObject.toJSONString(ResultsUtil.success(respVo)));
			String result = HttpClientUtil.sendPost(telChannelOrderInf.getNotifyUrl(),
					JSONObject.toJSONString(ResultsUtil.success(respVo)));
			logger.info("## 发起分销商回调,返回结果:[{}]", result);
			if (result != null && "SUCCESS ".equals(result.toUpperCase())) {
				telChannelOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_3.getCode());
			} else {
				telChannelOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_2.getCode());
			}
			telChannelOrderInfFacade.updateRetailChnlOrderInf(telChannelOrderInf);
		} catch (Exception ex) {
			logger.error("## 手机充值 回调通知分销商异常-->{}", ex);
		}
		return resultMap;
	}

	@Override
	public boolean addTelChannelRate(HttpServletRequest req, String channelId,String channelRate, String ids) {
		try {
			String[] productIds = ids.split(",");
			for (int i = 0; i < productIds.length; i++) {
				RetailChnlItemList telChannelItemList = new RetailChnlItemList();
				HttpSession session = req.getSession();
				User user = (User) session.getAttribute(Constants.SESSION_USER);
				if (user != null) {
					telChannelItemList.setCreateUser(user.getId().toString());
					telChannelItemList.setUpdateUser(user.getId().toString());
				}
				telChannelItemList.setProductId(productIds[i]);
				telChannelItemList.setDataStat("0");
				telChannelItemList.setChannelRate(new BigDecimal(channelRate));
				telChannelItemList.setAreaId(channelId);
				telChannelItemListFacade.saveRetailChnlItemList(telChannelItemList);
			}
		} catch (Exception e) {
			logger.error("## 楼层添加商品失败", e);
			return false;
		}
		return true;
	}

}
