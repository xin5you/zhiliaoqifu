package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
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
public class ProviderOrderInfServiceConfig extends DubboCustomerConfig {

	
	@Bean
	public ReferenceBean<ProviderOrderInfFacade> providerOrderInfFacade() {
		
		ReferenceBean<ProviderOrderInfFacade> referenceBean=new ReferenceBean<ProviderOrderInfFacade>();
		referenceBean.setInterface(ProviderOrderInfFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getProviderOrderInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("saveProviderOrderInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("updateProviderOrderInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("deleteProviderOrderInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderOrderInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getTelOrderInfByChannelOrderId");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getProviderOrderInfPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getListByTimer");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		return referenceBean;
	}
}
