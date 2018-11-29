package com.cn.thinkx.ecom.front.api.homepage.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.AESUtil;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.banner.domain.Banner;
import com.cn.thinkx.ecom.front.api.banner.service.BannerService;
import com.cn.thinkx.ecom.front.api.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.front.api.eshop.service.EshopInfService;
import com.cn.thinkx.ecom.front.api.homepage.domain.JFExtandJson;
import com.cn.thinkx.ecom.front.api.homepage.domain.JFHome;
import com.cn.thinkx.ecom.front.api.routes.domain.Routes;
import com.cn.thinkx.ecom.front.api.routes.service.RoutesService;

@RestController
@RequestMapping(value = "homePage")
public class HomePageController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BannerService bannerService;

	@Autowired
	private RoutesService routesService;

	@Autowired
	private EshopInfService eshopInfService;

	// 知了企服key
	@Value("${HKB_AES_KEY}")
	private String HKB_AES_KEY;

	// 海易通key
	@Value("${HYT_AES_KEY}")
	private String HYT_AES_KEY;

	// 家乐宝key
	@Value("${JLB_AES_KEY}")
	private String JLB_AES_KEY;

	// 嘉福京东key
	@Value("${JF_KEY}")
	private String JF_KEY;

	//嘉福京东对接参数
	@Value("${ident}")
	private String ident; //接入识别码

	@Value("${e_eid}")
	private String e_eid; //企业标识

	@Value("${jingdong_service}")
	private String jingdong_service; //京东服务接口
	
	@Value("${meituan_service}")
	private String meituan_service; //美团服务接口
	
	@Value("${dianping_service}")
	private String dianping_service; //美团服务接口

	/**
	 * 商城主页
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/listEshopHomePage")
	public ModelAndView listEshopHomePage(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("homepage/homePage");
		//接收知了企服加密参数进行解密
		String paramInfo = req.getParameter("paramInfo");
		logger.info("接收知了企服商城待解密参数[{}]", paramInfo);
		
		if (StringUtil.isNullOrEmpty(paramInfo)) {
			logger.error("## 接收知了企服商城请求参数paramInfo为空");
			return null; //TODO 统一返回错误页面 （接收参数为空）
		}
		
		String param = AESUtil.jzDecrypt(paramInfo, HKB_AES_KEY);
		
		//把参数按照 | 进行切割
		String[] str = param.split("\\|");
		logger.info("接收商城用户足迹信息为：userID[{}]，mobile[{}]，openID[{}]，innerMerchantNo[{}]，innerShopNo[{}]",
				str[0], str[1], str[2], str[3], str[4]);
		//按顺序拼接参数进行加密
		StringBuffer paramStr = new StringBuffer();
		paramStr.append(str[0])
		.append("|")
		.append(str[1])
		.append("|")
		.append(str[2])
		.append("|")
		.append(str[3])
		.append("|")
		.append(str[4]);
		EshopInf eshop = new EshopInf();
		eshop.setMchntCode(str[3]);
		eshop.setShopCode(str[4]);
		
		//按照海易通key对解密参数进行加密处理（加密后的用户信息）
		/*String paramInfos = AESUtil.jzEncrypt(paramStr.toString(), HYT_AES_KEY);
		String timeStamps = AESUtil.jzEncrypt(String.valueOf(System.currentTimeMillis()), HYT_AES_KEY);
		String hytUrl = "&paramInfo=" + paramInfos + "&timeStamp=" + timeStamps;*/

		//按照家乐宝key对解密参数进行加密（加密后的用户信息）
		String userInfo = AESUtil.jzEncrypt(paramStr.toString(), JLB_AES_KEY);
		String jlbUrl = "&params=" + "{\"userInfo\":\"" + userInfo + "\"}";
		
		//京东链接参数
		String jDUrl = JDParams(str[0], str[1], str[3], str[4]);
		//美团链接参数    
