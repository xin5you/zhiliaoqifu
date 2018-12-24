package com.ebeijia.zl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

	@Value("${spring.mvc.view.prefix}")
	private String prefix = "";
	@Value("${spring.mvc.view.suffix}")
	private String suffix = "";
//	@Value("${spring.mvc.view.view-name}")
//	private String viewName = "";

	/* @Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("/WEB-INF/**").addResourceLocations("classpath:/webapp/WEB-INF/view/");
	 }*/

	/*@Bean
	 public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix(prefix);
		viewResolver.setSuffix(suffix);
//		viewResolver.setViewNames(viewName);
//		viewResolver.setViewClass(JstlView.class);
		return viewResolver;
	}*/


	/*@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration addInterceptor = registry.addInterceptor(new OmsInterceptor());
		addInterceptor.excludePathPatterns("/error");
		addInterceptor.excludePathPatterns("/login/loginIndex");
		addInterceptor.excludePathPatterns("/login/doLogin");
		addInterceptor.excludePathPatterns("/authcode/**");
		addInterceptor.addPathPatterns("/**");
		super.addInterceptors(registry);
	}*/

}
