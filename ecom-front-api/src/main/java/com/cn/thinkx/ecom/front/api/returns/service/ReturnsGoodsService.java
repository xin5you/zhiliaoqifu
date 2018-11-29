package com.cn.thinkx.ecom.front.api.returns.service;

import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsInf;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.common.domain.BaseResult;

public interface ReturnsGoodsService {

	/**
	 * 申请退货
	 * @param returns
	 * @param reason 原因
	 * @param reasonDesc 原因说明
	 * @return
	 * @throws Exception
	 */
	BaseResult<Object> handleReturnsOrder(ReturnsInf returns, String reason, String reasonDesc) throws Exception;
	
	/**
	 * 取消退货
	 * @param returnsOrder
	 * @return
	 * @throws Exception
	 */
	BaseResult<Object> handleCancelReturns(ReturnsOrder returnsOrder) throws Exception;
	
	/**
	 * 绑定退货物流信息
	 * @param returnsOrder
	 * @param confirm
	 * @return
	 * @throws Exception
	 */
	BaseResult<Object> handleOfferExpress(ReturnsOrder returnsOrder, EcomExpressConfirm confirm) throws Exception;
	
	/**
	 * 查看退货详情
	 * @param apply
	 * @return
	 * @throws Exception
	 */
	BaseResult<Object> handleQueryReturnsOrder(String applyId) throws Exception;
}
