package com.ebeijia.zl.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
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
		
		List<MethodConfig> methods=new ArrayList<MethodConfig>();
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getCompanyInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);
		
		methodConfig=new MethodConfig();
		methodConfig.setName("insertCompanyInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("updateCompanyInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("deleteCompanyInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getCompanyInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getCompanyInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getCompanyInfByLawCode");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		return serviceBean;
	}
}
