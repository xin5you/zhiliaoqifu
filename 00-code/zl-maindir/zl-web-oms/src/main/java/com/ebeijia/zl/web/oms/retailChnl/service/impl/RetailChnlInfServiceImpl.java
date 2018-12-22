package com.ebeijia.zl.web.oms.retailChnl.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.IsOpenEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlItemList;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.retailChnl.service.RetailChnlInfService;
import com.ebeijia.zl.web.oms.utils.OrderConstants;

@Service("retailChnlInfService")
public class RetailChnlInfServiceImpl implements RetailChnlInfService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RetailChnlOrderInfFacade retailChnlOrderInfFacade;

	@Autowired
	private RetailChnlInfFacade retailChnlInfFacade;

	@Autowired
	private ProviderOrderInfFacade providerOrderInfFacade;
	
	@Autowired
	private RetailChnlItemListFacade retailChnlItemListFacade;
	
	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;
	
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
			RetailChnlOrderInf retailChnlOrderInf = retailChnlOrderInfFacade.getRetailChnlOrderInfById(channelOrderId);
			if (StringUtil.isNullOrEmpty(retailChnlOrderInf.getNotifyUrl())) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "该订单不能回调，回调地址是空");
				logger.error("## 该分销商订单不能回调，回调地址是空NotifyUrl:[{}]", retailChnlOrderInf.getNotifyUrl());
				return resultMap;
			}
			RetailChnlInf retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(retailChnlOrderInf.getChannelId());

			ProviderOrderInf providerOrderInf = providerOrderInfFacade
					.getTelOrderInfByChannelOrderId(channelOrderId);
			// 异步通知供应商
			TeleRespVO respVo = new TeleRespVO();
			respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
			respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
			respVo.setPayState(retailChnlOrderInf.getOrderStat());
			respVo.setRechargeState(providerOrderInf.getRechargeState()); // 充值状态
			if (providerOrderInf.getOperateTime() != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    String operateTimeStr = format.format(providerOrderInf.getOperateTime());  
			    Date operateTimeDate = format.parse(operateTimeStr);
				respVo.setOperateTime(DateUtil.COMMON_FULL.getDateText(operateTimeDate));
			}
			respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date(retailChnlOrderInf.getCreateTime()))); // 操作时间
			respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
			respVo.setItemNum(retailChnlOrderInf.getItemNum());
			respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
			respVo.setChannelId(retailChnlOrderInf.getChannelId());
			respVo.setChannelToken(retailChnlInf.getChannelCode());
			respVo.setV(retailChnlOrderInf.getAppVersion());
			respVo.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
			respVo.setSubErrorCode(providerOrderInf.getResv1());

			if ("1".equals(retailChnlOrderInf.getRechargeType())) {
				respVo.setMethod(ReqMethodCode.R1.getValue());
			} else if ("2".equals(retailChnlOrderInf.getRechargeType())) {
				respVo.setMethod(ReqMethodCode.R2.getValue());
			}
			String psotToken = MD5SignUtils.genSign(respVo, "key", retailChnlInf.getChannelKey(),
					new String[] { "sign", "serialVersionUID" }, null);
			respVo.setSign(psotToken);

			// 修改通知后 分销商的处理状态
			logger.info("##发起分销商回调[{}],返回参数:[{}]", retailChnlOrderInf.getNotifyUrl(),
					JSONObject.toJSONString(ResultsUtil.success(respVo)));
			String result = HttpClientUtil.sendPost(retailChnlOrderInf.getNotifyUrl(),
					JSONObject.toJSONString(ResultsUtil.success(respVo)));
			logger.info("## 发起分销商回调,返回结果:[{}]", result);
			if (result != null && "SUCCESS ".equals(result.toUpperCase())) {
				retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_3.getCode());
			} else {
				retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_2.getCode());
			}
			retailChnlOrderInfFacade.updateRetailChnlOrderInf(retailChnlOrderInf);
		} catch (Exception ex) {
			logger.error("## 手机充值 回调通知分销商异常-->{}", ex);
		}
		return resultMap;
	}

	@Override
	public boolean addRetailChnlRate(HttpServletRequest req, String channelId,String channelRate, String ids) {
		try {
			String[] productIds = ids.split(",");
			for (int i = 0; i < productIds.length; i++) {
				RetailChnlItemList retailChnlItemList = new RetailChnlItemList();
				HttpSession session = req.getSession();
				User user = (User) session.getAttribute(Constants.SESSION_USER);
				if (user != null) {
					retailChnlItemList.setCreateUser(user.getId().toString());
					retailChnlItemList.setUpdateUser(user.getId().toString());
				}
				retailChnlItemList.setProductId(productIds[i]);
				retailChnlItemList.setDataStat("0");
				retailChnlItemList.setChannelRate(new BigDecimal(channelRate));
				retailChnlItemList.setAreaId(channelId);
				retailChnlItemListFacade.saveRetailChnlItemList(retailChnlItemList);
			}
		} catch (Exception e) {
			logger.error("## 添加分销商折扣率失败", e);
			return false;
		}
		return true;
	}

	@Override
	public int retailChnlOpenAccount(HttpServletRequest req) {
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));
		RetailChnlInf retailChnl = null;
		try {
			retailChnl = retailChnlInfFacade.getRetailChnlInfById(channelId);
			if (StringUtil.isNullOrEmpty(retailChnl)) {
				logger.error("## 查询分销商信息失败，channelId--->{}", channelId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 查询分销商{}信息失败", channelId);
			return 0;
		}
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		BatchOrderList orderList = new BatchOrderList();
		orderList.setUserName(retailChnl.getChannelName());
		orderList.setPhoneNo(retailChnl.getPhoneNo());
		orderList.setAccountType(UserType.TYPE400.getCode());
		
		LinkedList<BatchOrderList> batchOrderList = new LinkedList<>();
		batchOrderList.add(orderList);
		
		int orderResult = batchOrderService.addBatchOrderAndOrderList(req, batchOrderList, TransCode.MB80.getCode(), UserType.TYPE400.getCode());
		if (orderResult < 0) {
			logger.error("## 新增分销商开户订单信息失败");
			return 0;
		}
		
		String orderId = jedisClusterUtils.get(OrderConstants.retailChnlOrderIdSession);
		int i = batchOrderService.batchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
		if (i < 1) {
			logger.error("## 调用开户接口失败");
			return 0;
		}
		jedisClusterUtils.del(OrderConstants.retailChnlOrderIdSession);
		
		try {
			retailChnl.setIsOpen(IsOpenEnum.ISOPEN_TRUE.getCode());
			if (!retailChnlInfFacade.updateRetailChnlInf(retailChnl)) {
				logger.error("## 更新分销商{}开户成功状态失败", channelId);
				return 0;
			}
		} catch (Exception e) {
			logger.error("## 更新分销商{}开户状态失败", channelId);
		}
		return 1;
	}

}
