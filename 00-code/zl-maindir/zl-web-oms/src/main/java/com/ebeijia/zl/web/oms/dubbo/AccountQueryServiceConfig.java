package com.ebeijia.zl.web.oms.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务的接口 配置
 *
 */
@Configuration
public class AccountQueryServiceConfig extends DubboCustomerConfig {

	@Bean
	public ReferenceBean<AccountQueryFacade> accountQueryFacade() {
		ReferenceBean<AccountQueryFacade> referenceBean=new ReferenceBean<AccountQueryFacade>();
		referenceBean.setInterface(AccountQueryFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");

		List<MethodConfig> methods=new ArrayList<MethodConfig>();
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getAccountInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getAccountInfPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getAccountLogPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getAccountLogVoByParams");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		referenceBean.setMethods(methods);

		return referenceBean;
	}
	
}
