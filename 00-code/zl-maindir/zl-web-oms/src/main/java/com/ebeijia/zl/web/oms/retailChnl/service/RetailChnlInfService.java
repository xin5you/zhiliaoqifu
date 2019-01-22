package com.ebeijia.zl.web.oms.retailChnl.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface RetailChnlInfService {

	/**
	 * 通知分销商失败，手动再次回调分销商
	 * 
	 * @param channelOrderId
	 */
	ModelMap doCallBackNotifyChannel(String channelOrderId);

	/**
	 * 添加分销商折扣率
	 * 
	 * @param req
	 * @param channelId
	 * @param ids
	 * @return
	 */
	boolean addRetailChnlRate(HttpServletRequest req, String channelId, String channelRate, String ids);

	/**
	 * 分销商开户
	 * @param req
	 * @return
	 */
	int retailChnlOpenAccount(HttpServletRequest req);

}
