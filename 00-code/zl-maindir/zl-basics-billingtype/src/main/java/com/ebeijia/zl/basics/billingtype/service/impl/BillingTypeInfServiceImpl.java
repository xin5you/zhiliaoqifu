package com.ebeijia.zl.basics.billingtype.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.billingtype.domain.BillingTypeInf;
import com.ebeijia.zl.basics.billingtype.mapper.BillingTypeInfMapper;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeInfService;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("billingTypeInfService")
public class BillingTypeInfServiceImpl  extends ServiceImpl<BillingTypeInfMapper, BillingTypeInf> implements BillingTypeInfService {
	 
	@Autowired
	private BillingTypeInfMapper billingTypeInfMapper;

	@Override
	public BillingTypeInf getBillingTypeInfById(String bId) {
		return billingTypeInfMapper.getBillingTypeInfById(bId);
	}

	@Override
	public BillingTypeInf getBillingTypeInfByName(String name) {
		return billingTypeInfMapper.getBillingTypeInfByName(name);
	}

	@Override
	public List<BillingTypeInf> getBillingTypeInfList(BillingTypeInf billingTypeInf) {
		return billingTypeInfMapper.getBillingTypeInfList(billingTypeInf);
	}

	@Override
	public PageInfo<BillingTypeInf> getBillingTypeInfListPage(int startNum, int pageSize, BillingTypeInf billingTypeInf) {
		PageHelper.startPage(startNum, pageSize);
		List<BillingTypeInf> list = billingTypeInfMapper.getBillingTypeInfList(billingTypeInf);
		for (BillingTypeInf b : list) {
			b.setCode(SpecAccountTypeEnum.findByBId(b.getBId()).getCode());
		}
		PageInfo<BillingTypeInf> page = new PageInfo<BillingTypeInf>(list);
		return page;
	}

	@Override
	public int updateBillingTypeInf(BillingTypeInf billingTypeInf) {
		return billingTypeInfMapper.updateBillingTypeInf(billingTypeInf);
	}

	@Override
	public int deleteBillingTypeInf(BillingTypeInf billingTypeInf) {
		return billingTypeInfMapper.deleteBillingTypeInf(billingTypeInf);
	}

	@Override
	public int insertBillingTypeInf(BillingTypeInf billingTypeInf) {
		return billingTypeInfMapper.insertBillingTypeInf(billingTypeInf);
	}
	

}
