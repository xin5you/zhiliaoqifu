package com.cn.thinkx.ecom.basics.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.ReturnsInf;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.mapper.ReturnsOrderMapper;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.common.constants.Constants.ApplyReturnState;
import com.cn.thinkx.ecom.common.constants.Constants.returnType;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.NumberUtils;

@Service("returnsOrderService")
public class ReturnsOrderServiceImpl extends BaseServiceImpl<ReturnsOrder> implements ReturnsOrderService {

	@Autowired
	private ReturnsOrderMapper returnsOrderMapper;
	
	@Override
	public List<ReturnsOrder> getReturnsOrderList(ReturnsOrder returnsOrder) {
		return returnsOrderMapper.getReturnsOrderList(returnsOrder);
	}

	@Override
	public List<ReturnsOrder> getReturnsOrderListByReturnsOrder(ReturnsOrder returnsOrder) {
		return returnsOrderMapper.getReturnsOrderListByReturnsOrder(returnsOrder);
	}
	
	@Override
	public List<ReturnsOrder> getReturnsOrderBySorderId(String sOrderId) {
		return returnsOrderMapper.getReturnsOrderBySorderId(sOrderId);
	}

	@Override
	public ReturnsOrder getReturnsOrderByReturnsId(String returnsId) {
		return returnsOrderMapper.getReturnsOrderByReturnsId(returnsId);
	}

	@Override
	public ReturnsInf getReturnsInfByItemId(String itemId) {
		return returnsOrderMapper.getReturnsInfByItemId(itemId);
	}

	@Override
	public ReturnsOrder getReturnsOrderByApplyId(ReturnsOrder returnsOrder) {
		return returnsOrderMapper.getReturnsOrderByApplyId(returnsOrder);
	}

	@Override
	public ReturnsOrder getReturnsOrderByItemId(ReturnsOrder returnsOrder) {
		ReturnsOrder rOrder = returnsOrderMapper.getReturnsOrderByItemId(returnsOrder);
		if(rOrder!=null){
			rOrder.setRetType(returnType.findByCode(rOrder.getApplyReasonType()).getValue());
			rOrder.setProductPrice(NumberUtils.RMBCentToYuan(rOrder.getProductPrice()));
			rOrder.setResv1(NumberUtils.RMBCentToYuan(rOrder.getResv1()));
			rOrder.setApplyReturnType(ApplyReturnState.findByCode(rOrder.getApplyReturnState()).getValue());
		}
		return rOrder;
	}

}
