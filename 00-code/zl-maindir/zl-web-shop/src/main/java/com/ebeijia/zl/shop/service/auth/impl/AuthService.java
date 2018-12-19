package com.ebeijia.zl.shop.service.auth.impl;

import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.service.auth.IAuthService;
import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.vo.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService implements IAuthService {
    private static Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private JedisClusterUtils jedis;

    @Autowired
    private JedisUtilsWithNamespace jedis2;

    @Autowired
    private IValidCodeService validCodeService;

    @Override
    public Token phoneLogin(String phone, String pwd) {

        //TODO 验证码校验
        boolean validCode = validCodeService.checkValidCode(PhoneValidMethod.LOGIN, phone, pwd);
        if (!validCode){
            throw new AdviceMessenger(403,"验证码有误");
        }
        //测试用
        HashMap<String, String> token = new HashMap<>();
        token.put("userid","TT233");
        token.put("token","testToken");



        //将获取到的token存入redis缓存;
        jedis2.set(token.get("token"),token.get("userid"),3600*24);
        //前端测试用
        return new Token(token.get("token"));
    }

}
