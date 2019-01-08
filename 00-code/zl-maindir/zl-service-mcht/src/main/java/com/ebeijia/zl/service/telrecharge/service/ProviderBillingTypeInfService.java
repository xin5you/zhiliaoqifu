package com.ebeijia.zl.service.telrecharge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * 供应商专项类型关联信息Service
 */
public interface ProviderBillingTypeInfService extends IService<ProviderBillingTypeInf> {

    /**
     * 根据条件查询供应商专项类型信息列表
     * @param providerBillingTypeInf
     * @return
     */
    public List<ProviderBillingTypeInf> getProviderBillingTypeInfList(ProviderBillingTypeInf providerBillingTypeInf);

    /**
     * 根据条件查询供应商专项类型信息列表（分页）
     * @param providerBillingTypeInf
     * @return
     */
    public PageInfo<ProviderBillingTypeInf> getProviderBillingTypeInfPage(int startNum, int pageSize, ProviderBillingTypeInf providerBillingTypeInf);

    /**
     * 根据BId与ProviderId查询供应商专项类型信息
     * @param providerBillingTypeInf
     * @return
     */
    public ProviderBillingTypeInf getProviderBillingTypeInfByBIdAndProviderId(ProviderBillingTypeInf providerBillingTypeInf);

}
