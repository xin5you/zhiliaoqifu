package com.cn.thinkx.ecom.system.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.system.domain.Resource;
import com.cn.thinkx.ecom.system.domain.User;
import com.cn.thinkx.ecom.system.service.ResourceService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.Constants.LoginType;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "system/resource")
public class ResourceController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ResourceService resourceService;

	/**
	 * 资源列表查询
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listResource")
	public ModelAndView listResource(HttpServletRequest req, Resource resource) {
		ModelAndView mv = new ModelAndView("system/resource/listResource");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Resource> pageList = resourceService.getResourcePage(startNum, pageSize, resource);
			mv.addObject("resource", resource);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询资源列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 资源列表查询
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/listResource")
	public ModelAndView listResource(HttpServletRequest req, HttpServletResponse resp, Resource resource) {
		ModelAndView mv = new ModelAndView("system/resource/listResource");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Resource> pageList = resourceService.getResourcePage(startNum, pageSize, resource);
			mv.addObject("resource", resource);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询资源列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 根据主键查询资源信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getResource")
	public Resource getResource(HttpServletRequest req, Resource resource) {
		Resource si = new Resource();
		try {
			si = resourceService.selectByPrimaryKey(resource.getId());
		} catch (Exception e) {
			logger.error("## 查询主键为[" + resource.getId() + "]的资源信息出错", e);
		}
		return si;
	}

	/**
	 * 新增资源信息
	 * 
	 * @param req
	 * @param resource
	 * @return
	 */
	@PostMapping(value = "/addResource")
	public BaseResult<Object> addResource(HttpServletRequest req, Resource resource) {
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			String u = resource.getUrl().substring(resource.getUrl().indexOf("/") + 1);
			String r = u.replaceAll("/", "_");
			resource.setId(UUID.randomUUID().toString());
			resource.setResourceKey(r.toUpperCase());
			resource.setResourceType("0");
			resource.setDataStat(Constants.PrmStat.PS0.getCode());
			resource.setLoginType(LoginType.LoginType2.getCode());
			resource.setCreateUser(user.getId().toString());
			resource.setUpdateUser(user.getId().toString());
			resource.setCreateTime(System.currentTimeMillis());
			resource.setUpdateTime(System.currentTimeMillis());
			if (resourceService.insert(resource) > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.resourceNews.RN01.getCode(), ExceptionEnum.resourceNews.RN01.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 新增资源出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增资源出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 修改资源信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/editResource")
	public BaseResult<Object> editResource(HttpServletRequest req, Resource res) {
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			Resource resource = resourceService.selectByPrimaryKey(res.getId());
			resource.setResourceName(res.getResourceName());
			resource.setDescription(res.getDescription());
			resource.setUrl(res.getUrl());
			String u = res.getUrl().substring(res.getUrl().indexOf("/") + 1);
			String r = u.replaceAll("/", "_");
			resource.setResourceKey(r.toUpperCase());
			resource.setUpdateUser(user.getId().toString());
			resource.setUpdateTime(System.currentTimeMillis());
			if (resourceService.updateByPrimaryKey(resource) > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.resourceNews.RN02.getCode(), ExceptionEnum.resourceNews.RN02.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 编辑资源出错", e);
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑资源出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 删除资源信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/deleteResource")
	public BaseResult<Object> deleteResource(HttpServletRequest req, Resource resource) {
		try {
			if (resourceService.deleteByPrimaryKey(resource.getId()) > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.resourceNews.RN03.getCode(), ExceptionEnum.resourceNews.RN03.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 删除资源出错", e);
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除资源出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

}
