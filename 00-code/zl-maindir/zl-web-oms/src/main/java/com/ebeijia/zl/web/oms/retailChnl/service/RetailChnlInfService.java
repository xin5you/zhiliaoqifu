package com.ebeijia.zl.web.oms.retailChnl.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

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
	
	int retailChnlOpenAccount(HttpServletRequest req);

	int addRetailChnlTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);

	int addRetailChnlTransferCommit(HttpServletRequest req);

	int editRetailChnlTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);

}
