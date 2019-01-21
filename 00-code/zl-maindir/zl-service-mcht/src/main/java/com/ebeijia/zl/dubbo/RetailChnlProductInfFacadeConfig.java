package com.ebeijia.zl.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class RetailChnlProductInfFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<RetailChnlProductInfFacade> RetailChnlProductInfFacade(RetailChnlProductInfFacade retailChnlProductInfFacade) {
		
		ServiceBean<RetailChnlProductInfFacade> serviceBean=new ServiceBean<RetailChnlProductInfFacade>();
		serviceBean.setInterface(RetailChnlProductInfFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(retailChnlProductInfFacade);
		serviceBean.setCluster("failfast");
		return serviceBean;
	}
}
