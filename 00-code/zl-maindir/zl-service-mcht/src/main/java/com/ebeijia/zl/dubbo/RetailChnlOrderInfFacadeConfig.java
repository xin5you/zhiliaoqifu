package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
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
public class RetailChnlOrderInfFacadeConfig extends DubboProviderConfig {

	
	@Bean
	public ServiceBean<RetailChnlOrderInfFacade> retailChnlOrderInfFacade(RetailChnlOrderInfFacade retailChnlOrderInfFacade) {
		
		ServiceBean<RetailChnlOrderInfFacade> serviceBean=new ServiceBean<RetailChnlOrderInfFacade>();
		serviceBean.setInterface(RetailChnlOrderInfFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(retailChnlOrderInfFacade);
		serviceBean.setCluster("failfast");
		serviceBean.setTimeout(6000);
		serviceBean.setRetries(0);


		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlOrderInfById");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("saveRetailChnlOrderInf");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("updateRetailChnlOrderInf");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("deleteRetailChnlOrderInfById");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("proTelChannelOrder");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlOrderInfList");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("doRechargeMobileMsg");
		methodConfig.setTimeout(2000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlOrderInfByOuterId");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //


		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlOrderInfPage");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlOrderInf");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlOrderInfCount");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		methodConfig=new MethodConfig();
		methodConfig.setName("doTelRechargeBackNotify");
		methodConfig.setTimeout(6000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //

		return serviceBean;
	}
}
