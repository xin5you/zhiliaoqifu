package com.ebeijia.zl.shop.service.valid.impl;

import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidCodeService implements IValidCodeService {

    @Autowired
    JedisUtilsWithNamespace jedis;

    @Override
    public Double getSession() {
        return jedis.getJedis().getResource().incrByFloat("shop_session", 1 + Math.random());
    }
}
