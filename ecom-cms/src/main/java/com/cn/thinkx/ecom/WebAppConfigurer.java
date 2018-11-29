package com.cn.thinkx.ecom;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cn.thinkx.ecom.base.interceptor.EcomInterceptor;

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
