package com.ebeijia.zl.service.telrecharge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingType;

@Mapper
public interface ProviderBillingTypeMapper extends BaseMapper<ProviderBillingType> {

	List<ProviderBillingType> getListByProviderId(String providerId);
}
