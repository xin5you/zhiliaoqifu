package com.ebeijia.zl.web.wechat.model.wxapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.wechat.base.wxapi.process.HttpMethod;
import com.cn.thinkx.wechat.base.wxapi.process.MpAccount;
import com.cn.thinkx.wechat.base.wxapi.process.MsgXmlUtil;
import com.cn.thinkx.wechat.base.wxapi.process.WxApi;
import com.cn.thinkx.wechat.base.wxapi.process.WxApiClient;
import com.cn.thinkx.wechat.base.wxapi.process.WxMessageBuilder;
import com.cn.thinkx.wechat.base.wxapi.vo.MsgRequest;
import com.cn.thinkx.wechat.base.wxapi.vo.SemaphoreMap;
import com.ebeijia.zl.basics.wechat.domain.AccountFans;
import com.ebeijia.zl.basics.wechat.domain.AccountMenu;
import com.ebeijia.zl.basics.wechat.domain.MsgBase;
import com.ebeijia.zl.basics.wechat.domain.MsgNews;
import com.ebeijia.zl.basics.wechat.domain.MsgText;
import com.ebeijia.zl.basics.wechat.enums.FansStatusEnum;
import com.ebeijia.zl.basics.wechat.enums.GroupsIdStatEnum;
import com.ebeijia.zl.basics.wechat.enums.MsgType;
import com.ebeijia.zl.basics.wechat.service.AccountFansService;
import com.ebeijia.zl.basics.wechat.service.AccountMenuGroupService;
import com.ebeijia.zl.basics.wechat.service.AccountMenuService;
import com.ebeijia.zl.basics.wechat.service.MpAccountService;
import com.ebeijia.zl.basics.wechat.service.MsgBaseService;
import com.ebeijia.zl.basics.wechat.service.MsgNewsService;
import com.ebeijia.zl.basics.wechat.service.MsgTextService;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.user.service.UserInfFacade;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.web.wechat.model.wxapi.service.BizService;


/**
 * 业务消息处理
 */
@Service("bizService")
public class BizServiceImpl implements BizService {

	@Autowired
	private MsgBaseService msgBaseService;

	@Autowired
	private MsgTextService msgTextService;

	@Autowired
	private MsgNewsService msgNewsService;

	@Autowired
	private AccountFansService accountFansService;
	
	@Autowired
	private MpAccountService mpAccountService;
	
	@Autowired
	private WxApiClient wxApiClient;

	@Autowired
	private UserInfFacade userInfFacade;

	/**
	 * 处理消息 根据用户发送的消息和自己的业务，自行返回合适的消息；
	 * 
	 * @param msgRequest
	 *            接收到的消息
	 * @param mpAccount
	 *            微信公众号
	 */
	public String processMsg(MsgRequest msgRequest, MpAccount mpAccount) {
		String msgtype = msgRequest.getMsgType();// 接收到的消息类型
		String respXml = null;// 返回的内容；
		if (msgtype.equals(MsgType.Text.toString())) {
			/**
			 * 文本消息，一般公众号接收到的都是此类型消息
			 */
			respXml = this.processTextMsg(msgRequest, mpAccount);
		} else if (msgtype.equals(MsgType.Event.toString())) {// 事件消息
			/**
			 * 用户订阅公众账号、点击菜单按钮的时候，会触发事件消息
			 */
			respXml = this.processEventMsg(msgRequest, mpAccount, true);

			// 其他消息类型，开发者自行处理
		} else {
			respXml = "success";
		}


		if (!StringUtil.isNullOrEmpty(msgRequest.getMsgId())) {
			if (SemaphoreMap.getSemaphore().containsKey(msgRequest.getMsgId())) {
				SemaphoreMap.getSemaphore().remove(msgRequest.getMsgId());
			}
		} else if (!StringUtil.isNullOrEmpty(msgRequest.getCreateTime())
				&& !StringUtil.isNullOrEmpty(msgRequest.getFromUserName())) {
			if (SemaphoreMap.getSemaphore().containsKey(msgRequest.getCreateTime() + msgRequest.getFromUserName())) {
				SemaphoreMap.getSemaphore().remove(msgRequest.getCreateTime() + msgRequest.getFromUserName());
			}
		}
		return respXml;
	}

