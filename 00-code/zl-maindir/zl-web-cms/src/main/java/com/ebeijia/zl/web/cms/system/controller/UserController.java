package com.ebeijia.zl.web.cms.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.basics.system.domain.Role;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.domain.UserRole;
import com.ebeijia.zl.basics.system.service.RoleService;
import com.ebeijia.zl.basics.system.service.UserRoleService;
import com.ebeijia.zl.basics.system.service.UserService;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.common.utils.tools.MD5Utils;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "system/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

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
			return ResultsUtil.error(ExceptionEnum.UserNews.UN07.getCode(), ExceptionEnum.UserNews.UN07.getMsg());
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			User currUser = userService.getById(user.getId());
			if (currUser != null) {
				if (!currUser.getPassword().equals(oldPasswrod))
					return ResultsUtil.error(ExceptionEnum.UserNews.UN08.getCode(), ExceptionEnum.UserNews.UN08.getMsg());
				currUser.setPassword(newPasswordPage);
				if (!userService.updateById(currUser)) {
					return ResultsUtil.error(ExceptionEnum.UserNews.UN09.getCode(), ExceptionEnum.UserNews.UN09.getMsg());
				}
			}
		} catch (BizHandlerException e) {
			logger.error("## 密码修改失败", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 密码修改失败", e);
			return ResultsUtil.error(ExceptionEnum.UserNews.UN09.getCode(), ExceptionEnum.UserNews.UN09.getMsg());
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
			user.setLoginType(LoginType.LoginType2.getCode());
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
	 * @param
	 * @return
	 */
	@PostMapping(value = "/listUser")
	public ModelAndView listUser(HttpServletRequest req, HttpServletResponse resp, User user) {
		ModelAndView mv = new ModelAndView("system/user/listUser");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			user.setLoginType(LoginType.LoginType2.getCode());
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
			user = userService.getById(userId);
		} catch (Exception e) {
			logger.error("## 查询主键为[" + userId + "]的用户信息出错", e);
		}
		return user;
	}

	/**
	 * 新增用户信息
	 * 
	 * @param req
	 * @param
	 * @return
	 */
	@PostMapping(value = "/addUser")
	public BaseResult<Object> addUser(HttpServletRequest req, User u) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			u.setId(IdUtil.getNextId());
			u.setPassword(MD5Utils.MD5(u.getPassword()));
			u.setIsdefault("1");
			u.setLoginType(LoginType.LoginType2.getCode());
			u.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			u.setCreateUser(user.getId());
			u.setUpdateUser(user.getId());
			u.setCreateTime(System.currentTimeMillis());
			u.setUpdateTime(System.currentTimeMillis());
			u.setLockVersion(0);
			User us = userService.getUserByName(null, u.getLoginName(), LoginType.LoginType2.getCode());
			if(us == null){
				if (userService.save(u))
					return ResultsUtil.success();
				else
					return ResultsUtil.error(ExceptionEnum.UserNews.UN01.getCode(), ExceptionEnum.UserNews.UN01.getMsg());
			}else{
				return ResultsUtil.error(ExceptionEnum.UserNews.UN05.getCode(), ExceptionEnum.UserNews.UN05.getMsg());
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
	 * @param
	 * @return
	 */
	@PostMapping(value = "/updateUser")
	public BaseResult<Object> updateUser(HttpServletRequest req, User users) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			User u = userService.getById(users.getId());
			if(u.getLoginName().equals(users.getLoginName())){
				u.setLoginName(users.getLoginName());
				u.setUserName(users.getUserName());
				u.setPassword(MD5Utils.MD5(users.getPassword()));
				u.setUpdateUser(user.getId());
				u.setUpdateTime(System.currentTimeMillis());
				u.setLoginType(LoginType.LoginType2.getCode());
			}else{
				User us = userService.getUserByName(null, users.getLoginName(), LoginType.LoginType2.getCode());
				if(us == null){
					u.setLoginName(users.getLoginName());
					u.setUserName(users.getUserName());//后面改成userName
					u.setPassword(MD5Utils.MD5(MD5Utils.MD5(users.getPassword())));
					u.setUpdateUser(user.getId().toString());
					u.setUpdateTime(System.currentTimeMillis());
					u.setLoginType(LoginType.LoginType2.getCode());
				}else{
					return ResultsUtil.error(ExceptionEnum.UserNews.UN05.getCode(), ExceptionEnum.UserNews.UN05.getMsg());
				}
			}
			u.setLockVersion(u.getLockVersion() + 1);
			if (userService.updateById(u))
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.UserNews.UN02.getCode(), ExceptionEnum.UserNews.UN02.getMsg());
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
	 * @param
	 * @return
	 */
	@PostMapping(value = "/deleteUser")
	public BaseResult<Object> deleteUser(HttpServletRequest req) {
		String userId = req.getParameter("userId");
		try {
			if (userService.removeById(userId))
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.UserNews.UN03.getCode(), ExceptionEnum.UserNews.UN03.getMsg());
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
	 * @param
	 * @return
	 */
	@PostMapping(value = "/listUserRole/{type}")
	public ModelAndView listUserRole(HttpServletRequest req, @PathVariable("type") String userId) {
		ModelAndView mv = new ModelAndView("system/user/listUserRole");
		try {
			Role role = new Role();
			role.setLoginType(LoginType.LoginType2.getCode());
			List<Role> RoleList = roleService.getUserRoleByUserId(userId); // 当前用户的角色
			List<Role> allRoleList = roleService.getRoleList(role);// 所有的角色列表
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
			if (userRoleService.deleteUserRoleByUserId(userId) < 1) {
				return ResultsUtil.error(ExceptionEnum.UserNews.UN06.getCode(), ExceptionEnum.UserNews.UN06.getMsg());
			}
			String ids = req.getParameter("ids");
			List<UserRole> urList = new ArrayList<>();
			if (ids == null || ids == "") {
				return ResultsUtil.error(ExceptionEnum.UserNews.UN10.getCode(), ExceptionEnum.UserNews.UN10.getMsg());
			} else {
				String[] roleId = ids.split(",");
				for (int i = 0; i < roleId.length; i++) {
					UserRole ur = new UserRole();
					ur.setId(IdUtil.getNextId());
					ur.setUserId(userId);
					ur.setRoleId(roleId[i]);
					urList.add(ur);
				}
			}
			if (userRoleService.saveBatch(urList))
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.UserNews.UN06.getCode(), ExceptionEnum.UserNews.UN06.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 新增用户角色出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增用户角色出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

}
