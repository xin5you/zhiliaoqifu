package com.cn.thinkx.ecom.eshoproutes.controller;

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

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.eshop.service.EshopInfService;
import com.cn.thinkx.ecom.eshoproutes.domain.EshopRoutes;
import com.cn.thinkx.ecom.eshoproutes.service.EshopRoutesService;
import com.cn.thinkx.ecom.routes.domain.Routes;
import com.cn.thinkx.ecom.routes.service.RoutesService;
import com.cn.thinkx.ecom.system.domain.User;

@RestController
@RequestMapping(value = "eshopRoutes")
public class EshopRoutesController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EshopRoutesService eshopRoutesService;

	@Autowired
	private RoutesService routesService;
	
	@Autowired
	private EshopInfService eshopInfService;

	/**
	 * 电商入口列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/listEshopRoutes/{type}")
	public ModelAndView listEshopRoutes(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable("type") String eshopId) {
		ModelAndView mv = new ModelAndView("eshoproutes/listEshopRoutes");
		try {
			//根据商城主键查询商城名称
			EshopInf eshop = eshopInfService.selectByPrimaryKey(eshopId);
			// 查询有的电商入口
			List<Routes> routesList = routesService.getList();
			// 根据商城id查询商城所拥有的电商入口
			EshopRoutes er = new EshopRoutes();
			er.setEshopId(eshopId);
			List<EshopRoutes> eshopRoutesList = eshopRoutesService.getList(er);
			for (Routes b : routesList) {
				for (EshopRoutes e : eshopRoutesList) {
					if (b.getId().equals(e.getRoutesId()))
						b.setChecked("1");
				}
			}
			mv.addObject("eshopName", eshop.getEshopName());
			mv.addObject("eshopId", eshopId);
			mv.addObject("routesList", routesList);
		} catch (Exception e) {
			logger.error("查询电商入口列表信息出错", e);
		}
		return mv;
	}

	// 新增商城电商入口信息
	@PostMapping(value = "/addEshopRoutes")
	public BaseResult<Object> addEshopRoutes(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		int count = 0;
		try {
			String eshopId = req.getParameter("eshopId");
			EshopRoutes er = new EshopRoutes();
			er.setEshopId(eshopId);
			List<EshopRoutes> eshopRoutesList = eshopRoutesService.getList(er);
			for (EshopRoutes e : eshopRoutesList)
				eshopRoutesService.deleteByPrimaryKey(e.getId());
			String ids = req.getParameter("ids");
			if (StringUtils.isEmpty(ids)) {
//				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
				count = 1;
			} else {
				String[] routesId = ids.split(",");
				for (int i = 0; i < routesId.length; i++) {
					EshopRoutes ers = new EshopRoutes();
					ers.setEshopId(eshopId);
					ers.setRoutesId(routesId[i]);
					ers.setCreateUser(StringUtils.trim(user.getId().toString()));
					ers.setUpdateUser(StringUtils.trim(user.getId().toString()));
					count = eshopRoutesService.insert(ers);
				}
			}
			if (count > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.routesNews.RO06.getCode(),
						ExceptionEnum.routesNews.RO06.getMsg());
		} catch (BizHandlerException e) {
			logger.error("新增商城电商入口出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("新增商城电商入口出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

}
