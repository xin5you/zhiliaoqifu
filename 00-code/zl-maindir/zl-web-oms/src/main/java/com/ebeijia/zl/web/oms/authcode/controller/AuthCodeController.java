package com.ebeijia.zl.web.oms.authcode.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.web.oms.authcode.util.ValidateCode;
import com.ebeijia.zl.common.utils.constants.Constants.RandomCodeType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;

/**
 *
 * 验证码跳转层
 *
 */
@Controller
@RequestMapping(value = "/authcode")
public class AuthCodeController {

	Logger logger = LoggerFactory.getLogger(AuthCodeController.class);
	
	@RequestMapping("genAuthCode")
	public ModelAndView genAuthCode(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		HttpSession session = request.getSession();
		ValidateCode vCode = new ValidateCode(130, 40, 4, 20);

		int codeType = NumberUtils.parseInt(request.getParameter("codeType"));
		RandomCodeType rcy = RandomCodeType.findByCode(codeType);
		if (rcy != null) {
			session.setAttribute(rcy.getCode(), vCode.getCode());
		}
		try {
			vCode.write(response.getOutputStream());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		return null;
	}
}
