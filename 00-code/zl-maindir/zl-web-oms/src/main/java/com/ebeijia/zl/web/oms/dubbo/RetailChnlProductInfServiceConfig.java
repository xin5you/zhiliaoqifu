package com.ebeijia.zl.web.oms.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class RetailChnlProductInfServiceConfig extends DubboCustomerConfig {

	
	@Bean
	public ReferenceBean<RetailChnlProductInfFacade> retailChnlProductInfFacade() {
		
		ReferenceBean<RetailChnlProductInfFacade> referenceBean=new ReferenceBean<RetailChnlProductInfFacade>();
		referenceBean.setInterface(RetailChnlProductInfFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlProductInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("saveRetailChnlProductInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("saveTelChannelProductForId");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("updateRetailChnlProductInf");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("deleteRetailChnlProductInfById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getProductRateByMaps");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlProductInfList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlProductInfPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getChannelProductByItemId");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getChannelProductListByChannelId");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		return referenceBean;
	}
}
