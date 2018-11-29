package com.cn.thinkx.ecom.front.api.base.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EcomBaseController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	@RequestMapping(value = ERROR_PATH)
	public ModelAndView handleError() {
		ModelAndView mv = new ModelAndView("main/error");
		return mv;
	}

	@Override
	public String getErrorPath() {

		return ERROR_PATH;
	}

}
