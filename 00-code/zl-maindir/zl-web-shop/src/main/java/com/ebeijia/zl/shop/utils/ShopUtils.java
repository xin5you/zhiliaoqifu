package com.ebeijia.zl.shop.utils;

import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class ShopUtils {
    @Autowired
    HttpSession session;

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
        if (memberInfo==null){
            throw new BizException(401, "超时了，请重新登录");
        }
        return memberInfo;
    }

}
