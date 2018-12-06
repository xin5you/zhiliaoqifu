package com.ebeijia.zl.web.api.model.welfaremart.service;

import javax.servlet.http.HttpServletRequest;

public interface WelfareMartService {
	
	String welfareBuyCardNotify(HttpServletRequest request);
	
	String welfareRechargeNotify(HttpServletRequest request);
	
}
