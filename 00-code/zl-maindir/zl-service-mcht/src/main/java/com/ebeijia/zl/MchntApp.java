package com.ebeijia.zl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;

@EnableCaching
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJms
@SpringBootApplication
public class MchntApp extends SpringBootServletInitializer implements WebApplicationInitializer {

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MchntApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MchntApp.class, args);
	}

	// 开发环境DataSource配置 使用druid数据源
	@Bean("dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource dataSource() {
		return new DruidDataSource();
	}


	// 提供SqlSeesion
	/*@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		
		sqlSessionFactoryBean.setDataSource(dataSource());
		// 分页插件
		PageHelper pageHelper = new PageHelper();
		Properties props = new Properties();
		props.setProperty("dialect", "oracle");
		props.setProperty("reasonable", "true");
		props.setProperty("supportMethodsArguments", "true");
		props.setProperty("returnPageInfo", "check");
		props.setProperty("params", "count=countSql");
		pageHelper.setProperties(props);
		// 添加插件
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { pageHelper });

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		// sqlSessionFactoryBean.setTypeAliasesPackage(myBatis.getTypeAliasesPackage());
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources(myBatis.getMapperLocations()));
		sqlSessionFactoryBean.setConfigLocation(resolver.getResource(myBatis.getConfigLocations()));
		return sqlSessionFactoryBean.getObject();
	}*/

	// 事务管理
	@Bean
	@ConditionalOnMissingBean
	public PlatformTransactionManager transactionManager() {
			return new DataSourceTransactionManager(dataSource());
	}
	
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

	
}