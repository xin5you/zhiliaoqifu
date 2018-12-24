package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class AccountQueryServiceConfig extends DubboCustomerConfig {

	@Bean
	public ReferenceBean<AccountQueryFacade> accountQueryFacade() {
		
		ReferenceBean<AccountQueryFacade> referenceBean=new ReferenceBean<>();
		referenceBean.setInterface(AccountQueryFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		
//		List<MethodConfig> methods=new ArrayList<MethodConfig>();
//		/*** dubbo method registerUserInf config*/
//		MethodConfig methodConfig=new MethodConfig();
//		methodConfig.setName("createAccount");
//		methodConfig.setTimeout(3000);
//		methodConfig.setRetries(0);
//		methods.add(methodConfig); //
//		referenceBean.setMethods(methods);
		
		return referenceBean;
	}
}
