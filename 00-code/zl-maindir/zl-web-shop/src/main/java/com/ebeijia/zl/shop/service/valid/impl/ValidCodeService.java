package com.ebeijia.zl.shop.service.valid.impl;

import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.config.ShopConfig;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidCodeService implements IValidCodeService {

    @Autowired
    JedisUtilsWithNamespace jedis;

    Logger logger = LoggerFactory.getLogger(ValidCodeService.class);

    @Override
    public Double getSession() {
        return jedis.getJedis().getResource().incrByFloat("shop_session", 1 + Math.random());
    }

    @Override
    public Integer sendPhoneValidCode(String phoneNum, String method) {
        validMethod(method);
        validPhoneNumber(phoneNum);
        String code = generateCode();
        //TODO MockData
        code = "1234";
        deliverAndSave(phoneNum,method,code);
        logger.info("向手机号%s发送%s验证码成功",phoneNum,method);
        throw new AdviceMessenger(200, "发送成功");
    }

    private void deliverAndSave(String phoneNum, String method, String code) {
        //TODO doMagic 调用API
        if (false){
            long del = jedis.del(method + ":" + phoneNum);
        }
        jedis.set(method+ "CODE:" + phoneNum,code,300);
    }


    private String generateCode() {
        int v = (int) (Math.random() * 10000);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(v);
        stringBuffer.append("0000");
        return stringBuffer.substring(0, 4);
    }

    @Override
    public void checkFrequency(String phoneNum, String method) {
        validMethod(method);
        String s = jedis.get(method +":"+ phoneNum);
        if (s != null){
            throw new AdviceMessenger(403,"发送太频繁了，请稍后");
        }
        jedis.set(method +":"+ phoneNum,"1",ShopConfig.PHONE_VAILD_INTERVAL);
    }

    @Override
    public boolean checkValidCode(String method, String phone, String pwd) {
        if (StringUtils.isEmpty(phone) ||StringUtils.isEmpty(pwd)){
            throw new AdviceMessenger(ResultState.FORBIDDEN,"请输入正确的参数");
        }
        String s = jedis.get(method + "CODE:" + phone);

        if (pwd!=null && pwd.equals(s)){
            return true;
        }
        return false;
    }


    private void validMethod(String method){
        if (PhoneValidMethod.LOGIN.equals(method)) {
            return;
        } else if (PhoneValidMethod.PAY.equals(method)) {
            return;
        }
        throw new AdviceMessenger(ResultState.FORBIDDEN,"参数无效");
    }

    private void validPhoneNumber(String phoneNum) {
        long phone = 0L;
        try {
            phone = Long.valueOf(phoneNum);
            if (phone / 10000000000L == 1L) {
                return;
            }
        } catch (Exception ignored) {
        }
        throw new AdviceMessenger(ResultState.FORBIDDEN,"手机号有误，请检查");
    }
}
