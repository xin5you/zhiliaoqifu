package com.ebeijia.zl;

import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableMQConfiguration
@EnableTransactionManagement
public class ShopApp extends SpringBootServletInitializer implements WebApplicationInitializer {
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShopApp.class);
	}
	
    public static void main(String[] args) {
        SpringApplication.run(ShopApp.class,args);
    }
}
