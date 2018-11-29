package com.cn.thinkx.ecom.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.MD5Utils;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.system.domain.Role;
import com.cn.thinkx.ecom.system.domain.User;
import com.cn.thinkx.ecom.system.service.RoleService;
import com.cn.thinkx.ecom.system.service.UserService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "system/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	/**
	 * 修改密码
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/updatePwdCommit")
	public BaseResult<Object> updatePwdCommit(HttpServletRequest req, HttpServletResponse response) {
		String oldPasswrod = req.getParameter("oldPasswrod");
		String newPasswordPage = req.getParameter("newPasswordPage");
		String newPassword2Page = req.getParameter("newPassword2Page");
		if (!newPasswordPage.equals(newPassword2Page))
			return ResultsUtil.error(ExceptionEnum.userNews.UN07.getCode(), ExceptionEnum.userNews.UN07.getMsg());
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			User currUser = userService.selectByPrimaryKey(user.getId().toString());
			if (currUser != null) {
				if (!currUser.getPassword().equals(oldPasswrod))
					return ResultsUtil.error(ExceptionEnum.userNews.UN08.getCode(), ExceptionEnum.userNews.UN08.getMsg());
				currUser.setPassword(newPasswordPage);
				userService.updateByPrimaryKeySelective(currUser);
			}
		} catch (BizHandlerException e) {
			logger.error("## 密码修改失败", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 密码修改失败", e);
			return ResultsUtil.error(ExceptionEnum.userNews.UN09.getCode(), ExceptionEnum.userNews.UN09.getMsg());
		}
		return ResultsUtil.success();
	}

	/**
	 * 用户列表
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listUser")
	public ModelAndView listUser(HttpServletRequest req, User user) {
		ModelAndView mv = new ModelAndView("system/user/listUser");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<User> pageList = userService.getUserPage(startNum, pageSize, user);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询用户列表信息出错", e);
		}
		return mv;
	}
	
	/**
	 * 用户列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/listUser")
	public ModelAndView listUser(HttpServletRequest req, HttpServletResponse resp, User user) {
		ModelAndView mv = new ModelAndView("system/user/listUser");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<User> pageList = userService.getUserPage(startNum, pageSize, user);
			mv.addObject("user", user);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询用户列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 根据编号查询用户
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getUser")
	public User getUser(HttpServletRequest req) {
		String userId = req.getParameter("userId");
		User user = new User();
		try {
			user = userService.selectByPrimaryKey(userId);
		} catch (Exception e) {
			logger.error("## 查询主键为[" + userId + "]的用户信息出错", e);
		}
		return user;
	}

	/**
	 * 新增用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/addUser")
	public BaseResult<Object> addUser(HttpServletRequest req, User u) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			u.setOrganizationId("1");
			u.setPassword(MD5Utils.MD5(u.getPassword()));
			u.setIsdefault("1");
			u.setState("0");
			u.setCreateUser("" + user.getId());
			u.setUpdateUser("" + user.getId());
			User us = userService.getUserByLoginName(u.getLoginName());
			if(us == null){
				if (userService.insert(u) > 0)
					return ResultsUtil.success();
				else
					return ResultsUtil.error(ExceptionEnum.userNews.UN01.getCode(), ExceptionEnum.userNews.UN01.getMsg());
			}else{
				return ResultsUtil.error(ExceptionEnum.userNews.UN05.getCode(), ExceptionEnum.userNews.UN05.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 新增用户出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增用户出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/updateUser")
	public BaseResult<Object> updateUser(HttpServletRequest req, User users) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			User u = userService.selectByPrimaryKey(users.getId()+"");
			if(u.getLoginName().equals(users.getLoginName())){
				u.setLoginName(users.getLoginName());
				u.setName(users.getName());
				u.setPassword(MD5Utils.MD5(users.getPassword()));
				u.setUpdateUser("" + user.getId());
			}else{
				User us = userService.getUserByLoginName(users.getLoginName());
				if(us == null){
					u.setLoginName(users.getLoginName());
					u.setName(users.getName());
					u.setPassword(MD5Utils.MD5(MD5Utils.MD5(users.getPassword())));
					u.setUpdateUser("" + user.getId());
				}else{
					return ResultsUtil.error(ExceptionEnum.userNews.UN05.getCode(), ExceptionEnum.userNews.UN05.getMsg());
				}
			}
			if (userService.updateByPrimaryKey(u) > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.userNews.UN02.getCode(), ExceptionEnum.userNews.UN02.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 编辑用户出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑用户出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 删除用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/deleteUser")
	public BaseResult<Object> deleteUser(HttpServletRequest req) {
		String userId = req.getParameter("userId");
		try {
			if (userService.deleteUser(userId) > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.userNews.UN03.getCode(), ExceptionEnum.userNews.UN03.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 删除用户出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除用户出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 用户角色列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/listUserRole/{type}")
	public ModelAndView listUserRole(HttpServletRequest req, @PathVariable("type") String userId) {
		ModelAndView mv = new ModelAndView("system/user/listUserRole");
		try {
			List<Role> RoleList = roleService.getUserRoleByUserId(userId); // 当前用户的角色
			List<Role> allRoleList = roleService.getList();// 所有的角色列表
			for (Role r : allRoleList) {
				for (Role s : RoleList) {
					if (r.getId().equals(s.getId()))
						r.setChecked("1");
				}
			}
			mv.addObject("userId", userId);
			mv.addObject("allRoleList", allRoleList);
		} catch (Exception e) {
			logger.error("## 查询用户角色列表信息出错", e);
		}
		return mv;
	}

	// 提交用户角色信息
	@PostMapping(value = "/addUserRole")
	public BaseResult<Object> addUserRole(HttpServletRequest req) {
		int count = 0;
		try {
			String userId = req.getParameter("userId");
			userService.deleteUserRoleByUserId(userId);
			String ids = req.getParameter("ids");
			if (ids == null || ids == "") {
				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
			} else {
				String[] roleId = ids.split(",");
				for (int i = 0; i < roleId.length; i++)
					count = userService.addUserRole(userId, roleId[i]);
			}
			if (count > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.userNews.UN06.getCode(), ExceptionEnum.userNews.UN06.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 新增用户角色出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增用户角色出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

}
