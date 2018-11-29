package com.cn.thinkx.ecom.front.api.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.thinkx.ecom.basics.member.domain.CityInf;
import com.cn.thinkx.ecom.basics.member.service.CityInfService;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.member.domain.AreaResult;

@RestController
@RequestMapping(value = "member/city")
public class CityInfController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CityInfService cityInfService;

	@GetMapping(value = "/getArea")
	public AreaResult getArea(HttpServletRequest req) {
		AreaResult res = new AreaResult();
		try {
			CityInf city = new CityInf();
			String areaId = req.getParameter("areaId");
			if (StringUtil.isNullOrEmpty(areaId)) {
				return null;
			} else if("0".equals(areaId)){
				areaId="100000";
				city.setParentId(Long.parseLong(areaId)); // 父级ID
			}else{
				city.setParentId(Long.parseLong(areaId)); // 父级ID
			}
			List<CityInf> cityList = cityInfService.findCityInfList(city);
			if(cityList.size()==0){
				cityList = null;
			}
			res.setCode("200");
			res.setContent("成功");
			res.setData(cityList);
		} catch (Exception e) {
			res.setCode("500");
			res.setContent("失败");
			logger.error("## 失败了", e);
		}
		return res;
	}

}
