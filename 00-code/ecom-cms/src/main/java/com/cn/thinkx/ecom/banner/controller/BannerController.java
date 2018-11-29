package com.cn.thinkx.ecom.banner.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.banner.domain.Banner;
import com.cn.thinkx.ecom.banner.service.BannerService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.bannerNews;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.system.domain.User;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "banner")
public class BannerController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BannerService bannerService;

	/**
	 * Banner信息列表查询
	 * 
	 * @param req
	 * @param banner
	 * @return
	 */
	@GetMapping(value = "/listBanner")
	public ModelAndView listBanner(HttpServletRequest req, Banner banner) {
		ModelAndView mv = new ModelAndView("banner/listBanner");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Banner> pageList = bannerService.getBannerPage(startNum, pageSize, banner);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询Banner列表信息出错", e);
		}
		return mv;
	}

	/**
	 * Banner信息列表查询
	 * 
	 * @param req
	 * @param resp
	 * @param banner
	 * @return
	 */
	@PostMapping(value = "/listBanner")
	public ModelAndView listBanner(HttpServletRequest req, HttpServletResponse resp, Banner banner) {
		ModelAndView mv = new ModelAndView("banner/listBanner");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Banner> pageList = bannerService.getBannerPage(startNum, pageSize, banner);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询Banner列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 根据主键查询Banner信息
	 * 
	 * @param req
	 * @param banner
	 * @return
	 */
	@PostMapping(value = "/getBanner")
	public Banner getBanner(HttpServletRequest req, Banner banner) {
		Banner ba = new Banner();
		try {
			ba = bannerService.selectByPrimaryKey(banner.getId());
		} catch (Exception e) {
			logger.error("## 查询主键为[{}]的Banner信息出错", banner.getId(), e);
		}
		return ba;
	}

	/**
	 * 新增Banner信息
	 * 
	 * @param imgfile
	 * @param req
	 * @param banner
	 * @return
	 */
	@PostMapping(value = "/addBanner")
	public BaseResult<Object> addBanner(@RequestParam("imageUrlFile") MultipartFile imgfile, HttpServletRequest req,
			Banner banner) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		banner.setCreateUser(StringUtils.trim(user.getId().toString()));
		banner.setUpdateUser(StringUtils.trim(user.getId().toString()));
		banner.setRemarks(req.getParameter("remarks_"));
		try {
			// 判断是否有文件提交
			if (imgfile != null && !imgfile.isEmpty())
				bannerService.insertBanner(banner, imgfile);
			else
				return ResultsUtil.error(bannerNews.BA09.getCode(), bannerNews.BA09.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## Banner图片上传出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增Banner出错", e);
			return ResultsUtil.error(bannerNews.BA01.getCode(), bannerNews.BA01.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 修改Banner信息
	 * 
	 * @param imgfile
	 * @param req
	 * @param banner
	 * @return
	 */
	@PostMapping(value = "/editBanner")
	public BaseResult<Object> editBanner(@RequestParam("imageUrlFile") MultipartFile imgfile, HttpServletRequest req,
			Banner banner) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			banner.setUpdateUser(StringUtils.trim(user.getId().toString()));
			banner.setId(req.getParameter("banner_id"));
			banner.setRemarks(req.getParameter("remarks_"));
			bannerService.updateBanner(banner, imgfile);
		} catch (BizHandlerException e) {
			logger.error("## Banner图片上传出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑Banner信息出错", e);
			return ResultsUtil.error(bannerNews.BA02.getCode(), bannerNews.BA02.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 删除Banner信息
	 * 
	 * @param req
	 * @param banner
	 * @return
	 */
	@PostMapping(value = "/deleteBanner")
	public BaseResult<Object> deleteBanner(HttpServletRequest req, Banner banner) {
		try {
			bannerService.deleteBanner(banner);
		} catch (BizHandlerException e) {
			logger.error("## 删除Banner信息出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除Banner信息出错", e);
			return ResultsUtil.error(bannerNews.BA03.getCode(), bannerNews.BA03.getMsg());
		}
		return ResultsUtil.success();
	}

}
