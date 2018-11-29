package com.cn.thinkx.ecom.basics.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.cn.thinkx.ecom.basics.order.mapper.EcomExpressConfirmMapper;
import com.cn.thinkx.ecom.basics.order.service.EcomExpressConfirmService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("ecomExpressConfirmService")
public class EcomExpressConfirmServiceImpl extends BaseServiceImpl<EcomExpressConfirm>
		implements EcomExpressConfirmService {

	@Autowired
	private EcomExpressConfirmMapper ecomExpressConfirmMapper;

	@Override
	public EcomExpressConfirm selectByReturnsId(String returnsId) {
		return ecomExpressConfirmMapper.selectByReturnsId(returnsId);
	}

}
