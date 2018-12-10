package com.ebeijia.zl.config;

import com.cn.thinkx.ecom.redis.core.utils.JedisUtilsWithNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JedisWithNamespaceConfig {

    @Bean
    @Primary
    public JedisUtilsWithNamespace jedisConfig(@Autowired JedisUtilsWithNamespace jedis){
        jedis.setNamespace(ShopConfig.ID);
        return jedis;
    }
}
