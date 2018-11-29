package com.cn.thinkx.ecom.front.api.ecomstore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.front.api.banner.service.EcomBannerService;

@RestController
@RequestMapping("/ecom/ecomstore")
public class EcomIndexController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EcomBannerService ecomBannerService;
	
	@GetMapping("/ecomIndex")
	public ModelAndView index(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("ecom/home/homePage");
		mv.addObject("footType", Constants.FootCodeType.INDEX.getId());
		String ecomCode = req.getParameter("ecomCode");
		Floor floor = new Floor();
		floor.setType("1");
		floor.setCheckDefault("0");	//显示6条数据
		floor.setEcomCode(ecomCode);
		List<Floor> floorList = ecomBannerService.getFloorGoodsList(floor);
		for (Floor floor2 : floorList) {
			for (Goods goods : floor2.getGoods()) {
				goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
			}
		}
		SettingBanner settingBanner = new SettingBanner();
		settingBanner.setEcomCode(ecomCode);
		List<SettingBanner> settingBannerList = ecomBannerService.getSettingBannerList(settingBanner);
		mv.addObject("floorList",floorList);
		mv.addObject("ecomCode", ecomCode);
		mv.addObject("settingBannerList", settingBannerList);
		return mv;
	}
}
