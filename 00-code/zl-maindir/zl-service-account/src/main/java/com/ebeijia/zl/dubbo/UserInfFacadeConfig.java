package com.ebeijia.zl.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ebeijia.zl.facade.user.service.UserInfFacade;

/**
 * 服务暴露的接口 配置
 * @author zhuqiuyou
 *
 */
@Configuration
public class UserInfFacadeConfig extends DubboProviderConfig {

	@Bean
	public ServiceBean<UserInfFacade> userInfFacade(UserInfFacade userInfFacade) {
		
		ServiceBean<UserInfFacade> serviceBean=new ServiceBean<UserInfFacade>();
		serviceBean.setInterface(UserInfFacade.class.getName());
		serviceBean.setVersion("1.0.0");
		serviceBean.setRef(userInfFacade);
		serviceBean.setCluster("failfast");
		serviceBean.setTimeout(10000);
		serviceBean.setRetries(0);
		
		List<MethodConfig> methods=new ArrayList<MethodConfig>();
		
		/*** dubbo method registerUserInf config*/
		MethodConfig methodConfig=new MethodConfig();
		methodConfig.setName("registerUserInf");
		methodConfig.setTimeout(10000);
		methodConfig.setRetries(0);
		methods.add(methodConfig); //
		serviceBean.setMethods(methods);
		
		return serviceBean;
	}
}