//		String MTUrl = MTParams(str[0], str[1], str[3], str[4]);

		//商城信息
		EshopInf eshopInf = eshopInfService.selectByEshopInf(eshop);
		//主页列表信息
		List<Banner> bannerList = null;
		//电商入口列表信息
		List<Routes> routesList = null;
		if (eshopInf == null) {
			logger.error("## 根据商户号[{}]和门店号[{}]查询商城信息为空", str[3], str[4]);
			return null; //TODO 统一返回错误页面（查询信息为空）
		}
		//主页信息
		bannerList = bannerService.selectByBannerHomePage(eshopInf.getId());
		if (bannerList.size() > 0) {
			for (Banner b : bannerList) {
				switch (b.getChannel()) {
				/*case "40006004":
					b.setBannerUrl(b.getBannerUrl() + MTUrl);
					break;*/
				case "40006003":
					b.setBannerUrl(b.getBannerUrl() + jDUrl);
					break;
				case "40006002":
					b.setBannerUrl(b.getBannerUrl() + jlbUrl);
					break;
				/*case "40006001":
					b.setBannerUrl(b.getBannerUrl() + hytUrl);
					break;*/
				default:
					break;
				}
			}
		} else {
			logger.error("## 根据商城主键[{}]查询商城主页信息为空", eshopInf.getId());
		}
		//电商入口信息
		routesList = routesService.selectByRoutesHomePage(eshopInf.getId());
		if (routesList.size() > 0) {
			for (Routes r : routesList) {
				switch (r.getEcomCode()) {
				/*case "40006004":
					r.setEcomUrl(r.getEcomUrl() + MTUrl);
					break;*/
				case "40006003":
					r.setEcomUrl(r.getEcomUrl() + jDUrl);
					break;
				case "40006002":
					r.setEcomUrl(r.getEcomUrl() + jlbUrl);
					break;
				/*case "40006001":
					r.setEcomUrl(r.getEcomUrl() + hytUrl);
					break;*/
				default:
					break;
				}
			}
		} else {
			logger.error("## 根据商城主键[{}]查询电商入口信息为空", eshopInf.getId());
		}
		mv.addObject("eshopName", eshopInf.getEshopName());
		mv.addObject("bannerList", bannerList);
		mv.addObject("routesList", routesList);
		return mv;
	}
	
	/**
	 * 京东链接参数封装
	 * 
	 * @param e_uid
	 * @param mobile
	 * @param mchntCode
	 * @param shopCode
	 * @return
	 */
	private String JDParams(String e_uid, String mobile, String mchntCode, String shopCode) {
		JFHome biz_content = new JFHome();
		biz_content.setE_eid(e_eid);
		biz_content.setE_uid(e_uid);
		biz_content.setMobile(mobile);
		String JDExtand = toJFExtandJsonStr(mobile, e_uid, mchntCode, shopCode, Constants.ChannelEcomType.CEU03.getCode(), Constants.ChannelCode.CHANNEL1.toString());
		SortedMap<String, String> JfItemsMap = new TreeMap<String, String>();
		JfItemsMap.put("biz_content", JSONArray.toJSONString(biz_content));
		JfItemsMap.put("charset", "utf-8");
		JfItemsMap.put("extand_params", JDExtand);
		JfItemsMap.put("format", "json");
		JfItemsMap.put("ident", ident);
		JfItemsMap.put("service", jingdong_service);
		JfItemsMap.put("sign_type", "MD5");
		JfItemsMap.put("timestamp", String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
		JfItemsMap.put("version", "1.0");
		StringBuilder JfForSign = new StringBuilder();
		for (String key : JfItemsMap.keySet()) {
			JfForSign.append(key).append("=").append(JfItemsMap.get(key)).append("&");
		}
		JfForSign.deleteCharAt(JfForSign.length()-1);
		JfForSign.append(JF_KEY);
		String jfsigns = SignUtil.MD5Encode(JfForSign.toString());
		//logger.info("对嘉福京东参数要求加密前的值：[{}]", forSign.toString());
		//根据嘉福参数要求对参数进行urlencode
		String biz_contentUrl = urlEncoder(JfItemsMap.get("biz_content"));
		String jf_extand_params = urlEncoder(JfItemsMap.get("extand_params"));
		//跳转嘉福京东url参数
		StringBuffer jfUrl = new StringBuffer();
		jfUrl.append("?ident=").append(JfItemsMap.get("ident")).append("&service=").append(JfItemsMap.get("service"))
		.append("&format=").append(JfItemsMap.get("format")).append("&charset=").append(JfItemsMap.get("charset"))
		.append("&timestamp=").append(JfItemsMap.get("timestamp")).append("&version=").append(JfItemsMap.get("version"))
		.append("&biz_content=").append(biz_contentUrl).append("&extand_params=").append(jf_extand_params)
		.append("&sign_type=").append(JfItemsMap.get("sign_type")).append("&sign=").append(jfsigns.toLowerCase());
		return jfUrl.toString();
	}
	
	/**
	 * 美团链接参数封装
	 * 
	 * @param e_uid
	 * @param mobile
	 * @param mchntCode
	 * @param shopCode
	 * @return
	 */
	/*private String MTParams(String e_uid, String mobile, String mchntCode, String shopCode) {
		JFHome biz_content = new JFHome();
		biz_content.setE_eid(e_eid);
		biz_content.setE_uid(e_uid);
		biz_content.setMobile(mobile);
		biz_content.setType("1");
		String MTExtand = toJFExtandJsonStr(mobile, e_uid, mchntCode, shopCode, Constants.ChannelEcomType.CEU04.getCode(), Constants.ChannelCode.CHANNEL1.toString());
		SortedMap<String, String> MTItemsMap = new TreeMap<String, String>();
		MTItemsMap.put("biz_content", JSONArray.toJSONString(biz_content));
		MTItemsMap.put("charset", "utf-8");
		MTItemsMap.put("extand_params", MTExtand);
		MTItemsMap.put("format", "json");
		MTItemsMap.put("ident", ident);
		MTItemsMap.put("service", meituan_service);
		MTItemsMap.put("sign_type", "MD5");
		MTItemsMap.put("timestamp", String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
		MTItemsMap.put("version", "1.0");
		StringBuilder MTForSign = new StringBuilder();
		for (String key : MTItemsMap.keySet()) {
			MTForSign.append(key).append("=").append(MTItemsMap.get(key)).append("&");
		}
		MTForSign.deleteCharAt(MTForSign.length()-1);
		MTForSign.append(JF_KEY);
		String mtsigns = SignUtil.MD5Encode(MTForSign.toString());
		//根据美团参数要求对参数进行urlencode
		String biz_contentUrl = urlEncoder(MTItemsMap.get("biz_content"));
		String mt_extand_params = urlEncoder(MTItemsMap.get("extand_params"));
		//跳转嘉福美团url参数
		StringBuffer mtUrl = new StringBuffer();
		mtUrl.append("?ident=").append(MTItemsMap.get("ident")).append("&service=").append(MTItemsMap.get("service"))
		.append("&format=").append(MTItemsMap.get("format")).append("&charset=").append(MTItemsMap.get("charset"))
		.append("&timestamp=").append(MTItemsMap.get("timestamp")).append("&version=").append(MTItemsMap.get("version"))
		.append("&biz_content=").append(biz_contentUrl).append("&extand_params=").append(mt_extand_params)
		.append("&sign_type=").append(MTItemsMap.get("sign_type")).append("&sign=").append(mtsigns.toLowerCase());
		return mtUrl.toString();
	}*/

	/**
	 * 拓展参数封装方法
	 * 
	 * @param pNo
	 * @param uNo
	 * @param mNo
	 * @param sNo
	 * @param eCh
	 * @param ch
	 * @return
	 */
	private String toJFExtandJsonStr(String pNo, String uNo, String mNo, String sNo, String eCh, String ch) {
		JFExtandJson extJson = new JFExtandJson();
		extJson.setPhoneNo(pNo);
		extJson.setUserId(uNo);
		extJson.setMchntCode(mNo);
		extJson.setShopCode(sNo);
		extJson.setEcomChnl(eCh);
		extJson.setChannel(ch);
		return JSONArray.toJSONString(extJson);
	}
	
	/**
	 * 对参数值进行urlEncoder
	 * 
	 * @param params
	 * @return
	 */
	private String urlEncoder(String params) {
		String encoderParams = "";
		try {
			encoderParams = URLEncoder.encode(params, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("## 商城--->对接参数params进行urlencode出错，urlencode前为[{}]", params);
		}
		return encoderParams;
	}
	
}
