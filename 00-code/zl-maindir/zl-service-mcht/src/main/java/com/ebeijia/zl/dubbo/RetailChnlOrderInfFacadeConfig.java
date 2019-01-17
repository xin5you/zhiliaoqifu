package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
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
public class RetailChnlOrderInfFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<RetailChnlOrderInfFacade> retailChnlOrderInfFacade(RetailChnlOrderInfFacade retailChnlOrderInfFacade) {
		
		ServiceBean<RetailChnlOrderInfFacade> serviceBean=new ServiceBean<RetailChnlOrderInfFacade>();
		serviceBean.setInterface(RetailChnlOrderInfFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(retailChnlOrderInfFacade);
		serviceBean.setCluster("failfast");

		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlOrderInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		return serviceBean;
	}
}
