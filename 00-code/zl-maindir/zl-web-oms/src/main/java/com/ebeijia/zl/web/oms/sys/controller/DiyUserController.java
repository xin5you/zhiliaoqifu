package com.ebeijia.zl.web.oms.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.web.oms.sys.model.Role;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.sys.service.RoleService;
import com.ebeijia.zl.web.oms.sys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.Constants.LoginType;
import com.ebeijia.zl.common.utils.tools.MD5Utils;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "diy/diyUser")
public class DiyUserController{

	Logger logger = LoggerFactory.getLogger(DiyUserController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	/**
	 * 查询商户用户列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listDiyUser")
	public ModelAndView listDiyUser(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyUser/listDiyUser");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		User bizUser=new User();
		PageInfo<User> pageList = null;
		try {
			HttpSession session=req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			bizUser = getDiyUserInfo(req, user);
			pageList = userService.getDiyUserPage(startNum, pageSize, bizUser);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询角色列表信息出错", e);
		}
		mv.addObject("bizUser", bizUser);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}

	/**
	 * 进入新增页面
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddDiyUser")
	public ModelAndView intoAddDiyUser(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyUser/addDiyUser");
		// 获取角色列表
		Role role = new Role();
		role.setLoginType(LoginType.LoginType3.getCode());
		List<Role> diyRoleList = roleService.getRoleList(role);
		mv.addObject("diyRoleList", diyRoleList);
		//TODO 查询分销商信息于用户的关联
		
		return mv;
	}

	/**
	 * 用户添加提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addDiyUserCommit")
	@ResponseBody
	public Map<String, Object> addDiyUserCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("status", Boolean.TRUE);
		String roleId = StringUtil.nullToString(req.getParameter("roleId"));
		try {
			HttpSession session=req.getSession();
			User user=(User)session.getAttribute(Constants.SESSION_USER);
			User diyUser = getDiyUserInfo(req, user);
			User phoneNoUser = userService.getUserByPhoneNo(diyUser);
			if (phoneNoUser != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "当前手机号码已存在，请重新输入");
				return resultMap;
			}
			User loginNameUser = userService.getUserByName(diyUser.getUserName(), "", LoginType.LoginType3.getCode());
			if (loginNameUser != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "当前用户名已存在，请重新输入");
				return resultMap;
			}
			diyUser.setPassword(MD5Utils.MD5(diyUser.getPassword()));
			diyUser.setIsdefault("0");
			String[] roleIds = {roleId};
			userService.saveUser(diyUser, roleIds);

			//TODO 添加分销商信息于用户的关联
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增用户失败，请重新添加");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}

	/**
	 * 进入编辑用户
	 * 
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/intoEditDiyUser")
	public ModelAndView intoEditDiyUser(HttpServletRequest req, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("diy/diyUser/editDiyUser");
		String userId = req.getParameter("userId");
		User diyUser = userService.getById(userId);
		Role role = new Role();
		role.setLoginType(LoginType.LoginType3.getCode());
		// 获取角色列表
		List<Role> diyRoleList = roleService.getRoleList(role);
		mv.addObject("diyRoleList", diyRoleList);
		List<Role> diyUserRoleList = roleService.getUserRoleByUserId(diyUser.getId().toString());
		mv.addObject("diyUserRoleList", diyUserRoleList);
		mv.addObject("diyUser", diyUser);
		return mv;
	}

	/**
	 * 用户编辑 提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editDiyUserCommit")
	@ResponseBody
	public Map<String, Object> editUserCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("status", Boolean.TRUE);
		String roleId = StringUtil.nullToString(req.getParameter("roleId"));
		try {
			HttpSession session=req.getSession();
			User user=(User)session.getAttribute(Constants.SESSION_USER);
			User diyUser = getDiyUserInfo(req, user);
			User phoneNoUser = userService.getUserByPhoneNo(diyUser);
			if (phoneNoUser != null && !phoneNoUser.getId().equals(diyUser.getId())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "当前手机号码已存在，请重新输入");
				return resultMap;
			}
			User loginNameUser = userService.getUserByName(diyUser.getUserName(), "", diyUser.getLoginType());
			if (loginNameUser != null && !phoneNoUser.getId().equals(diyUser.getId())) {
				if (!diyUser.getId().equals(loginNameUser.getId())) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "当前用户名已存在，请重新输入");
					return resultMap;
				}
			}
			String[] roleIds = {roleId};
			userService.updateUser(diyUser, roleIds);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑用户失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}

	/**
	 * 删除用户 commit
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteDiyUserCommit")
	@ResponseBody
	public Map<String, Object> deleteUserCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String userId = req.getParameter("userId");
		try {
			userService.removeById(userId);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除用户失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}

	/**
	 * @Title: getUserInfo @Description: 用户表封装 @param @param
	 * req @param @return @param @throws Exception @return User 返回类型 @throws
	 */
	private User getDiyUserInfo(HttpServletRequest req, User user) throws Exception {
		User diyUser = null;
		String userId = StringUtil.nullToString(req.getParameter("userId"));
		if (!StringUtil.isNullOrEmpty(userId)) {
			diyUser = userService.getById(userId);
		} else {
			diyUser = new User();
			diyUser.setId(UUID.randomUUID().toString());
			diyUser.setCreateUser(user.getId().toString());
			diyUser.setCreateTime(System.currentTimeMillis());
		}
		diyUser.setUserName(StringUtil.nullToString(req.getParameter("userName")));
		String password = StringUtil.nullToString(req.getParameter("password"));
		if (!StringUtil.isNullOrEmpty(password)) {
			diyUser.setPassword(password);
		}
		diyUser.setPhoneNo(StringUtil.nullToString(req.getParameter("phoneNo")));
		diyUser.setLoginType(LoginType.LoginType3.getCode());
		diyUser.setSupplierId(StringUtil.nullToString(req.getParameter("supplierId")));
		diyUser.setUpdateUser(user.getId().toString());
		diyUser.setUpdateTime(System.currentTimeMillis());
		return diyUser;
	}

}
