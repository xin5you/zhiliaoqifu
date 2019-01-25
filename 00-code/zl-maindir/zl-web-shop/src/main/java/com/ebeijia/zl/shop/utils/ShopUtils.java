package com.ebeijia.zl.shop.utils;

import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;

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

    public String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(System.currentTimeMillis());
        return dateTime;
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 16; i++) {
            String str = uuid.substring(i * 2, i * 2 + 2);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

}
