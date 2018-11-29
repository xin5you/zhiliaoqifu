package com.cn.thinkx.ecom.exception.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.ExceptionInf;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.exception.service.ExceptionService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("exception")
public class ExceptionController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExceptionService exceptionService;

	@RequestMapping(value = "getExceptionList")
	public ModelAndView getExceptionList(HttpServletRequest req, ExceptionInf exceptionInf) {
		ModelAndView mv = new ModelAndView("exception/listException");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<ExceptionInf> pageList = null;
		try {
			pageList = exceptionService.getExceptionlistPage(startNum, pageSize, exceptionInf);
		} catch (Exception e) {
			logger.error("查询异常信息出错", e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("exceptionInf", exceptionInf);
		mv.addObject("exceptionTypeList", ExceptionEnum.exceptionType.values());
		return mv;
	}

}
