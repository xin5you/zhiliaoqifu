package com.ebeijia.zl.web.oms.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class RetailChnlItemListServiceConfig extends DubboCustomerConfig {

	
	@Bean
	public ReferenceBean<RetailChnlItemListFacade> retailChnlItemListFacade() {
		
		ReferenceBean<RetailChnlItemListFacade> referenceBean=new ReferenceBean<RetailChnlItemListFacade>();
		referenceBean.setInterface(RetailChnlItemListFacade.class.getName());
		referenceBean.setVersion("1.0.0");
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		
		List<MethodConfig> methods = new ArrayList<MethodConfig>();

		
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlItemListById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("saveRetailChnlItemList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("updateRetailChnlItemList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("deleteRetailChnlItemListById");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		
		methodConfig=new MethodConfig();
		methodConfig.setName("deleteByProductId");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlItemList");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		methodConfig=new MethodConfig();
		methodConfig.setName("getRetailChnlItemListPage");
		methodConfig.setTimeout(3000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		return referenceBean;
	}
}
