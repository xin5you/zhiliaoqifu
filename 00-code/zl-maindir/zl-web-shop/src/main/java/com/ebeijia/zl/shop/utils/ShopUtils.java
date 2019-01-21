package com.ebeijia.zl.shop.utils;

import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class ShopUtils {
    @Autowired
    private HttpSession session;

    @Autowired
    private JedisUtilsWithNamespace jedis;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MemberInfo getSession() {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(401, "超时了，请重新登录");
        }
        return memberInfo;
    }

    public String getBaseDict(String key) {
        String v;
//        v = (String) session.getAttribute("key");
//        if (v == null) {
        v = jedis.getJedis().hget("TB_BASE_DICT_KV", key);
//            session.setAttribute(key,v);
//        }
        return v;
    }

}
