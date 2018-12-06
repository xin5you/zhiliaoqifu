package com.ebeijia.zl.web.api.model.phoneRecharge.service;

import javax.servlet.http.HttpServletRequest;

import com.ebeijia.zl.web.api.model.phoneRecharge.model.PhoneRechargeOrder;

public interface UnicomAyncService {

	/**
	 * 购买请求
	 * 
	 * @param vo 充值参数
	 * @return
	 */
	String buy(PhoneRechargeOrder flowOrder);
	
	/**
	 * 查询方法
	 * 
	 * @param req
	 * @return
	 */
	String query(HttpServletRequest req);
	
	/**
	 * 汇卡宝回调方法(提供鼎驰调用)
	 * 
	 * @param request
	 * @return
	 */
	String notify(HttpServletRequest request);
	
}
