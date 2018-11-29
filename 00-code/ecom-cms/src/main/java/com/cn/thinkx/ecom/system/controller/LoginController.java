package com.cn.thinkx.ecom.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.RandomCodeType;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.MD5Utils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.system.domain.Resource;
import com.cn.thinkx.ecom.system.domain.Role;
import com.cn.thinkx.ecom.system.domain.User;
import com.cn.thinkx.ecom.system.service.ResourceService;
import com.cn.thinkx.ecom.system.service.RoleService;
import com.cn.thinkx.ecom.system.service.UserService;

@RestController
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	@GetMapping(value = { "", "/", "/login" })
	public ModelAndView toLogin() {
		ModelAndView mv = new ModelAndView("login/login");

		return mv;
	}

	@PostMapping(value = "/doLogin")
	public BaseResult<Object> doLogin(HttpServletRequest req, HttpServletResponse resp) {

		HttpSession session = req.getSession();

		String userName = req.getParameter("userName");
		String md5Code = req.getParameter("md5Code");
		String authCode = req.getParameter("authCode");
		try {
			// 先判断验证码
			String sysAuthCode = (String) req.getSession().getAttribute(RandomCodeType.LOGIN.getCode());
			if (sysAuthCode == null)// session过期
				return ResultsUtil.error(ExceptionEnum.loginNews.LN01.getCode(), ExceptionEnum.loginNews.LN01.getMsg());
			if (!authCode.equalsIgnoreCase(sysAuthCode))
				return ResultsUtil.error(ExceptionEnum.loginNews.LN02.getCode(), ExceptionEnum.loginNews.LN02.getMsg());

			User currUser = userService.getUserByLoginName(userName);
			if (currUser == null || 1 > 2)
				return ResultsUtil.error(ExceptionEnum.loginNews.LN03.getCode(), ExceptionEnum.loginNews.LN03.getMsg());

			String sysMd5Password = MD5Utils.MD5(currUser.getPassword() + authCode);
			if (!currUser.getLoginName().equals(userName) || !sysMd5Password.equals(md5Code))
				return ResultsUtil.error(ExceptionEnum.loginNews.LN03.getCode(), ExceptionEnum.loginNews.LN03.getMsg());

			session.setAttribute(Constants.SESSION_USER, currUser);

			// 根据用户登录ID获得该用户所拥有的角色
			List<Role> roleList = roleService.getUserRoleByUserId(currUser.getId().toString());
			List<Resource> resourceList = null;
			// 循环用户所拥有的角色获得角色所拥有的资源
			if (roleList.size() > 0) {
				for (Role r : roleList)
					resourceList = resourceService.getRoleResourceByRoleId(r.getId().toString());
			}
			if (resourceList == null || resourceList.size() <= 0) {
				return ResultsUtil.error(ExceptionEnum.loginNews.LN05.getCode(), ExceptionEnum.loginNews.LN05.getMsg());
			} else {
				String[] UserAuthUrlArray = new String[resourceList.size()];
				// 循环将该用户所拥有的权限路径保存在字符串数组
				for (int i = 0; i < UserAuthUrlArray.length; i++) {
					Resource resource = resourceList.get(i);
					UserAuthUrlArray[i] = resource.getUrl();
				}
				session.setAttribute("USER_AUTH_URL_ARRAY", UserAuthUrlArray);
			}
		} catch (BizHandlerException e) {
			logger.error("## 登录失败：", e);
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 登录失败：", e);
			return ResultsUtil.error(ExceptionEnum.loginNews.LN04.getCode(), ExceptionEnum.loginNews.LN04.getMsg());
		}
		return ResultsUtil.success();
	}

	@GetMapping(value = "/main")
	public ModelAndView main(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("login/main");
		HttpSession session = req.getSession();
		mv.addObject("user", session.getAttribute(Constants.SESSION_USER));
		return mv;
	}

	@PostMapping(value = "/logout")
	public BaseResult<Object> logout(HttpServletRequest req) {
		req.getSession().removeAttribute(Constants.SESSION_USER);
		req.getSession().invalidate();
		return ResultsUtil.success();
	}

	@GetMapping(value = "/unauthorized")
	public ModelAndView unauthorized(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("main/unauthorized");

		return mv;
	}

}
