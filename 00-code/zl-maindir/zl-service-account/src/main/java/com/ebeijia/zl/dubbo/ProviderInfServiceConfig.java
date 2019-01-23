package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class ProviderInfServiceConfig extends DubboCustomerConfig {

	@Bean
	public ReferenceBean<ProviderInfFacade> providerInfFacade() {
		ReferenceBean<ProviderInfFacade> referenceBean=new ReferenceBean<ProviderInfFacade>();
		referenceBean.setInterface(ProviderInfFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		referenceBean.setTimeout(10000);
		referenceBean.setRetries(0);
		return referenceBean;
	}
}
