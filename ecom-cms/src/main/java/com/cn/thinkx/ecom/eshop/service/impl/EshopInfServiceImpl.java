package com.cn.thinkx.ecom.eshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.eshop.mapper.EshopInfMapper;
import com.cn.thinkx.ecom.eshop.service.EshopInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("eshopInfService")
public class EshopInfServiceImpl extends BaseServiceImpl<EshopInf> implements EshopInfService {

	@Autowired
	private EshopInfMapper eshopInfMapper;

	@Override
	public PageInfo<EshopInf> getEshopInfPage(int startNum, int pageSize, EshopInf entity) {
		PageHelper.startPage(startNum, pageSize);
		List<EshopInf> list = eshopInfMapper.getList(entity);
		PageInfo<EshopInf> page = new PageInfo<EshopInf>(list);
		return page;
	}

	@Override
	public List<EshopInf> getList() {
		return this.eshopInfMapper.getList();
	}

	@Override
	public List<EshopInf> selectByComboBox() {
		return this.eshopInfMapper.selectByComboBox();
	}
	
	@Override
	public EshopInf selectByEshopName(EshopInf eshopInf) {
		return this.eshopInfMapper.selectByEshopName(eshopInf);
	}

	@Override
	public EshopInf selectByEshopInf(EshopInf eshopInf) {
		return this.eshopInfMapper.selectByEshopInf(eshopInf);
	}

}
