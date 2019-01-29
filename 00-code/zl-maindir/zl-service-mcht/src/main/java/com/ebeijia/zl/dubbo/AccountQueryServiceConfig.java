package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
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
public class AccountQueryServiceConfig extends DubboCustomerConfig {

	@Bean
	public ReferenceBean<AccountQueryFacade> accountQueryFacade() {
		
		ReferenceBean<AccountQueryFacade> referenceBean=new ReferenceBean<AccountQueryFacade>();
		referenceBean.setVersion("1.0.0");
		referenceBean.setInterface(AccountQueryFacade.class.getName());
		referenceBean.setCluster("failfast");
		referenceBean.setTimeout(6000);
		referenceBean.setRetries(0);
		return referenceBean;
	}
}
