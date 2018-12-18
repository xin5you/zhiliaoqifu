package com.ebeijia.zl.web.oms.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.system.domain.Role;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.service.RoleService;
import com.ebeijia.zl.basics.system.service.UserService;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.common.utils.tools.MD5Utils;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.oms.sys.service.UserRoleResourceService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "diy/diyUser")
public class DiyUserController{

	Logger logger = LoggerFactory.getLogger(DiyUserController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleResourceService userRoleResourceService;

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
		String userName = StringUtil.nullToString(req.getParameter("userName"));
		String phoneNo = StringUtil.nullToString(req.getParameter("phoneNo"));
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		
		PageInfo<User> pageList = null;
		User bizUser = new User();
		try {
			bizUser.setPhoneNo(phoneNo);
			bizUser.setUserName(userName);
			bizUser.setLoginType(LoginType.LoginType3.getCode());
			pageList = userService.getUserPage(startNum, pageSize, bizUser);
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
			User diyUser = getDiyUserInfo(req);
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
			
			String[] roleIds = {roleId};
			if (userRoleResourceService.insertUserRole(diyUser, roleIds) < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增用户失败，请重新添加");
				return resultMap;
			}

			//TODO 添加分销商信息于用户的关联
			
		} catch (Exception e) {
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
		List<Role> diyUserRoleList = roleService.getUserRoleByUserId(diyUser.getId().toString());
		
		mv.addObject("diyRoleList", diyRoleList);
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
			User diyUser = getDiyUserInfo(req);
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
			if (userRoleResourceService.updateUserRole(diyUser, roleIds) < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑用户失败，请重新操作");
				return resultMap;
			}
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
	private User getDiyUserInfo(HttpServletRequest req) throws Exception {
		HttpSession session = req.getSession();
		User u = (User)session.getAttribute(Constants.SESSION_USER);
		
		String userId = StringUtil.nullToString(req.getParameter("userId"));
		
		User diyUser = null;
		if (!StringUtil.isNullOrEmpty(userId)) {
			diyUser = userService.getById(userId);
		} else {
			diyUser = new User();
			diyUser.setId(IdUtil.getNextId());
			diyUser.setCreateUser(u.getId().toString());
			diyUser.setCreateTime(System.currentTimeMillis());
			diyUser.setIsdefault("1");
			diyUser.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			diyUser.setLockVersion(0);
		}
		
		diyUser.setUserName(StringUtil.nullToString(req.getParameter("userName")));
		String password = StringUtil.nullToString(req.getParameter("password"));
		if (!StringUtil.isNullOrEmpty(password)) {
			diyUser.setPassword(password);
		}
		diyUser.setPhoneNo(StringUtil.nullToString(req.getParameter("phoneNo")));
		diyUser.setLoginType(LoginType.LoginType3.getCode());
		diyUser.setSupplierId(StringUtil.nullToString(req.getParameter("supplierId")));
		diyUser.setUpdateUser(u.getId().toString());
		diyUser.setUpdateTime(System.currentTimeMillis());
		return diyUser;
	}

}
