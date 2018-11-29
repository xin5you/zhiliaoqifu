package com.cn.thinkx.ecom.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cn.thinkx.ecom.common.constants.Constants;

public class EcomInterceptor extends HandlerInterceptorAdapter {

	private static String[] COMMON_AUTH_URL = { "/logout", "/main", "/error", "/login", "/unauthorized",
			"/system/user/updatePwdCommit", "/unifiedOrder" };

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Object o = session.getAttribute(Constants.SESSION_USER);
		if (o == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}

		String method = request.getMethod();// 获得当前请求的方式
		String url = request.getRequestURI();// 获得当前请求的路径

		if ("get".equalsIgnoreCase(method)) {// 判断当前请求路径方式是否是get方式
												// equalsIgnoreCase()忽略大小写
			for (String c : COMMON_AUTH_URL) {// 循环判断当前请求的路径是否包含公共权限路径
				if (url.indexOf(c) != -1)// 当前请求的路径包含公共权限路径就通过拦截器
					return super.preHandle(request, response, handler);
			}

			String[] UserAuthUrlArray = (String[]) session.getAttribute("USER_AUTH_URL_ARRAY");// 用户权限路径数组
			int flag = 0;// 权限判断标识
			if (UserAuthUrlArray != null && UserAuthUrlArray.length > 0) {// 判断用户权限路径数组是否为空
				for (String u : UserAuthUrlArray) {// 循环判断当前请求路径是否包含用户权限路径数组
					if (url.indexOf(u) != -1) {// 有权限
						flag = 1;
						break;
					}
				}
				if (flag == 0) {// 没权限
					response.sendRedirect(request.getContextPath() + "/unauthorized");
					return false;
				}
			} else {
				response.sendRedirect(request.getContextPath() + "/unauthorized");
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}

}
