package com.cn.thinkx.ecom.bm001.api.service;

import com.cn.thinkx.ecom.bm001.api.req.PayBillReq;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;
import com.qianmi.open.api.response.BmRechargeMobileGetItemInfoResponse;
import com.qianmi.open.api.response.BmRechargeMobileGetPhoneInfoResponse;
import com.qianmi.open.api.response.BmRechargeMobilePayBillResponse;
import com.qianmi.open.api.response.BmRechargeOrderInfoResponse;

public interface BMOpenApiService {

	
	/**
	 * @param mobileNo
	 * @param rechargeAmount
	 * @return
	 */
	BmRechargeMobileGetItemInfoResponse handleGetItemInfo(String mobileNo, String rechargeAmount, String accessToken);
	
	/**
	 * @param mobileNo
	 * @param rechargeAmount
	 * @param callback
	 * @return
	 */
	BmRechargeMobilePayBillResponse handlePayBill(PayBillReq payBillReq, String accessToken);
	
	/**
	 * @param billId
	 * @return
	 */
	BmRechargeOrderInfoResponse handleGetOrderInfo(String billId, String accessToken);
	
	/**
	 * @param outerTid
	 * @return
	 */
	BmOrderCustomGetResponse handleGetCustomOrder(String outerTid, String accessToken);
	
	/**
	 * @param mobileNo
	 * @param accessToken
	 * @return
	 */
	BmRechargeMobileGetPhoneInfoResponse handleGetPhoneInfo(String mobileNo, String accessToken);
	
}
