package com.ebeijia.zl.web.user.model.customer.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.RedisDictProperties;
import com.ebeijia.zl.core.wechat.process.*;
import com.ebeijia.zl.core.wechat.util.WxSignUtil;
import com.ebeijia.zl.core.wechat.vo.MsgRequest;
import com.ebeijia.zl.core.wechat.vo.SemaphoreMap;
import com.ebeijia.zl.web.user.model.utils.JsonResult;
import com.ebeijia.zl.web.user.model.utils.JsonView;
import com.ebeijia.zl.web.user.model.wxapi.service.BizService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 微信与开发者服务器交互接口
 */
@Api(value = "/w", description = "微信API对接")
@RestController
@RequestMapping("/w")
public class WxApiCtrl {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BizService bizService;
	
	@Autowired
	private WxMemoryCacheClient wxMemoryCacheClient;
	
	@Autowired
	private RedisDictProperties redisDictProperties;
	
	@Autowired
	private WxApiClient  wxApiClient;

	/**
	 * GET请求：进行URL、Tocken 认证； 1. 将token、timestamp、nonce三个参数进行字典序排序 2.
	 * 将三个参数字符串拼接成一个字符串进行sha1加密 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 */
	@ApiOperation("获取微信公众号GET请求")
	@ApiImplicitParams({
			@ApiImplicitParam(name="signature",required = true,dataType = "String"),
			@ApiImplicitParam(name="timestamp",required = true,dataType = "String"),
			@ApiImplicitParam(name="nonce",required = true,dataType = "String"),
			@ApiImplicitParam(name="echostr",required = true,dataType = "String"),
	})
	@RequestMapping(value = "/msg", method = RequestMethod.GET)
	public @ResponseBody String doGet(HttpServletRequest request) {
		// 如果是多账号，根据url中的account参数获取对应的MpAccount处理即可
		MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();// 获取缓存中的唯一账号
		String echostr=null;
		if (mpAccount != null) {
			String token = mpAccount.getToken();// 获取token，进行验证；
			String signature = request.getParameter("signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce");// 随机数
			echostr = request.getParameter("echostr");// 随机字符串
			// 校验成功返回 echostr，成功成为开发者；否则返回error，接入失败
			if (WxSignUtil.validSign(signature, token, timestamp, nonce)) {
				return echostr;
			}
		}
		return echostr;
	}

	/**
	 * POST 请求：进行消息处理(核心业务controller)
	 */
	@ApiOperation("微信公众号消息处理")
	@RequestMapping(value = "/msg", method = RequestMethod.POST)
	public @ResponseBody String doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();
			//String wxAESKey = redisDictProperties.getdictValueByCode("WX_AES_KEY");// 加密密钥
			String token = mpAccount.getToken();// token
			String appId = mpAccount.getAppid();
			String signature = request.getParameter("msg_signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce");// 随机数

			//WXBizMsgCrypt pc = new WXBizMsgCrypt(token, wxAESKey, appId);
			MsgRequest msgRequest = MsgXmlUtil.parseXml(request, null, null, appId, token, signature, timestamp, nonce);// 获取发送的消息

			logger.info(JSONObject.toJSONString(msgRequest));

			String msgId = msgRequest.getMsgId();
			String createTimeAndFromUserName = msgRequest.getCreateTime() + msgRequest.getFromUserName();
			if (!StringUtil.isNullOrEmpty(msgId)) {
				if (SemaphoreMap.getSemaphore().containsKey(msgId)) {
					logger.info("消息重复推送msgId [{}]--->FromUserName [{}], MsgType [{}], Event [{}]", msgId, 
							msgRequest.getFromUserName(), msgRequest.getMsgType(), msgRequest.getEvent());
					return "success";
				} else {
					SemaphoreMap.getSemaphore().put(msgId, msgId);
				}
			} else if (!StringUtil.isNullOrEmpty(createTimeAndFromUserName)) {
				if (SemaphoreMap.getSemaphore().containsKey(createTimeAndFromUserName)) {
					logger.info("消息重复推送createTimeAndFromUserName [{}]--->FromUserName [{}], MsgType [{}], Event [{}]", 
							createTimeAndFromUserName, msgRequest.getFromUserName(), msgRequest.getMsgType(), msgRequest.getEvent());
					return "success";
				} else {
					SemaphoreMap.getSemaphore().put(createTimeAndFromUserName, createTimeAndFromUserName);
				}
			} else {
				logger.info("消息重复推送msgId [{}]--->FromUserName [{}], MsgType [{}], Event [{}]", msgId, 
						msgRequest.getFromUserName(), msgRequest.getMsgType(), msgRequest.getEvent());
				return "success";
			}

			String rtnXml = bizService.processMsg(msgRequest, mpAccount);// 处理完业务逻辑后回复微信平台

			if (StringUtil.isNullOrEmpty(rtnXml) || "null".equals(rtnXml)) {
				return "success";
			} else if ("success".equals(rtnXml) || "error".equals(rtnXml)) {
				return rtnXml;
			} else {
				//String encryptXml = pc.encryptMsg(rtnXml, timestamp, nonce);// 返回消息加密
				return rtnXml;
			}
		} catch (Exception e) {
			logger.error("## 公众号消息处理发生异常：", e);
			return "error";
		}
	}


	/**
	 * 获取js ticket
	 * 
	 * @param request
	 * @param url
	 * @return
	 */
	@ApiOperation("获取微信公众号jsTicket")
	@RequestMapping(value = "/jsTicket", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult jsTicket(HttpServletRequest request, @RequestParam("url")String url) {
		MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();// 获取缓存中的唯一账号
		String jsTicket = wxApiClient.getJSTicket(mpAccount);
		WxSign sign = new WxSign(mpAccount.getAppid(), jsTicket, url);
		JsonView jv = new JsonView();
		jv.setData(sign);
		JsonResult result =new JsonResult<JsonView>();
		result.setResult(jv);
		return result;
	}


	/**
	 * 获取微信公众号openId
	 *
	 * @param request
	 * @param code
	 * @return
	 */
	@ApiOperation("获取微信公众号openId")
	@ApiImplicitParam(name="code",required = true,dataType = "String")
	@RequestMapping(value = "/getOpenId", method = RequestMethod.POST)
	@ResponseBody
	public String getOpenId(HttpServletRequest request, @RequestParam("code")String code) {
		MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();// 获取缓存中的唯一账号
	//	OAuthAccessToken token=wxApiClient.getOAuthAccessToken(mpAccount,code);
        String oAuthOpenId = wxApiClient.getOAuthOpenId(mpAccount, code);
       // String openId="";
	/*	if(token !=null){
			openId=token.getOpenid();
		}*/
		return oAuthOpenId;
	}

    /**
     * 获取微信公众号用户信息
     *
     * @param request
     * @param openId
     * @return
     */
    @ApiOperation("获取微信公众号用户信息")
    @ApiImplicitParam(name="openId",required = true,dataType = "String")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getUserInfo(HttpServletRequest request, @RequestParam("openId")String openId) {
        MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();// 获取缓存中的唯一账号
        AccessToken accessToken = WxApi.getAccessToken(mpAccount.getAppid(), mpAccount.getAppsecret());
        String userInfoUrl = WxApi.getFansInfoUrl(accessToken.getAccessToken(),openId);
        JSONObject jsonObj = WxApi.httpsRequest(userInfoUrl, "GET", null);

        return jsonObj;
    }

}
