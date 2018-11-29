package com.cn.thinkx.ecom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cn.thinkx.ecom.front.api.interceptor.FrontInterceptor;


@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

	@Bean
	public FrontInterceptor getFrontInterceptor() { 
		return new FrontInterceptor(); 
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptor = registry.addInterceptor(getFrontInterceptor());
		addInterceptor.excludePathPatterns("/error");
		addInterceptor.excludePathPatterns("/login");
		addInterceptor.excludePathPatterns("/doLogin");
		addInterceptor.excludePathPatterns("/authcode/**");
		addInterceptor.excludePathPatterns("/ecom/hkbstore/**");
		addInterceptor.excludePathPatterns("/ecom/channel/**");
//		addInterceptor.excludePathPatterns("/you163/**");
		addInterceptor.addPathPatterns("/ecom/goods/**","/ecom/cart/**","/ecom/member/**");
		super.addInterceptors(registry);
	}

}
