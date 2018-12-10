package com.cn.thinkx.ecom.front.api.interceptor;

import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 拦截器
 * @author zhuqiuyou
 *
 */
@Component
public class FrontInterceptor extends HandlerInterceptorAdapter {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		//front web user session
		String memberId=StringUtil.nullToString(request.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		
		//如果没有会员信息，则
		if(StringUtil.isNullOrEmpty(memberId)){
		       String url = "/ecom/hkbstore/index";
		       response.sendRedirect(url);
		       return false;
		}
		return true;
	}
}