	// 处理文本消息
	private String processTextMsg(MsgRequest msgRequest, MpAccount mpAccount) {
		String content = msgRequest.getContent();
		if (!StringUtils.isEmpty(content)) {// 文本消息，默认回复订阅消息
			String tmpContent = content.trim();
			MsgText msgText = msgTextService.getRandomMsg(content);
			if (msgText != null) {// 回复文本
				return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, msgText));
			}
			List<MsgNews> msgNews = msgNewsService.getRandomMsgByContent(tmpContent, mpAccount.getMsgcount());
			if (!CollectionUtils.isEmpty(msgNews)) {// 回复图文
				return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
			}
		}
		return "success";
	}

	// 处理事件消息
	private String processEventMsg(MsgRequest msgRequest, MpAccount mpAccount, boolean merge) {
		String key = msgRequest.getEventKey();
		if (MsgType.SUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 订阅消息
			this.syncAccountFans(msgRequest.getFromUserName(), mpAccount, merge);
			MsgText text = null;
			/*** 用户是否已经注册汇卡包会员 **/
			UserInf user = userInfFacade.getUserInfByExternalId(msgRequest.getFromUserName(),UserChnlCode.USERCHNL2001.getCode());// 微信公众号
			if (user == null) {
				// 首次注册欢迎语 图文消息
				List<MsgNews> newsList = msgNewsService.getMsgNewsByCode(MsgType.SUBSCRIBE.getName());// TODO 放在缓存中
				return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, newsList));
			} else {
				text = msgTextService.getMsgTextByInputCode("again_subscribe"); // 再次关注，并且已经注册欢迎语 文本消息
				return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
			}
		} else if (MsgType.UNSUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 取消订阅消息
			accountFansService.syncAccountFans(msgRequest.getFromUserName(), MsgType.UNSUBSCRIBE.getName());
			MsgText text = msgTextService.getMsgTextByInputCode(MsgType.UNSUBSCRIBE.getName());
			if (text != null) {
				return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
			}
		} else {// 点击事件消息
			if (MsgType.VIEW.toString().equals(msgRequest.getEvent())) {// 点击菜单跳转链接时的事件推送
				return "success";
			}
			if (MsgType.SCANCODE_WAITMSG.toString().equals(msgRequest.getEvent())) {
				return "success";
			}
			if (MsgType.Location.toString().equals(msgRequest.getEvent())) { // 获取地理位置接口
				return "success";
			}
			if (!StringUtils.isEmpty(key)) {
				// 固定消息 _fix_ ：在创建菜单的时候，做了限制，对应的event_key 加了 _fix_
				if (key.startsWith("_fix_")) {
					String baseIds = key.substring("_fix_".length());
					if (!StringUtils.isEmpty(baseIds)) {
						String[] idArr = baseIds.split(",");
						if (idArr.length > 1) {// 多条图文消息
							List<MsgNews> msgNews = msgBaseService.listMsgNewsByBaseId(idArr);
							if (msgNews != null && msgNews.size() > 0) {
								return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
							}
						} else {// 图文消息，或者文本消息
							MsgBase msg = msgBaseService.getById(baseIds);
							if (msg.getMsgtype().equals(MsgType.Text.toString())) {
								MsgText text = msgBaseService.getMsgTextByBaseId(baseIds);
								if (text != null) {
									return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
								}
							} else {
								List<MsgNews> msgNews = msgBaseService.listMsgNewsByBaseId(idArr);
								if (msgNews != null && msgNews.size() > 0) {
									return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
								}
							}
						}
					}
				}
			}
		}
		return "success";
	}



	// 获取用户列表
	public boolean syncAccountFansList(MpAccount mpAccount) {
		String nextOpenId = null;
		AccountFans lastFans = accountFansService.getLastOpenId();
		if (lastFans != null) {
			nextOpenId = lastFans.getOpenId();
		}
		return doSyncAccountFansList(nextOpenId, mpAccount);
	}

	// 同步粉丝列表(开发者在这里可以使用递归处理)
	private boolean doSyncAccountFansList(String nextOpenId, MpAccount mpAccount) {
		String url = WxApi.getFansListUrl(wxApiClient.getAccessToken(mpAccount), nextOpenId);
		JSONObject jsonObject = WxApi.httpsRequest(url, HttpMethod.POST, null);
		if (jsonObject.containsKey("errcode")) {
			return false;
		}
		List<AccountFans> fansList = new ArrayList<AccountFans>();
		if (jsonObject.containsKey("data")) {
			if (jsonObject.getJSONObject("data").containsKey("openid")) {
				JSONArray openidArr = jsonObject.getJSONObject("data").getJSONArray("openid");
				int length = 5;// 同步5个
				if (openidArr.size() < length) {
					length = openidArr.size();
				}
				for (int i = 0; i < length; i++) {
					Object openId = openidArr.get(i);
					AccountFans fans = wxApiClient.syncAccountFans(openId.toString(), mpAccount);
					fansList.add(fans);
				}
				// 批处理
				accountFansService.saveBatch(fansList);
			}
		}
		return true;
	}

	// 获取用户信息接口 - 必须是开通了认证服务，否则微信平台没有开放此功能
	public AccountFans syncAccountFans(String openId, MpAccount mpAccount, boolean merge) {
		AccountFans fans = wxApiClient.syncAccountFans(openId, mpAccount);
		if (merge && null != fans) {
			AccountFans tmpFans = accountFansService.getByOpenId(openId);
			if (tmpFans == null) {
				fans.setWxid(mpAccount.getAccount());
				fans.setFansStatus(FansStatusEnum.Fans_STATUS_00.getCode());
				if (StringUtil.isNullOrEmpty(fans.getGroupid())) {
					fans.setGroupid("0");
				}
				accountFansService.save(fans);
			} else {
				// 同步商户
				try {
					if (fans.getGroupid() != null
							&& !GroupsIdStatEnum.groupdefauls_stat.getCode().equals(fans.getGroupid())) {
						this.updateFansGroupId(openId, fans.getGroupid(), mpAccount); // 同步商户分组
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				fans.setId(tmpFans.getId());
				fans.setWxid(mpAccount.getAccount());
				accountFansService.updateById(fans);
			}
		}
		return fans;
	}

	// 根据openid 获取粉丝，如果没有，同步粉丝
	public AccountFans getFansByOpenId(String openId, MpAccount mpAccount) {
		AccountFans fans = accountFansService.getByOpenId(openId);
		if (fans == null) {// 如果没有，添加
			fans = wxApiClient.syncAccountFans(openId, mpAccount);
			if (null != fans) {
				accountFansService.save(fans);
			}
		}
		return fans;
	}

	

	/**
	 * 此方法是构建菜单对象的；构建菜单时，对于 key 的值可以任意定义； 当用户点击菜单时，会把key传递回来；对已处理就可以了
	 * 
	 * @param menu
	 * @return
	 */
	private JSONObject getMenuJSONObj(AccountMenu menu) {
		JSONObject obj = new JSONObject();
		obj.put("name", menu.getName());
		obj.put("type", menu.getMtype());
		if ("click".equals(menu.getMtype())) {// 事件菜单
			if ("fix".equals(menu.getEvenType())) {// fix 消息
				obj.put("key", "_fix_" + menu.getMsgid());// 以 _fix_ 开头
			} else {
				if (StringUtils.isEmpty(menu.getInputcode())) {// 如果inputcode为空，默认设置为subscribe，以免创建菜单失败
					obj.put("key", "subscribe");
				} else {
					obj.put("key", menu.getInputcode());
				}
			}
		} else if ("view".equals(menu.getMtype())) {// 链接菜单-view
			obj.put("url", menu.getUrl());
		} else if ("scancode_waitmsg".equals(menu.getMtype())) {// 扫码带提示菜单
			obj.put("key", "rselfmenu_0_0");
			obj.put("sub_button", "[ ]");
		} else if ("scancode_push".equals(menu.getMtype())) {// 扫码推事件菜单
			obj.put("key", "rselfmenu_0_1");
			obj.put("sub_button", "[ ]");
		}
		return obj;
	}

	// 移动用户分组
	public boolean updateFansGroupId(String openid, String togroupid, MpAccount mpAccount) {

		JSONObject rstObj = wxApiClient.updateMembersGorups(openid, togroupid, mpAccount);
		if (rstObj != null && rstObj.getInteger("errcode") == 0) {
			return true;
		}
		return false;
	}

	private JSONObject getParentMenuJSONObj(AccountMenu menu, List<JSONObject> subMenu) {
		JSONObject obj = new JSONObject();
		obj.put("name", menu.getName());
		obj.put("sub_button", subMenu);
		return obj;
	}

}
