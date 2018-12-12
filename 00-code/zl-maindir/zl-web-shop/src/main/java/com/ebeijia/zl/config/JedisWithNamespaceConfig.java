package com.ebeijia.zl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;

@Configuration
public class JedisWithNamespaceConfig {

    @Bean
    @Primary
    public JedisUtilsWithNamespace jedisConfig(@Autowired JedisUtilsWithNamespace jedis){
        jedis.setNamespace(ShopConfig.ID);
        return jedis;
    }
}
