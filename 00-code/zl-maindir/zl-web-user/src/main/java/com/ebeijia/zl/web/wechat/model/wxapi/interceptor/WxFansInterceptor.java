package com.ebeijia.zl.web.wechat.model.wxapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ebeijia.zl.basics.wechat.domain.AccountFans;
import com.ebeijia.zl.basics.wechat.service.AccountFansService;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.wechat.process.MpAccount;
import com.ebeijia.zl.core.wechat.process.WxMemoryCacheClient;
import com.ebeijia.zl.web.wechat.model.utils.HttpRequestDeviceUtils;
import com.ebeijia.zl.web.wechat.model.utils.HttpWebUtil;
import com.ebeijia.zl.web.wechat.model.wxapi.service.BizService;

/**
 * 微信客户端用户请求验证拦截器
 */
public class WxFansInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private AccountFansService accountFansService;

	@Autowired
	@Qualifier("bizService")
	private BizService bizService;
	
	@Autowired
	private WxMemoryCacheClient wxMemoryCacheClient;

	/**
	 * 开发者自行处理拦截逻辑， 方便起见，此处只处理includes
	 */
	private String[] excludes;// 不需要拦截的
	private String[] includes;// 需要拦截的

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();

		boolean oauthFlag = false;// 为方便展示的参数，开发者自行处理
		for (String s : includes) {
			if (uri.contains(s)) {// 如果包含，就拦截
				oauthFlag = true;
				break;
			}
		}

		if (!oauthFlag) {// 如果不需要oauth认证
			return true;
		}
		if (!HttpRequestDeviceUtils.isMicroMessenger(request)) { // 判读是否是微信浏览器访问
			HttpWebUtil.redirectUrl(request, response, "/base/unvalidated.html");
			return false;
		}

		String openid = wxMemoryCacheClient.getOpenid(request);// 先从缓存中获取openid
		if (StringUtil.isNullOrEmpty(openid)) {
			return true;
		}

		AccountFans accountFans = accountFansService.getByOpenId(openid); // 获取粉丝信息
		if (accountFans == null || accountFans.getSubscribestatus() == 0) { // 判断是否已经关注了公众号
			MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();
			accountFans = bizService.syncAccountFans(openid, mpAccount, true);
			if (accountFans == null || accountFans.getSubscribestatus() == 0) {
				HttpWebUtil.redirectUrl(request, response, "/base/fansTips.html");
				return false;
			}
		}
		return true;
	}

	public String[] getExcludes() {
		return excludes;
	}

	public void setExcludes(String[] excludes) {
		this.excludes = excludes;
	}

	public String[] getIncludes() {
		return includes;
	}

	public void setIncludes(String[] includes) {
		this.includes = includes;
	}

}
