package com.ebeijia.zl.dubbo;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.ebeijia.zl.facade.user.service.UserInfFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用服务暴露的接口配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class DubboUserInfServiceConfig extends DubboCustomerConfig {

	@Bean
	public ReferenceBean<UserInfFacade> userInfFacade() {
		ReferenceBean<UserInfFacade> referenceBean=new ReferenceBean<UserInfFacade>();
		referenceBean.setVersion("1.0.0");
		referenceBean.setInterface(UserInfFacade.class.getName());
		referenceBean.setCluster("failfast");
		referenceBean.setCheck(false);
		
		List<MethodConfig> methods=new ArrayList<MethodConfig>();
		
		/*** dubbo method registerUserInf config*/
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("registerUserInf");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		
		referenceBean.setMethods(methods);
		return referenceBean;
	}
}
