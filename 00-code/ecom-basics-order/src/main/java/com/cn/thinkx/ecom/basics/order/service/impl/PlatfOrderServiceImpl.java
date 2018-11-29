package com.cn.thinkx.ecom.basics.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.mapper.PlatfOrderMapper;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("platfOrderService")
public class PlatfOrderServiceImpl extends BaseServiceImpl<PlatfOrder> implements PlatfOrderService {

	@Autowired
	private PlatfOrderMapper platfOrderMapper;

	@Override
	public List<PlatfOrder> getPlatfOrderList(PlatfOrder po) {
		return platfOrderMapper.getPlatfOrderList(po);
	}

	@Override
	public String getPrimaryKey() {
		return platfOrderMapper.getPrimaryKey();
	}

	@Override
	public List<PlatfOrder> getPlatfOrderGoodsByMemberId(PlatfOrder po) {
		return platfOrderMapper.getPlatfOrderGoodsByMemberId(po);
	}

	@Override
	public int updateOlatfOrder() {
		return platfOrderMapper.updateOlatfOrder();
	}

}
