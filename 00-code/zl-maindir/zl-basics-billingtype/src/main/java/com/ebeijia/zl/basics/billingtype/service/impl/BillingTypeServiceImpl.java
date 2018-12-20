package com.ebeijia.zl.basics.billingtype.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.billingtype.mapper.BillingTypeMapper;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeService;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 企业员工在平台账户类型 Service 实现类
 *
 * @Date 2018-12-18
 */
@Service
public class BillingTypeServiceImpl extends ServiceImpl<BillingTypeMapper, BillingType> implements BillingTypeService{

	@Autowired
	private BillingTypeMapper billingTypeMapper;

	@Override
	public BillingType getBillingTypeInfById(String bId) {
		return billingTypeMapper.getBillingTypeInfById(bId);
	}

	@Override
	public BillingType getBillingTypeInfByName(String name) {
		return billingTypeMapper.getBillingTypeInfByName(name);
	}

	@Override
	public List<BillingType> getBillingTypeInfList(BillingType billingTypeInf) {
		return billingTypeMapper.getBillingTypeInfList(billingTypeInf);
	}

	@Override
	public PageInfo<BillingType> getBillingTypeInfListPage(int startNum, int pageSize, BillingType billingTypeInf) {
		PageHelper.startPage(startNum, pageSize);
		List<BillingType> list = billingTypeMapper.getBillingTypeInfList(billingTypeInf);
		for (BillingType b : list) {
			b.setCode(SpecAccountTypeEnum.findByBId(b.getBId()).getCode());
		}
		PageInfo<BillingType> page = new PageInfo<BillingType>(list);
		return page;
	}

	@Override
	public int updateBillingTypeInf(BillingType billingTypeInf) {
		return billingTypeMapper.updateBillingTypeInf(billingTypeInf);
	}

	@Override
	public int deleteBillingTypeInf(BillingType billingTypeInf) {
		return billingTypeMapper.deleteBillingTypeInf(billingTypeInf);
	}

	@Override
	public int insertBillingTypeInf(BillingType billingTypeInf) {
		return billingTypeMapper.insertBillingTypeInf(billingTypeInf);
	}

	
}
