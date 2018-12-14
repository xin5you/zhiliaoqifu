package com.ebeijia.zl.service.telrecharge.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderBillingType;


/**
 * 供应商账户类型信息 Service 接口类
 * @author Administrator
 *
 */
public interface ProviderBillingTypeService extends IService<ProviderBillingType> {

	public List<ProviderBillingType> getListByProviderId(String providerId);
}
