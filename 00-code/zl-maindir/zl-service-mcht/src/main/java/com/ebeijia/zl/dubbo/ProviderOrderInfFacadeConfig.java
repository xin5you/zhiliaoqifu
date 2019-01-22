package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class ProviderOrderInfFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<ProviderOrderInfFacade> providerOrderInfFacade(ProviderOrderInfFacade providerOrderInfFacade) {
		
		ServiceBean<ProviderOrderInfFacade> serviceBean=new ServiceBean<ProviderOrderInfFacade>();
		serviceBean.setInterface(ProviderOrderInfFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(providerOrderInfFacade);
		serviceBean.setCluster("failfast");
		serviceBean.setTimeout(5000);
		serviceBean.setRetries(0);


		return serviceBean;
	}
}
