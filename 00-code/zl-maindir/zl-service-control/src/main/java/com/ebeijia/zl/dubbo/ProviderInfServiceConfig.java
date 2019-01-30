package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ProviderInfServiceConfig extends DubboCustomerConfig {

	@Bean
	public ReferenceBean<ProviderInfFacade> providerInfFacade() {
		
		ReferenceBean<ProviderInfFacade> referenceBean=new ReferenceBean<ProviderInfFacade>();
		referenceBean.setInterface(ProviderInfFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		
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

		return referenceBean;
	}
}
