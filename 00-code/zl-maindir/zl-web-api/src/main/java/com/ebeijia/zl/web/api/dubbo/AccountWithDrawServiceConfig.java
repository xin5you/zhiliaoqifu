package com.ebeijia.zl.web.api.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.account.service.AccountWithDrawOrderFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务暴露的接口 配置
 * @author zhuqi
 *
 */
@Configuration
public class AccountWithDrawServiceConfig extends DubboCustomerConfig {

	
	@Bean
	public ReferenceBean<AccountWithDrawOrderFacade> accountWithDrawOrderFacade() {
		
		ReferenceBean<AccountWithDrawOrderFacade> referenceBean=new ReferenceBean<AccountWithDrawOrderFacade>();
		referenceBean.setInterface(AccountWithDrawOrderFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getAccountWithdrawOrderById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(2);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getAccountWithdrawDetailById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(2);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("updateAccountWithdrawOrder");
		methodConfig.setTimeout(5000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		return referenceBean;
	}
}
