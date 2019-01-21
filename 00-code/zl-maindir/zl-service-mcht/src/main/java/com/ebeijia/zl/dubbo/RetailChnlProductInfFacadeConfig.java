package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;

import java.util.ArrayList;
import java.util.List;

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
		serviceBean.setTimeout(6000);
		serviceBean.setRetries(0);


		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlProductInfById");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("saveRetailChnlProductInf");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("saveTelChannelProductForId");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("updateRetailChnlProductInf");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("deleteRetailChnlProductInfById");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getProductRateByMaps");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlProductInfList");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlProductInfPage");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getChannelProductByItemId");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getChannelProductListByChannelId");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		return serviceBean;
	}
}
