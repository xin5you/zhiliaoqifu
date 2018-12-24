package com.ebeijia.zl.shop.utils;

import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.vo.MemberInfo;
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

import static com.ebeijia.zl.shop.constants.ResultState.NOT_ACCEPTABLE;

/**
 * Session AOP切面
 */
@Component
@Aspect
public class ShopAop {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;

    @Autowired
    private JedisUtilsWithNamespace jedis;

    @Autowired
    ShopUtils shopUtils;
    private static Logger logger = LoggerFactory.getLogger(ShopAop.class);

    @Around(value = "@annotation(com.ebeijia.zl.shop.utils.TokenCheck)")
    public Object tokenAccess(ProceedingJoinPoint pj) throws Throwable {
        //TODO 优化性能，避免执行不必要的逻辑
        doLog(pj);
        String token = getToken();
        logger.info(token);

        if (token != null) {
            String s = jedis.get(token);
            logger.info("[user:" + s + "]");
            //TODO fake login user
            if (StringUtils.isEmpty(s)) {
                throw new BizException(NOT_ACCEPTABLE, "参数异常");
            }
            //获得身份信息
            MemberInfo memberInfo = shopUtils.readValue(s, MemberInfo.class);
            session.setAttribute("user", memberInfo);
        } else if (isForceLogin(pj)) {
            session.setAttribute("user", null);
            throw new AdviceMessenger(ResultState.UNAUTHORIZED, "请登录后再试");
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
        if (sessionId != null && useCache) {
            String json = jedis.get(signature.toLongString() + sessionId);
            if (json != null) {
                if (json == "undefined") {
                    throw new AdviceMessenger(403, "处理中，请稍后");
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
        if (annotation != null) {
            return annotation.cache();
        }
        return false;
    }

    private boolean isForceLogin(ProceedingJoinPoint pj) {
        MethodSignature signature = (MethodSignature) pj.getSignature();
        TokenCheck annotation = signature.getMethod().getAnnotation(TokenCheck.class);
        if (annotation != null) {
            return annotation.force();
        }
        return true;
    }

    private String getToken() {
        String token = null;
        //从请求头获取
        Enumeration e1 = request.getHeaderNames();
        while (e1.hasMoreElements()) {
            String headerName = (String) e1.nextElement();
            System.out.print("/" + headerName);
            System.out.println("");
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
