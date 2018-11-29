package com.cn.thinkx.ecom.front.api.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.member.domain.AccountFans;
import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.domain.PersonInf;
import com.cn.thinkx.ecom.basics.member.domain.UserMerchantAcct;
import com.cn.thinkx.ecom.basics.member.service.AccountFansService;
import com.cn.thinkx.ecom.basics.member.service.PersonInfService;
import com.cn.thinkx.ecom.basics.member.service.UserMerchantAcctService;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.banner.service.EcomBannerService;
import com.cn.thinkx.ecom.front.api.black.domain.WithDrawBlacklistInf;
import com.cn.thinkx.ecom.front.api.black.service.WithDrawBlacklistInfService;
import com.cn.thinkx.ecom.front.api.card.service.CardKeysOrderService;
import com.cn.thinkx.ecom.front.api.member.service.MemberService;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;

@RestController
@RequestMapping("ecom/member")
public class MemberController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PersonInfService personInfService;
	
	@Autowired
	private AccountFansService accountFansService;
	
	@Autowired
	private UserMerchantAcctService userMerchantAcctService; 
	
	@Autowired
	private WithDrawBlacklistInfService withDrawBlacklistInfService;
	
	@Autowired
	private JedisClusterUtils jedisClusterUtils;
	
	@Autowired
	private EcomBannerService ecomBannerService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CardKeysOrderService cardKeysOrderService;
	
	@Value("${HKB_H5_URL}")
	private String hkb_h5_url;

	@RequestMapping(value = "/insertMember")
	public BaseResult<Object> insertMember(HttpServletRequest req, HttpServletResponse resp, MemberInf entity) {
		return memberService.insertMember(req, resp, entity);
	}
	
	@GetMapping("/toMyAccount")
	public ModelAndView toMyAccount(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("ecom/member/member");
		mv.addObject("hkb_h5_url",hkb_h5_url);
		mv.addObject("footType", Constants.FootCodeType.MY.getId());
		String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
//		String openid = "oWcjP0q1T6echWuMg-Ybjmbb1KO0";
		MemberInf entity = memberService.getMemberInfByPrimaryKey(memberId);
		
		WithDrawBlacklistInf blackInf = withDrawBlacklistInfService.getWithDrawBlacklistInfByUserId(entity.getUserId());
		
		int count = cardKeysOrderService.getCardKeysOrderCount(entity.getUserId());
		
		String openid = entity.getOpenId();
		PersonInf personInf = personInfService.getPersonInfByOpenId(openid);
		if (personInf == null) {
//			mv = new ModelAndView("redirect:/customer/user/userRegister.html");
			return mv;
		}
		
		personInf.setMobilePhoneNo(NumberUtils.hiddingMobileNo(personInf.getMobilePhoneNo()));
		mv.addObject("personInf", personInf);
		
		AccountFans fans = accountFansService.getByOpenId(openid);
		if (fans == null) {
			logger.error("## 用户[{}]进入通卡账户失败：无法查找到粉丝信息", openid);
//			mv = new ModelAndView("redirect:/common/500.html");
			return mv;
		}
		mv.addObject("fansInfo", fans);
		String productCode = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_PROD_NO);
		String mchntCode = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_MCHNT_NO);
		String insCode = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_INS_CODE);
		
		UserMerchantAcct uAcc = new UserMerchantAcct();
		uAcc.setExternalId(openid);
		uAcc.setUserId(personInf.getUserId());
		uAcc.setMchntCode(mchntCode);
		uAcc.setInsCode(insCode);
		uAcc.setProductCode(productCode);
		List<UserMerchantAcct> userAccList = userMerchantAcctService.getUserMerchantAcctByUser(uAcc);
		if (userAccList == null || userAccList.size() < 1) {
			logger.error("## 用户[{}]进入通卡账户失败：无法查找到通卡账户信息", openid);
//			mv = new ModelAndView("redirect:/common/500.html");
			return mv;
		}
		
		Floor floor = new Floor();
		
		floor.setType("0");
		floor.setCheckDefault("0");
		
		List<Floor> floorList = ecomBannerService.getFloorGoodsList(floor);
		//查询购物车数量
		int cartNum=0;
		//front web user session
//		String memberId=StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		if(StringUtil.isNotEmpty(memberId)){
			cartNum=cartService.getCartCountByMemberId(memberId);
		}
		
		mv.addObject("userAccInfo", userAccList.get(0));
		
		mv.addObject("money", Integer.parseInt(NumberUtils.RMBYuanToCent(userAccList.get(0).getAccBal())));
		
		mv.addObject("floorList", floorList);
		
		mv.addObject("cartNum",cartNum);//购物车数量
		
		mv.addObject("blackInf", blackInf);
		
		mv.addObject("count", count);
		
		return mv;
	}
}
