package com.ebeijia.zl;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication
public class WebApiApp extends SpringBootServletInitializer implements WebApplicationInitializer {

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebApiApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(WebApiApp.class, args);
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
}