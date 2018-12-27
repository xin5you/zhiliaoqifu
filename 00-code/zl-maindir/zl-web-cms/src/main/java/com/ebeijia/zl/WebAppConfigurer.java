package com.ebeijia.zl;

import com.ebeijia.zl.web.cms.base.interceptor.EcomInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptor = registry.addInterceptor(new EcomInterceptor());
		addInterceptor.excludePathPatterns("/error");
		addInterceptor.excludePathPatterns("/login");
		addInterceptor.excludePathPatterns("/doLogin");
		addInterceptor.excludePathPatterns("/authcode/**");
		addInterceptor.addPathPatterns("/**");
		super.addInterceptors(registry);
	}

}
