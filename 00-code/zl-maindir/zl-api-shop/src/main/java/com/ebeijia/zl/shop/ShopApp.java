package com.ebeijia.zl.shop;

import com.cn.thinkx.ecom.redis.core.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.Self;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.ebeijia.zl","com.cn.thinkx.ecom.redis"})
@EnableTransactionManagement
public class ShopApp {
    public static void main(String[] args) {
        SpringApplication.run(ShopApp.class,args);
    }

    @Bean
    @Primary
    public JedisUtilsWithNamespace jedisConfig(@Autowired JedisUtilsWithNamespace jedis){
        jedis.setNamespace(Self.name);
        return jedis;
    }

}
