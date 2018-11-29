package com.cn.thinkx.ecom.platforder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.cn.thinkx.ecom.basics.order.mapper.ExpressPlatfMapper;
import com.cn.thinkx.ecom.basics.order.service.ExpressPlatfService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.platforder.service.ExpressPlatfInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("expressPlatfInfService")
public class ExpressPlatfInfServiceImpl implements ExpressPlatfInfService {

	@Autowired
	private ExpressPlatfService expressPlatfService;

	@Override
	public PageInfo<ExpressPlatf> getExpressPlatfListPage(int startNum, int pageSize, ExpressPlatf entity) {
		PageHelper.startPage(startNum, pageSize);
		List<ExpressPlatf> list = expressPlatfService.getExpressPlatfList(entity);
		for (ExpressPlatf expressPlatf : list) {
			if(!StringUtil.isNullOrEmpty(expressPlatf.getEcomCode()))
				expressPlatf.setEcomCode(Constants.GoodsEcomCodeType.findByCode(expressPlatf.getEcomCode()).getValue());
			if (!StringUtil.isNullOrEmpty(expressPlatf.getPackageStat()))
				expressPlatf.setPackageStat(Constants.PackageStatus.findByCode(expressPlatf.getPackageStat()).getValue());
			if(!StringUtil.isNullOrEmpty(expressPlatf.getSubOrderStatus()))
				expressPlatf.setSubOrderStatus(Constants.SubOrderStatus.findByCode(expressPlatf.getSubOrderStatus()).getValue());
		}
		PageInfo<ExpressPlatf> page = new PageInfo<ExpressPlatf>(list);
		return page;
	}

	@Override
	public List<ExpressPlatf> getExpressPlatfProductByPackId(String packId) {
		return expressPlatfService.getExpressPlatfProductByPackId(packId);
	}

}
