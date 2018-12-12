package com.ebeijia.zl.web.oms.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/main")
public class MainController {
	

	@RequestMapping(value = "/index")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("main/index");

		return mv;
	}
	
	@RequestMapping(value = "/error")
	public ModelAndView error(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("common/error");

		return mv;
	}

}
