package com.cn.thinkx.ecom.front.api.hkbstore.controller;

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
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.banner.service.EcomBannerService;

@RestController
@RequestMapping("hkbstore/recommendMore")
public class RecommendMoreController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EcomBannerService ecomBannerService;

	@GetMapping("/getRecommendMoreList")
	public ModelAndView getRecommendMoreList(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("hkbstore/recommendMore");
		String floorId = req.getParameter("floorId");
		String type = req.getParameter("type");
		if (StringUtil.isNullOrEmpty(floorId) && StringUtil.isNullOrEmpty(type)) {
			logger.error("## 楼层id:[{}]和楼层类型type:[{}]为空", floorId, type);
			return null;
		}
		Floor floor = new Floor();
		if (type.equals(Constants.FloorType.ID01.getCode())) {
			String ecomCode = req.getParameter("ecomCode");
			if (StringUtil.isNullOrEmpty(ecomCode)) {
				logger.error("## 渠道商城code为空ecomCode：[{}]", ecomCode);
				return null;
			}
			floor.setEcomCode(ecomCode);
		}
		floor.setType(type);
		floor.setFloorId(floorId);
		floor.setCheckDefault("1"); // 0：表示默认显示六条推荐商品；1：表示默认显示全部楼层的商品信息
		List<Floor> floorList = ecomBannerService.getFloorGoodsList(floor);
		for (Goods goods : floorList.get(0).getGoods()) {
			goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
		}
		mv.addObject("floorList", floorList);
		return mv;
	}
}
