package com.cn.thinkx.ecom;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;

@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@SpringBootApplication
public class CmsApp extends SpringBootServletInitializer implements WebApplicationInitializer {

	@Autowired
	private MyBatisProps myBatis;
	@Autowired
	private Environment env;
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CmsApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CmsApp.class, args);
	}

	// 开发环境DataSource配置 使用druid数据源
	@Bean("dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource dataSource() {
		return new DruidDataSource();
	}


	// 提供SqlSeesion
	@Bean
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
	}

	// 事务管理
	@Bean
	@ConditionalOnMissingBean
	public PlatformTransactionManager transactionManager() {
			return new DataSourceTransactionManager(dataSource());
	}
	
	// 显示声明CommonsMultipartResolver为mutipartResolver
	@Bean(name = "multipartResolver")
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setResolveLazily(true);// resolveLazily属性启用是为了推迟文件解析，可以在controller中捕获文件大小异常
		resolver.setMaxInMemorySize(40960);
		resolver.setMaxUploadSize(50 * 1024 * 1024);// 上传文件大小 50M 50*1024*1024
		return resolver;
	}
	
}