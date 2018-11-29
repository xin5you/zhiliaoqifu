package com.cn.thinkx.ecom.basics.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.OrderRefund;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.mapper.OrderRefundMapper;
import com.cn.thinkx.ecom.basics.order.service.OrderRefundService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("orderRefundService")
public class OrderRefundServiceImpl extends BaseServiceImpl<OrderRefund> implements OrderRefundService {

	@Autowired
	private OrderRefundMapper orderRefundMapper;
	
	@Override
	public List<OrderRefund> getOrderRefundList(OrderRefund orderRefund) {
		return orderRefundMapper.getOrderRefundList(orderRefund);
	}

	@Override
	public void saveOrderRefundInf(String returnsId, PlatfShopOrder pso) {
		/** 退款表 */
		OrderRefund orderRefund = new OrderRefund();
		orderRefund.setReturnsId(returnsId);
		orderRefund.setsOrderId(pso.getsOrderId());
		if(Constants.GoodsEcomCodeType.ECOM01.getCode().equals(pso.getEcomCode())){
			orderRefund.setRefundAmt(String.valueOf(Double.parseDouble(pso.getPayAmt())+ Double.parseDouble(pso.getShippingFreightPrice()))); // 订单支付金额+配送费用
		}
		orderRefund.setMemberId(pso.getMemberId());
		orderRefund.setRefundStatus(Constants.RefundStatus.RFS0.getCode()); // 申请中
		orderRefundMapper.insert(orderRefund);
	}

	@Override
	public OrderRefund getOrderRefundByReturnsId(String returnsId) {
		return orderRefundMapper.getOrderRefundByReturnsId(returnsId);
	}


}
