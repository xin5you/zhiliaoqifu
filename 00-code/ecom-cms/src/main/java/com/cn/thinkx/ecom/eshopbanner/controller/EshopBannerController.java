package com.cn.thinkx.ecom.eshopbanner.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.banner.domain.Banner;
import com.cn.thinkx.ecom.banner.service.BannerService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.eshop.service.EshopInfService;
import com.cn.thinkx.ecom.eshopbanner.domain.EshopBanner;
import com.cn.thinkx.ecom.eshopbanner.service.EshopBannerService;
import com.cn.thinkx.ecom.system.domain.User;

@RestController
@RequestMapping(value = "eshopBanner")
public class EshopBannerController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EshopBannerService eshopBannerService;

	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private EshopInfService eshopInfService;

	/**
	 * 主页列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/listEshopBanner/{type}")
	public ModelAndView listEshopBanner(HttpServletRequest req, @PathVariable("type") String eshopId) {
		ModelAndView mv = new ModelAndView("eshopbanner/listEshopBanner");
		try {
			//根据商城主键查询商城名称
			EshopInf eshop = eshopInfService.selectByPrimaryKey(eshopId);
			// 所有的主页
			List<Banner> bannerList = bannerService.getList();
			// 根据商城id查询商城所拥有的主页
			EshopBanner eb = new EshopBanner();
			eb.setEshopId(eshopId);
			List<EshopBanner> eshopBannerList = eshopBannerService.getList(eb);
			for (Banner b : bannerList) {
				for (EshopBanner e : eshopBannerList) {
					if (b.getId().equals(e.getBannerId()))
						b.setChecked("1");
				}
			}
			mv.addObject("eshopName", eshop.getEshopName());
			mv.addObject("eshopId", eshopId);
			mv.addObject("bannerList", bannerList);
		} catch (Exception e) {
			logger.error("查询商城主页列表信息出错", e);
		}
		return mv;
	}

	// 新增商城主页信息
	@PostMapping(value = "/addEshopBanner")
	public BaseResult<Object> addEshopBanner(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		int count = 0;
		try {
			String eshopId = req.getParameter("eshopId");
			EshopBanner eb = new EshopBanner();
			eb.setEshopId(eshopId);
			List<EshopBanner> eshopBannerList = eshopBannerService.getList(eb);
			for (EshopBanner e : eshopBannerList)
				eshopBannerService.deleteByPrimaryKey(e.getId());
			String ids = req.getParameter("ids");
			if (StringUtils.isEmpty(ids)) {
//				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
				count = 1;
			} else {
				String[] bannerId = ids.split(",");
				for (int i = 0; i < bannerId.length; i++) {
					EshopBanner ebr = new EshopBanner();
					ebr.setEshopId(eshopId);
					ebr.setBannerId(bannerId[i]);
					ebr.setCreateUser(StringUtils.trim(user.getId().toString()));
					ebr.setUpdateUser(StringUtils.trim(user.getId().toString()));
					count = eshopBannerService.insert(ebr);
				}
			}
			if (count > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.bannerNews.BA06.getCode(),
						ExceptionEnum.bannerNews.BA06.getMsg());
		} catch (BizHandlerException e) {
			logger.error("新增商城主页出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("新增商城主页出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}
}
