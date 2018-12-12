package com.ebeijia.zl.web.oms.phoneRecharge.service;

import java.util.List;

import com.ebeijia.zl.web.oms.phoneRecharge.model.PhoneRechargeOrder;
import com.ebeijia.zl.web.oms.phoneRecharge.model.PhoneRechargeOrderUpload;
import com.github.pagehelper.PageInfo;

public interface PhoneRechargeService {

	/**
	 * 查询手机充值订单信息
	 * 
	 * @return
	 */
	PageInfo<PhoneRechargeOrder> getPhoneRechargeListPage(int startNum, int pageSize, PhoneRechargeOrder entity);
	
	List<PhoneRechargeOrderUpload> getPhoneRechargeList(PhoneRechargeOrder entity);
	
	/**
	 * 支付渠道是福利余额,手机充值退款接口
	 * 
	 * @param rId
	 * @return
	 */
	String doPhoneRechargeRefund(String rId);
}
