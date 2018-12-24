package com.ebeijia.zl.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class AccountTransationFacadeConfig extends DubboProviderConfig {


	@Bean
	public ServiceBean<AccountTransactionFacade> accountTransactionFacade(AccountTransactionFacade accountTransactionFacade) {

		ServiceBean<AccountTransactionFacade> serviceBean=new ServiceBean<AccountTransactionFacade>();
		serviceBean.setInterface(AccountTransactionFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(accountTransactionFacade);
		serviceBean.setCluster("failfast");

		List<MethodConfig> methods=new ArrayList<MethodConfig>();


		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("executeRechargeByOneBId");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("executeRecharge");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("executeConsume");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("executeTransfer");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("executeRefund");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("executeQuery");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		return serviceBean;
	}
}
