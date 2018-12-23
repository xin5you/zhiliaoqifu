package com.ebeijia.zl.api.bm001.api.service;

import com.ebeijia.zl.api.bm001.api.req.PayBillReq;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;
import com.qianmi.open.api.response.BmRechargeMobileGetItemInfoResponse;
import com.qianmi.open.api.response.BmRechargeMobileGetPhoneInfoResponse;
import com.qianmi.open.api.response.BmRechargeMobilePayBillResponse;
import com.qianmi.open.api.response.BmRechargeOrderInfoResponse;

public interface BMOpenApiService {

	
	/**
	 * @param mobileNo
	 * @param rechargeAmount
	 * @param accessToken
	 * @return
	 */
	BmRechargeMobileGetItemInfoResponse handleGetItemInfo(String mobileNo, String rechargeAmount, String accessToken);
	
	/**
	 * @param payBillReq
	 * @param accessToken
	 * @return
	 */
	BmRechargeMobilePayBillResponse handlePayBill(PayBillReq payBillReq, String accessToken);
	
	/**
	 * @param billId
	 * @param accessToken
	 * @return
	 */
	BmRechargeOrderInfoResponse handleGetOrderInfo(String billId, String accessToken);
	
	/**
	 * @param outerTid
	 * @param accessToken
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
