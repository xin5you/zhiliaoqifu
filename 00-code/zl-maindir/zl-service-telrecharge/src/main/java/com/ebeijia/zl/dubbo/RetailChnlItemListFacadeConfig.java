package com.ebeijia.zl.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class RetailChnlItemListFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<RetailChnlItemListFacade> RetailChnlItemListFacade(RetailChnlItemListFacade retailChnlItemListFacade) {
		
		ServiceBean<RetailChnlItemListFacade> serviceBean=new ServiceBean<RetailChnlItemListFacade>();
		serviceBean.setInterface(RetailChnlItemListFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(retailChnlItemListFacade);
		serviceBean.setCluster("failfast");
		return serviceBean;
	}
}
