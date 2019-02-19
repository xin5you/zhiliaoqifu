package com.ebeijia.zl.web.cms.banner.controller;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.BannerPositionEnum;
import com.ebeijia.zl.common.utils.enums.BannerSpecEnum;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.IsDisabledEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecification;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomBannerService;
import com.ebeijia.zl.web.cms.banner.service.BannerService;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.ebeijia.zl.web.cms.member.service.MemberService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("banner")
public class BannerController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BannerService bannerService;

	@Autowired
	private ITbEcomBannerService ecomBannerService;

	/**
	 * 查询banner信息列表（分页）
	 * @param req
	 * @param banner
	 * @return
	 */
	@RequestMapping(value = "/getBannerList")
	public ModelAndView getBannerList(HttpServletRequest req, TbEcomBanner banner) {
		ModelAndView mv = new ModelAndView("banner/listBanner");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<TbEcomBanner> pageInfo = bannerService.getBannerListPage(startNum, pageSize, banner);
			mv.addObject("pageInfo", pageInfo);
			mv.addObject("bannerPositionList", BannerPositionEnum.values());
			//mv.addObject("bannerSpecList", BannerSpecEnum.values());
		} catch (Exception e) {
			logger.error("## 会员信息查询出错", e);
		}
		mv.addObject("banner", banner);
		return mv;
	}

	/**
	 * 根据id查询banner信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "getBanner")
	public TbEcomBanner getBanner(HttpServletRequest req) {
		TbEcomBanner banner = new TbEcomBanner();
		String bannerId = req.getParameter("bannerId");
		try {
			banner = ecomBannerService.getById(bannerId);
		} catch (Exception e) {
			logger.error("## 根据id{}查询banner信息异常", banner, e);
		}
		return banner;
	}

	/**
	 * 新增banner信息异常
	 * @param imageUrlFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/addBanner")
	public BaseResult<Object> addBanner(@RequestParam("imageUrlFile") MultipartFile imageUrlFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomBanner banner = getTbEcomBanner(req);
			result = bannerService.addBanner(banner, imageUrlFile);
		} catch (Exception e) {
			logger.error("## 新增banner信息异常");
			return ResultsUtil.error(ExceptionEnum.BannerNews.BannerNews01.getCode(), ExceptionEnum.BannerNews.BannerNews01.getMsg());
		}
		return result;
	}

	/**
	 * 编辑banner信息
	 * @param imageUrlFile
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/editBanner")
	public BaseResult<Object> editBanner(@RequestParam("imageUrlFile") MultipartFile imageUrlFile, HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			TbEcomBanner banner = getTbEcomBanner(req);
			result = bannerService.editBanner(banner, imageUrlFile);
		} catch (Exception e) {
			logger.error("## 编辑banner信息异常");
			return ResultsUtil.error(ExceptionEnum.BannerNews.BannerNews02.getCode(), ExceptionEnum.BannerNews.BannerNews02.getMsg());
		}
		return result;
	}

	/**
	 * 删除banner信息
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/deleteBanner")
	public BaseResult<Object> deleteBanner(HttpServletRequest req) {
		BaseResult<Object> result = new BaseResult<>();
		try {
			result = bannerService.deleteBanner(req);
		} catch (Exception e) {
			logger.error("## 编辑banner信息异常");
			return ResultsUtil.error(ExceptionEnum.BannerNews.BannerNews03.getCode(), ExceptionEnum.BannerNews.BannerNews03.getMsg());
		}
		return result;
	}

	/**
	 * 根据banner位置获取banner规格list
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getBannerSpecList")
	public Map<String, Object> getBannerSpecList(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = bannerService.getBannerSpecList(req);
		} catch (Exception e) {
			logger.error("## 编辑banner信息异常");
			resultMap.put("status", Boolean.FALSE);
		}
		return resultMap;
	}

	/**
	 * banner信息封装类方法
	 * @param req
	 * @return
	 */
	private TbEcomBanner getTbEcomBanner(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String bannerId = req.getParameter("banner_id");
		String imageUrl = req.getParameter("image_url");
		String bannerUrl = req.getParameter("banner_url");
		String spec = req.getParameter("spec");
		String position = req.getParameter("position");
		String bannerText = req.getParameter("banner_text");
		String sort = req.getParameter("sort");

		TbEcomBanner banner = null;
		if (!StringUtil.isNullOrEmpty(bannerId)) {
			banner = ecomBannerService.getById(bannerId);
		} else {
			banner = new TbEcomBanner();
			banner.setId(IdUtil.getNextId());
			banner.setDisable(IsDisabledEnum.IsDisabledEnum_0.getCode());
		}
		banner.setImageUrl(imageUrl);
		banner.setBannerUrl(bannerUrl);
		banner.setSpec(spec);
		banner.setPosition(position);
		banner.setBannerText(bannerText);
		banner.setSort(Integer.valueOf(sort));
		return banner;
	}

}
