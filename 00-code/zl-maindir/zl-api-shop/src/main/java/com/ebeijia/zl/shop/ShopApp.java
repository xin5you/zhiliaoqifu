package com.ebeijia.zl.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.ebeijia.zl","com.cn.thinkx.ecom.redis"})
@EnableTransactionManagement
public class ShopApp {
    public static void main(String[] args) {
        SpringApplication.run(ShopApp.class,args);
    }
}