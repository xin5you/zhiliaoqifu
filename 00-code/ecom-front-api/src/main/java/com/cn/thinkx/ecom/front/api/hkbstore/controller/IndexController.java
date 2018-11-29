package com.cn.thinkx.ecom.front.api.hkbstore.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;
import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.domain.UserMerchantAcct;
import com.cn.thinkx.ecom.basics.member.service.UserMerchantAcctService;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.banner.service.EcomBannerService;
import com.cn.thinkx.ecom.front.api.black.domain.WithDrawBlacklistInf;
import com.cn.thinkx.ecom.front.api.black.service.WithDrawBlacklistInfService;
import com.cn.thinkx.ecom.front.api.member.service.MemberService;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;

@RestController
@RequestMapping("/ecom/hkbstore")
public class IndexController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EcomBannerService ecomBannerService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserMerchantAcctService userMerchantAcctService; 
	
	@Autowired
	private WithDrawBlacklistInfService withDrawBlacklistInfService;
	
	@Autowired
	private JedisClusterUtils jedisClusterUtils;
	

	//调用知了企服收卡券集市
	@Value("${HKB_H5_URL}")
	private String hkb_h5_url;

	@GetMapping("/index")
	public ModelAndView index(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("hkbstore/homePage");
		mv.addObject("footType", Constants.FootCodeType.INDEX.getId());
		mv.addObject("hkb_h5_url", hkb_h5_url);
		String memberId = null;
		try {
			//查询购物车数量
			int cartNum = 0;
			//front web user session
			memberId = StringUtil.nullToString(request.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
			if (StringUtil.isNotEmpty(memberId)) {
				cartNum = cartService.getCartCountByMemberId(memberId);
			}
			Floor floor = new Floor();
			floor.setType("0");
			floor.setCheckDefault("0");
			List<Floor> floorList = ecomBannerService.getFloorGoodsList(floor);
			for (Floor floor2 : floorList) {
				for (Goods goods : floor2.getGoods()) {
					goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
				}
			}
			SettingBanner settingBanner = new SettingBanner();
			settingBanner.setEcomCode(Constants.GoodsEcomCodeType.ECOM00.getCode());
			List<SettingBanner> settingBannerList = ecomBannerService.getSettingBannerList(settingBanner);
			MemberInf entity = memberService.getMemberInfByPrimaryKey(memberId);
			WithDrawBlacklistInf blackInf = withDrawBlacklistInfService.getWithDrawBlacklistInfByUserId(entity.getUserId());
			String productCode = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_PROD_NO);
			String mchntCode = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_MCHNT_NO);
			String insCode = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_INS_CODE);
			UserMerchantAcct uAcc = new UserMerchantAcct();
			uAcc.setExternalId(entity.getOpenId());
			uAcc.setUserId(entity.getUserId());
			uAcc.setMchntCode(mchntCode);
			uAcc.setInsCode(insCode);
			uAcc.setProductCode(productCode);
			List<UserMerchantAcct> userAccList = userMerchantAcctService.getUserMerchantAcctByUser(uAcc);
			String accBal = StringUtil.isNullOrEmpty(userAccList.get(0).getAccBal())?"0":userAccList.get(0).getAccBal();
			int money = Integer.parseInt(NumberUtils.RMBYuanToCent(accBal));
			mv.addObject("money", money);
			mv.addObject("cartNum", cartNum);
			mv.addObject("floorList", floorList);
			mv.addObject("settingBannerList", settingBannerList);
			mv.addObject("blackInf", blackInf);
		} catch (Exception e) {
			logger.error(" ## 会员【[{}]】跳转知了商城主页出错 ", memberId, e);
		}
		return mv;
	}
	
	@PostMapping("/findJurisdiction")
	@ResponseBody
	public ModelMap findJurisdiction(HttpServletRequest req){
		ModelMap map = new ModelMap();
		map.addAttribute("hkb_h5_url", hkb_h5_url);
		WithDrawBlacklistInf blackInf = null;
		try {
			String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
			MemberInf entity = memberService.getMemberInfByPrimaryKey(memberId);
			blackInf = withDrawBlacklistInfService.getWithDrawBlacklistInfByUserId(entity.getUserId());
		} catch (Exception e) {
			logger.error(" ##  查看用户是否有权限访问 出错",e);
		}
		map.addAttribute("blackInf", blackInf);
		return map;
	}
	
	@GetMapping("/redirectHKB")
	public void redirectHKB(HttpServletRequest req,HttpServletResponse resp){
		String channel = req.getParameter("channel");
		try {
			//按知了企服收银台要求对参数进行加密
			String url = "http://hkb.tao-lue.com/eshop/mchntEshop/AccHKBEshop.html";
			
			/** 跳转知了企服收银台页面 */
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.println("<form name='cashSubmit' method='get'  action='" + url + "' >");
			out.println("<input type='hidden' name='channel' value='" + channel + "'>");
			out.println("</form>");
			out.println("<script type='text/javascript'>");
			out.println("document.cashSubmit.submit()");
			out.println("</script>");
		} catch (IOException e) {
			logger.error(" ## 知了企服电商平台调用知了企服收银台失败", e);
		}
	}
}
