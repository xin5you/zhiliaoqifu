package com.ebeijia.zl.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class AccountQueryFacadeConfig extends DubboProviderConfig {

	@Bean
	public ServiceBean<AccountQueryFacade> accountQueryFacade(AccountQueryFacade accountQueryFacade) {

		ServiceBean<AccountQueryFacade> serviceBean=new ServiceBean<AccountQueryFacade>();
		serviceBean.setInterface(AccountQueryFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(accountQueryFacade);
		serviceBean.setCluster("failfast");

		List<MethodConfig> methods=new ArrayList<MethodConfig>();
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getAccountInfList");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getAccountInfPage");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getAccountLogPage");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getAccountLogVoByParams");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		serviceBean.setMethods(methods);

		return serviceBean;
	}
}
