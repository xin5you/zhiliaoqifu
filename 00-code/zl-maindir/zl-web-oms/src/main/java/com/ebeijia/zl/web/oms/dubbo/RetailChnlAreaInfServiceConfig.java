package com.ebeijia.zl.web.oms.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class RetailChnlAreaInfServiceConfig extends DubboCustomerConfig {

	
	@Bean
	public ReferenceBean<ProviderOrderInfFacade> providerOrderInfFacade() {
		
		ReferenceBean<ProviderOrderInfFacade> referenceBean=new ReferenceBean<ProviderOrderInfFacade>();
		referenceBean.setInterface(ProviderOrderInfFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlAreaInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("saveRetailChnlAreaInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("updateRetailChnlAreaInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("deleteRetailChnlAreaInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlAreaInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		return referenceBean;
	}
}
