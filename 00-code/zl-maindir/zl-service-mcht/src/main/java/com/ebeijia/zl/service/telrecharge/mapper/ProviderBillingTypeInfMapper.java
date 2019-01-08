package com.ebeijia.zl.service.telrecharge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingTypeInf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 供应商专项类型关联信息Mapper
 */
@Mapper
public interface ProviderBillingTypeInfMapper extends BaseMapper<ProviderBillingTypeInf> {

    /**
     * 根据条件查询供应商专项类型信息列表
     * @param providerBillingTypeInf
     * @return
     */
    public List<ProviderBillingTypeInf> getProviderBillingTypeInfList(ProviderBillingTypeInf providerBillingTypeInf);

    /**
     * 根据BId与ProviderId查询供应商专项类型信息
     * @param providerBillingTypeInf
     * @return
     */
    public ProviderBillingTypeInf getProviderBillingTypeInfByBIdAndProviderId(ProviderBillingTypeInf providerBillingTypeInf);

}
