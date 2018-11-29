package com.cn.thinkx.ecom.routes.controller;

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

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.routesNews;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.routes.domain.Routes;
import com.cn.thinkx.ecom.routes.service.RoutesService;
import com.cn.thinkx.ecom.system.domain.User;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "routes")
public class RoutesController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoutesService routesService;

	/**
	 * 电商入口信息列表查询
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listRoutes")
	public ModelAndView listRoutes(HttpServletRequest req, Routes routes) {
		ModelAndView mv = new ModelAndView("routes/listRoutes");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Routes> pageList = routesService.getRoutesPage(startNum, pageSize, routes);
			mv.addObject("channelList", Constants.ChannelEcomType.values());
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询电商入口列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 电商入口信息列表查询
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/listRoutes")
	public ModelAndView listRoutes(HttpServletRequest req, HttpServletResponse resp, Routes routes) {
		ModelAndView mv = new ModelAndView("routes/listRoutes");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Routes> pageList = routesService.getRoutesPage(startNum, pageSize, routes);
			mv.addObject("channelList", Constants.ChannelEcomType.values());
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询电商入口列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 根据主键查询商城信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getRoutes")
	public Routes getRoutes(HttpServletRequest req, Routes routes) {
		Routes rs = new Routes();
		try {
			rs = routesService.selectByPrimaryKey(routes.getId());
		} catch (Exception e) {
			logger.error("## 查询主键为[" + routes.getId() + "]的电商入口信息出错", e);
		}
		return rs;
	}

	/**
	 * 新增电商入口信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/addRoutes")
	public BaseResult<Object> addRoutes(@RequestParam("loginFile") MultipartFile loginFile, HttpServletRequest req,
			Routes routes) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		routes.setEcomName(req.getParameter("ecom_name"));
		routes.setCreateUser(StringUtils.trim(user.getId().toString()));
		routes.setUpdateUser(StringUtils.trim(user.getId().toString()));
		try {
			// 判断是否有文件提交
			if (loginFile != null && !loginFile.isEmpty())
				routesService.insertRoutes(routes, loginFile);
			else
				return ResultsUtil.error(routesNews.RO09.getCode(), routesNews.RO09.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 上传LOGO图片出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增电商入口信息出错", e);
			return ResultsUtil.error(routesNews.RO01.getCode(), routesNews.RO01.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 修改电商入口信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/editRoutes")
	public BaseResult<Object> editRoutes(@RequestParam("loginFile") MultipartFile loginFile, HttpServletRequest req,
			Routes routes) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		routes.setId(req.getParameter("routes_id"));
		routes.setEcomName(req.getParameter("ecom_name"));
		routes.setUpdateUser(StringUtils.trim(user.getId().toString()));
		try {
			routesService.updateRoutes(routes, loginFile);
		} catch (BizHandlerException e) {
			logger.error("## 上传LOGO图片出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑电商入口信息出错", e);
			return ResultsUtil.error(routesNews.RO02.getCode(), routesNews.RO02.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 删除电商入口信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/deleteRoutes")
	public BaseResult<Object> deleteRoutes(HttpServletRequest req, Routes routes) {
		try {
			routesService.deleteRoutes(routes);
		} catch (BizHandlerException e) {
			logger.error("## 删除电商入口信息出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除电商入口信息出错", e);
			return ResultsUtil.error(routesNews.RO03.getCode(), routesNews.RO03.getMsg());
		}
		return ResultsUtil.success();
	}

}
