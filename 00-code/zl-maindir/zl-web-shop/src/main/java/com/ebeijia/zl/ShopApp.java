package com.ebeijia.zl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class ShopApp extends SpringBootServletInitializer implements WebApplicationInitializer {
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShopApp.class);
	}
	
    public static void main(String[] args) {
        SpringApplication.run(ShopApp.class,args);
    }
}
