package com.ebeijia.zl.shop.auth.service.impl;

import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.ebeijia.zl.shop.auth.service.IAuthService;
import com.ebeijia.zl.shop.constants.Self;
import com.ebeijia.zl.shop.vo.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService implements IAuthService {
    @Autowired
    JedisClusterUtils jedis;

    @Override
    public Token phoneLogin(String phone, String pwd) {
        //测试用
        HashMap<String, String> token = new HashMap<>();
        token.put("userid","TT233");
        token.put("token","testToken");
        //将获取到的token存入redis缓存;
        jedis.set(Self.name+token.get("token"),token.get("userid"),3600*24);
        //前端测试用
        return new Token(token.get("token"));
    }
}
