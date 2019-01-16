package com.ebeijia.zl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

@EnableCaching
@EnableTransactionManagement
@EnableAutoConfiguration
@SpringBootApplication
public class ControlApp extends SpringBootServletInitializer implements WebApplicationInitializer {


	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ControlApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ControlApp.class, args);
	}

	// 开发环境DataSource配置 使用druid数据源
/*
	@Bean("dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource dataSource() {
		return new DruidDataSource();
	}
*/


	// 提供SqlSeesion
//	@Bean
//	@ConditionalOnMissingBean
//	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		
//		sqlSessionFactoryBean.setDataSource(dataSource());
//		return sqlSessionFactoryBean.getObject();
//	}

	// 事务管理
/*	@Bean
	@ConditionalOnMissingBean
	public PlatformTransactionManager transactionManager() {
			return new DataSourceTransactionManager(dataSource());
	}
	*/
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

	@Bean
    public IAcsClient acsClient() throws ClientException {
		//产品名称:云通信短信API产品,开发者无需替换
		String product = "Dysmsapi";
		//产品域名,开发者无需替换
		String domain = "dysmsapi.aliyuncs.com";
		// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
		String accessKeyId = "LTAIrrwbSybL466i";
		String accessKeySecret = "sDtHQg4GN5TWl5rzSNoYCigHejt1jS";
		//初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		return acsClient;
	}
	
}