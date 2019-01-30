package com.ebeijia.zl.web.oms.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.account.service.AccountWithDrawOrderFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务暴露的接口 配置
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
		referenceBean.setTimeout(10000);
		referenceBean.setRetries(0);
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawOrderList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(2);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawDetailList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(2);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawOrderPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getWithdrawDetailPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		return referenceBean;
	}
}
