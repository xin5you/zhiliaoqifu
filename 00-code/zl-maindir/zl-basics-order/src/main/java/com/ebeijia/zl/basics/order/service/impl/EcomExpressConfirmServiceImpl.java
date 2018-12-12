package com.ebeijia.zl.basics.order.service.impl;

import com.ebeijia.zl.basics.order.domain.EcomExpressConfirm;
import com.ebeijia.zl.basics.order.mapper.EcomExpressConfirmMapper;
import com.ebeijia.zl.basics.order.service.EcomExpressConfirmService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
