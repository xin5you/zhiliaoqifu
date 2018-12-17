package com.ebeijia.zl.shop.service.valid.impl;

import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
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

    @Override
    public Integer sendPhoneValidCode(String phoneNum, String method) {
        if (!isPhoneNumber(phoneNum)) {
            throw new AdviceMessenger(406, "出错了！请检查手机号");
        }

        if (PhoneValidMethod.LOGIN.equals(method)) {

        } else if (PhoneValidMethod.PAY.equals(method)) {

        }
        throw new AdviceMessenger(200, "发送成功");
    }

    private boolean isPhoneNumber(String phoneNum) {
        long phone = 0L;
        try {
            phone = Long.valueOf(phoneNum);
            if (phone / 10000000000L == 1L) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }
}
