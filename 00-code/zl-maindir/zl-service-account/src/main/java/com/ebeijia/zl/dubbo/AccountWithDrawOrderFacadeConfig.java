package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.facade.account.service.AccountWithDrawOrderFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务暴露的接口 配置
 */
@Configuration
public class AccountWithDrawOrderFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<AccountWithDrawOrderFacade> accountWithDrawOrderFacade(AccountWithDrawOrderFacade accountWithDrawOrderFacade) {
		
		ServiceBean<AccountWithDrawOrderFacade> serviceBean = new ServiceBean<AccountWithDrawOrderFacade>();
		serviceBean.setInterface(AccountWithDrawOrderFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(accountWithDrawOrderFacade);
		serviceBean.setCluster("failfast");
		serviceBean.setTimeout(10000);
		serviceBean.setRetries(0);
		
		List<MethodConfig> methods=new ArrayList<MethodConfig>();
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawOrderList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);

		methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawDetailList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);

		methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawOrderPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);

		methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawDetailPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);
		
		return serviceBean;
	}
}
