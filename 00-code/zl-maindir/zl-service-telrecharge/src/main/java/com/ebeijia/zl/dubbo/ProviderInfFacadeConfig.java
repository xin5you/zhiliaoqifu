package com.ebeijia.zl.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class ProviderInfFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<ProviderInfFacade> providerInfFacade(ProviderInfFacade providerInfFacade) {
		
		ServiceBean<ProviderInfFacade> serviceBean=new ServiceBean<ProviderInfFacade>();
		serviceBean.setInterface(ProviderInfFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(providerInfFacade);
		serviceBean.setCluster("failfast");
		return serviceBean;
	}
}
