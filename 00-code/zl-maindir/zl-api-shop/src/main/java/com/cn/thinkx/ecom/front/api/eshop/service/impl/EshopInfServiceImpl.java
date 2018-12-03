package com.cn.thinkx.ecom.front.api.eshop.service.impl;

import com.cn.thinkx.ecom.front.api.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.front.api.eshop.mapper.EshopInfMapper;
import com.cn.thinkx.ecom.front.api.eshop.service.EshopInfService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("eshopInfService")
public class EshopInfServiceImpl extends BaseServiceImpl<EshopInf> implements EshopInfService {

	@Autowired
	private EshopInfMapper eshopInfMapper;

	@Override
	public EshopInf selectByEshopInf(EshopInf eshopInf) {
		return this.eshopInfMapper.selectByEshopInf(eshopInf);
	}

}
