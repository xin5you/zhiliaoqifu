package com.ebeijia.zl.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
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
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();
		
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getProviderInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("saveProviderInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("updateProviderInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("deleteProviderInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderInfPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderInfByLawCode");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderInfByOperSolr");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderBillingTypeInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderBillingTypeInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderBillingTypeInfPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderBillingTypeInfByBIdAndProviderId");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("insertProviderBillingTypeInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("updateProviderBillingTypeInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("deleteProviderBillingTypeInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderInfByProviderName");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		return serviceBean;
	}
}
