package com.ebeijia.zl;

import com.ebeijia.zl.web.user.model.wxapi.interceptor.WxOAuth2Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

	@Bean
	public WxOAuth2Interceptor getFrontInterceptor() {
		return new WxOAuth2Interceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptor = registry.addInterceptor(getFrontInterceptor());
		addInterceptor.excludePathPatterns("/w/msg");
		addInterceptor.excludePathPatterns("/w/jsTicket");
		addInterceptor.excludePathPatterns("/w/getOpenId");
		super.addInterceptors(registry);
	}

}
