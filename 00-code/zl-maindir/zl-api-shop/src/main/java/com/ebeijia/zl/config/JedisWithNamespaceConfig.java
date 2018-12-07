package com.ebeijia.zl.config;

import com.cn.thinkx.ecom.redis.core.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.Self;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JedisWithNamespaceConfig {

    @Bean
    @Primary
    public JedisUtilsWithNamespace jedisConfig(@Autowired JedisUtilsWithNamespace jedis){
        jedis.setNamespace(Self.NAME);
        return jedis;
    }
}
