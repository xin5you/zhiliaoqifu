package com.ebeijia.zl.shop.utils;

import com.cn.thinkx.ecom.redis.core.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Session AOP切面
 */
@Component
@Aspect
public class ShopAop {

    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Autowired
    HttpSession session;

    @Autowired
    JedisUtilsWithNamespace jedis;

    ObjectMapper jsonMapper = new ObjectMapper();

    Logger logger = LoggerFactory.getLogger(ShopAop.class);

    @Around(value = "@annotation(com.ebeijia.zl.shop.utils.TokenCheck)")
    public Object tokenAccess(ProceedingJoinPoint pj) throws Throwable {
        //TODO 优化性能，避免执行不必要的逻辑
            doLog(pj);
            String token = getToken();
            if (token != null) {
                String s = jedis.get(token);
                logger.info("[user:" + s + "]");
                //TODO fake login user
                session.setAttribute("userId", s);
            }
        Object proceed = pj.proceed();
        return proceed;
    }

    private void doLog(ProceedingJoinPoint pj) {
        String path = request.getContextPath();
        String fullpath = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + path + "/";
        MethodSignature signature = (MethodSignature) pj.getSignature();
        logger.info("\n[FullPath:" + fullpath + "]" +
                "\n[signature:" + signature.toLongString() + "]");
    }


    @Around(value = "@annotation(com.ebeijia.zl.shop.utils.SessionCheck)")
    public Object repeatAvoid(ProceedingJoinPoint pj) throws Throwable {
        doLog(pj);

        //初始化
        Double nextSession = jedis.getJedis().getResource().incrByFloat("shop_session", 1 + Math.random());
        boolean useCache = isUseCache(pj);
        String sessionId = getSession();
        MethodSignature signature = (MethodSignature) pj.getSignature();

        System.out.println(nextSession);

        //处理缓存
        if (sessionId!=null && useCache) {
            String json = jedis.get(signature.toLongString() + sessionId);
            if (json!=null){
                if (json == "undefined") {
                    return new JsonResult<>().setCode(403).setMessage("请求正在处理");
                }
                return json;
            }
        }

        Object proceed = pj.proceed();
        return proceed;
    }

    private String getSession() {
        String session = null;
        //从URL获取
        session = request.getParameter("post_session");
        if (session != null) {
            return session;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName() == "post_session") {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    private boolean isUseCache(ProceedingJoinPoint pj) {
        MethodSignature signature = (MethodSignature) pj.getSignature();
        SessionCheck annotation = signature.getMethod().getAnnotation(SessionCheck.class);
        if (annotation != null){
            return annotation.cache();
        }
        return false;
    }

    private String getToken() {
        String token = null;
        //从请求头获取
        Enumeration e1 = request.getHeaderNames();
        while (e1.hasMoreElements()) {
            String headerName = (String) e1.nextElement();
            System.out.println(headerName);
            if (headerName.equals("Authorization") || headerName.equals("authorization")) {
                return request.getHeader(headerName);
            }
        }
        //从URL获取
        token = request.getParameter("token");
        if (token != null) {
            return token;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName() == "token") {
                    return c.getValue();
                }
            }
        }
        return null;
    }


}