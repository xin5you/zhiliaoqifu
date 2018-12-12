package com.ebeijia.zl.basics.order.service.impl;

import com.ebeijia.zl.basics.order.domain.OrderRefund;
import com.ebeijia.zl.basics.order.domain.PlatfShopOrder;
import com.ebeijia.zl.basics.order.mapper.OrderRefundMapper;
import com.ebeijia.zl.basics.order.service.OrderRefundService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import com.ebeijia.zl.common.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
