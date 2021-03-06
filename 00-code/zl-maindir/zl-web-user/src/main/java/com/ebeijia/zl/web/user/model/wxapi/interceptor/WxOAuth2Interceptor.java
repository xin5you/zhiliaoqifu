package com.ebeijia.zl.web.user.model.wxapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebeijia.zl.web.user.model.utils.HttpWebUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.wechat.process.MpAccount;
import com.ebeijia.zl.core.wechat.process.OAuthScope;
import com.ebeijia.zl.core.wechat.process.WxApi;
import com.ebeijia.zl.core.wechat.process.WxApiClient;
import com.ebeijia.zl.core.wechat.process.WxMemoryCacheClient;

/**
 * 微信客户端用户请求验证拦截器
 */
@Component
public class WxOAuth2Interceptor extends HandlerInterceptorAdapter {

	/**
	 * 开发者自行处理拦截逻辑， 方便起见，此处只处理includes
	 */
	private String[] excludes;// 不需要拦截的

	
	
	@Autowired
	private WxMemoryCacheClient wxMemoryCacheClient;
	
	@Autowired
	private WxApiClient wxApiClient;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();

		for (String s : excludes) {
			if (uri.contains(s)) {// 如果不需要包含
				return true;
			}
		}

		String openid = wxMemoryCacheClient.getOpenid(request);// 先从缓存中获取openid

		if (!StringUtils.isBlank(openid)) {// 没有，通过微信页面授权获取
			return true;
		}

		if (StringUtils.isBlank(openid)) {// 没有，通过微信页面授权获取

			String code = request.getParameter("code");

			if (!StringUtils.isBlank(code)) {// 如果request中包括code，则是微信回调
				try {
					
					openid = wxApiClient.getOAuthOpenId(wxMemoryCacheClient.getSingleMpAccount(), code);
					if (!StringUtils.isBlank(openid)) {
						openid = StringUtil.trim(openid);
						wxMemoryCacheClient.setOpenid(request,openid);// 缓存openid
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {// oauth获取code
				MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();// 获取缓存中的唯一账号
				String redirectUrl = HttpWebUtil.getRequestFullUriNoContextPath(request);// 请求code的回调url
				String state = OAuth2RequestParamHelper.prepareState(request);
				//带参数的路径请求
				String redirectParam=StringUtil.nullToString(request.getQueryString());
				if(!"".equals(redirectParam)){
					redirectUrl= redirectUrl+"?"+redirectParam;
				}
				String url = WxApi.getOAuthCodeUrl(mpAccount.getAppid(),redirectUrl, OAuthScope.Base.toString(), state);
				HttpWebUtil.redirectHttpUrl(request, response, url);
				return false;
			}
		} else {
			// System.out.println("#### WxOAuth2Interceptor Session : openid = " + openid);
			return true;
		}
		return false;
	}

}
