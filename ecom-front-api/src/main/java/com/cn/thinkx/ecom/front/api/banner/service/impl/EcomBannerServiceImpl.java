package com.cn.thinkx.ecom.front.api.banner.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.domain.SettingBanner;
import com.cn.thinkx.ecom.basics.goods.service.EsettingService;
import com.cn.thinkx.ecom.basics.goods.service.FloorService;
import com.cn.thinkx.ecom.front.api.banner.service.EcomBannerService;

@Service("ecomBannerService")
public class EcomBannerServiceImpl implements EcomBannerService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FloorService floorService;

	@Autowired
	private EsettingService esettingService;

	@Override
	public List<Floor> getFloorGoodsList(Floor floor) {
		List<Floor> floorList = null;
		try {
			// 需要二个参数修改
			// floor.setType("0"); //首页还是渠道商城 0：首页，1：渠道商城
			// floor.setCheckDefault("1"); //0：表示默认显示六条推荐商品；1：表示默认显示全部楼层的商品信息
			// if(floor.getType().equals("1")){ //如果是1，商城code,需要传入
			// floor.setEcomCode("");
			// }
			// if(floor.getCheckDefault().equals("1")){
			// floor.setFloorId("100010");
			// }
			floorList = floorService.getFloorGoodsList(floor);

			// logger.info("floorList------->"+JSON.toJSONString(floorList));
		} catch (Exception e) {
			logger.error("## 楼层推荐商品查询出错！[{}]", e);
		}
		return floorList;
	}

	/**
	 *查询商城的banner
	 * 
	 */
	@Override
	public List<SettingBanner> getSettingBannerList(SettingBanner entity) {
		return esettingService.getSettingBannerList(entity);
	}

}
