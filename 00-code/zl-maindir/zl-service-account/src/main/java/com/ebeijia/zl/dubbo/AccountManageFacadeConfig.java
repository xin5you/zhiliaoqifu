package com.ebeijia.zl.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class AccountManageFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<AccountManageFacade> accountManageFacade(AccountManageFacade accountManageFacade) {
		
		ServiceBean<AccountManageFacade> serviceBean=new ServiceBean<AccountManageFacade>();
		serviceBean.setInterface(AccountManageFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(accountManageFacade);
		serviceBean.setCluster("failfast");
		
		List<MethodConfig> methods=new ArrayList<MethodConfig>();
		/*** dubbo method registerUserInf config*/
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("createAccount");
		methodConfig.setTimeout(120000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);


		/*** dubbo method registerUserInf config*/
		 methodConfig=new MethodConfig();
		methodConfig.setName("createAccountList");
		methodConfig.setTimeout(60000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);
		
		return serviceBean;
	}
}
