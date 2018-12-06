package com.ebeijia.zl.web.api.model.phoneRecharge.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ebeijia.zl.web.api.model.phoneRecharge.service.PhoneRechargeService;



@Controller
@RequestMapping("/phoneRechargeNotify")
public class PhoneRechargeNotifyController {
	
	
	@Autowired
	@Qualifier("phoneRechargeService")
	private PhoneRechargeService phoneRechargeService;
	
	@RequestMapping(value = "/flowRechargeNotify")
	@ResponseBody
	public String flowRechargeNotify(HttpServletRequest request) {
		boolean flag = phoneRechargeService.flowRechargeNotify(request);
		if (flag) {
			return "SUCCESS";
		}
		return "FAIL";
	}
	
	@RequestMapping(value = "/phoneRechargeBackResult")
	@ResponseBody
	public String phoneRechargeBackResult(HttpServletRequest request) {
		boolean flag = phoneRechargeService.phoneRechargeNotify(request);
		if (flag) {
			return "SUCCESS";
		}
		return "FAIL";
	}
	
}
