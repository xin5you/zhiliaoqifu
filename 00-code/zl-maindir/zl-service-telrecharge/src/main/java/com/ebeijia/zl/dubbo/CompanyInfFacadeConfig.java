package com.ebeijia.zl.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class CompanyInfFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<CompanyInfFacade> companyInfFacade(CompanyInfFacade companyInfFacade) {
		
		ServiceBean<CompanyInfFacade> serviceBean=new ServiceBean<CompanyInfFacade>();
		serviceBean.setInterface(CompanyInfFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(companyInfFacade);
		serviceBean.setCluster("failfast");
		return serviceBean;
	}
}
