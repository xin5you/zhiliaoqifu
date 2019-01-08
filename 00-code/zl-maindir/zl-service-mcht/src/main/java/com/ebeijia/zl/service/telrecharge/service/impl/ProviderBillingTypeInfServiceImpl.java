package com.ebeijia.zl.service.telrecharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
import com.ebeijia.zl.service.telrecharge.mapper.ProviderBillingTypeInfMapper;
import com.ebeijia.zl.service.telrecharge.service.ProviderBillingTypeInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商专项类型关联信息ServiceImpl
 */
@Service
public class ProviderBillingTypeInfServiceImpl extends ServiceImpl<ProviderBillingTypeInfMapper, ProviderBillingTypeInf> implements ProviderBillingTypeInfService {

    @Override
    public List<ProviderBillingTypeInf> getProviderBillingTypeInfList(ProviderBillingTypeInf providerBillingTypeInf) {
        return baseMapper.getProviderBillingTypeInfList(providerBillingTypeInf);
    }

    @Override
    public PageInfo<ProviderBillingTypeInf> getProviderBillingTypeInfPage(int startNum, int pageSize, ProviderBillingTypeInf providerBillingTypeInf) {
        PageHelper.startPage(startNum, pageSize);
        List<ProviderBillingTypeInf> list = baseMapper.getProviderBillingTypeInfList(providerBillingTypeInf);
        PageInfo<ProviderBillingTypeInf> page = new PageInfo<ProviderBillingTypeInf>(list);
        return page;
    }

    @Override
    public ProviderBillingTypeInf getProviderBillingTypeInfByBIdAndProviderId(ProviderBillingTypeInf providerBillingTypeInf) {
        return baseMapper.getProviderBillingTypeInfByBIdAndProviderId(providerBillingTypeInf);
    }
}
