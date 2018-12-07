package com.cn.thinkx.wechat.base.wxapi.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cn.thinkx.wechat.base.wxapi.util.CalendarUtil;
import com.cn.thinkx.wechat.base.wxapi.util.WxConstants;
import com.ebeijia.zl.common.utils.tools.StringUtil;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisCluster;

/**
 * 缓存工具类； 目前使用 服务器内存的方式；
 * 
 * 1、开发者可以根据自己的需求使用不同的缓存方式,比如memcached 2、系统默认使用单个公众账号的缓存处理，如果有多个账号，请开发者自行处理
 * 
 */
@Configuration
public class WxMemoryCacheClient {
	
	@Autowired
	private  JedisCluster jedisCluster; 
	
	// 服务器内存的方式缓存account、accessToken、jsTicket
	private  Map<String, MpAccount> mpAccountMap = new HashMap<String, MpAccount>();
	public final  String MP_ACCOUNT_KEY = "MP_ACCOUNT_KEY_";

	private  Map<String, AccessToken> accountAccessTokenMap = new HashMap<String, AccessToken>();
	public final  String ACCOUNT_ACCESS_TOKENKEY = "ACCOUNT_ACCESS_TOKEN_KEY_";

	private  Map<String, JSTicket> accountJSTicketMap = new HashMap<String, JSTicket>();
	public final  String JS_TICKET_KEY = "JS_TICKET_KEY_";

	// 微信OAuth认证的时候，服务器内存的方式缓存openid; key=sessionid ，value=openid
	// private  Map<String,String> sessionOpenIdMap = new
	// HashMap<String,String>();
	private  Map<String, OAuthAccessToken> accountOAuthTokenMap = new HashMap<String, OAuthAccessToken>();
	public final  String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY_";

	public  void addMpAccount(MpAccount account) {
		if (account != null && !mpAccountMap.containsKey(account.getAccount())) {
			mpAccountMap.put(account.getAccount(), account);
		}
	}

	public  MpAccount getMpAccount(String account) {
		return mpAccountMap.get(account);
	}

	// 获取唯一的公众号,如果需要多账号，请自行处理
	public  MpAccount getSingleMpAccount() {
		MpAccount sigleAccount = null;
		for (String key : mpAccountMap.keySet()) {
			sigleAccount = mpAccountMap.get(key);
			break;
		}
		return sigleAccount;
	}

	public  AccessToken addAccessToken(String account, AccessToken token) {
		if (token != null) {
			token.setCreateTime(CalendarUtil.getTimeInSeconds());
				jedisCluster.set(ACCOUNT_ACCESS_TOKENKEY + account, JSONObject.fromObject(token).toString());
				
		}
		return token;
	}

	/**
	 * accessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * 
	 * @param account
	 * @return
	 */
	public  AccessToken getAccessToken(String account) {
			String jsonStr = jedisCluster.get(ACCOUNT_ACCESS_TOKENKEY + account);
			return (AccessToken) JSONObject.toBean(JSONObject.fromObject(jsonStr), AccessToken.class);
		
	}

	/**
	 * 获取唯一的公众号的accessToken,如果需要多账号，请自行处理
	 * accessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * 
	 * @return
	 */
	public  AccessToken getSingleAccessToken(String account) {
			String jsonStr = jedisCluster.get(ACCOUNT_ACCESS_TOKENKEY + account);
			return (AccessToken) JSONObject.toBean(JSONObject.fromObject(jsonStr), AccessToken.class);
	}

	/**
	 * 添加JSTicket到缓存
	 * 
	 * @param account
	 * @param jsTicket
	 * @return
	 */
	public  JSTicket addJSTicket(String account, JSTicket jsTicket) {
		if (jsTicket != null) {
			jsTicket.setCreateTime(CalendarUtil.getTimeInSeconds());
			jedisCluster.set(JS_TICKET_KEY + account, JSONObject.fromObject(jsTicket).toString());
		}
		return jsTicket;
	}

	/**
	 * JSTicket的获取，绝对不要从缓存中直接获取，请从JSTicket中获取；
	 * 
	 * @param account
	 * @return
	 */
	public  JSTicket getJSTicket(String account) {
		String jsonStr = jedisCluster.get(JS_TICKET_KEY + account);
		return (JSTicket) JSONObject.toBean(JSONObject.fromObject(jsonStr), JSTicket.class);
	}

	/**
	 * 获取唯一的公众号的JSTicket,如果需要多账号，请自行处理
	 * JSTicket的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * 
	 * @return
	 */
	public  JSTicket getSingleJSTicket(String account) {
			String jsonStr = jedisCluster.get(JS_TICKET_KEY + account);
			return (JSTicket) JSONObject.toBean(JSONObject.fromObject(jsonStr), JSTicket.class);
	}

	// //处理openid缓存
	// public  String getOpenid(String sessionid){
	// if(!StringUtils.isBlank(sessionid)){
	// return sessionOpenIdMap.get(sessionid);
	// }
	// return null;
	// }

	// 处理openid缓存
	public  String getOpenid(HttpServletRequest request) {
		return StringUtil.nullToString(request.getSession().getAttribute(WxConstants.OPENID_SESSION_KEY));
	}

	// public  String setOpenid(String sessionid, String openid){
	// if(!StringUtils.isBlank(sessionid) && !StringUtils.isBlank(openid)){
	// sessionOpenIdMap.put(sessionid, openid);
	// }
	// return openid;
	// }

	public  String setOpenid(HttpServletRequest request, String openid) {
		request.getSession().setAttribute(WxConstants.OPENID_SESSION_KEY, openid);
		return openid;
	}

	// 处理OAuth的Token
	public  AccessToken addOAuthAccessToken(String account, OAuthAccessToken token) {
		if (token != null) {
			
			jedisCluster.set(ACCESS_TOKEN_KEY + account, JSONObject.fromObject(token).toString());
			
		}
		return token;
	}

	/**
	 * OAuthAccessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * 
	 * @param account
	 * @return
	 */
	public  OAuthAccessToken getOAuthAccessToken(String account) {
		String jsonStr = jedisCluster.get(ACCESS_TOKEN_KEY + account);
		return (OAuthAccessToken) JSONObject.toBean(JSONObject.fromObject(jsonStr), OAuthAccessToken.class);
	}

	/**
	 * 获取唯一的公众号的accessToken,如果需要多账号，请自行处理
	 * OAuthAccessToken的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * 
	 * @return
	 */
	public  OAuthAccessToken getSingleOAuthAccessToken(String account) {
			String jsonStr = jedisCluster.get(ACCESS_TOKEN_KEY + account);
			return (OAuthAccessToken) JSONObject.toBean(JSONObject.fromObject(jsonStr), OAuthAccessToken.class);
	}

	/**
	 * 获取所有的公众号号 All MpAccount的获取，绝对不要从缓存中直接获取，请从WxApiClient中获取；
	 * 
	 * @return
	 */
	public  List<MpAccount> getAllMpAccountMaps() {
		Collection<MpAccount> valueCollection = mpAccountMap.values();
		List<MpAccount> list = new ArrayList<MpAccount>(valueCollection);
		return list;
	}
}
