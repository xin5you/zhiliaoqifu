package com.ebeijia.zl.service.telrecharge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingType;
import com.ebeijia.zl.service.telrecharge.mapper.ProviderBillingTypeMapper;
import com.ebeijia.zl.service.telrecharge.service.ProviderBillingTypeService;

/**
 * 供应商账户类型信息 Service 实现类
 * @author Administrator
 *
 */
@Service
public class ProviderBillingTypeServiceImpl extends ServiceImpl<ProviderBillingTypeMapper, ProviderBillingType> implements ProviderBillingTypeService{
	
	@Autowired
	private ProviderBillingTypeMapper providerBillingTypeMapper;

	@Override
	public List<ProviderBillingType> getListByProviderId(String providerId) {
		return providerBillingTypeMapper.getListByProviderId(providerId);
	}

}
