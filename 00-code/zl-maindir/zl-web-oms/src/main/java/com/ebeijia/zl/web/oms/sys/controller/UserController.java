package com.ebeijia.zl.web.oms.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.ebeijia.zl.basics.system.domain.Organization;
import com.ebeijia.zl.basics.system.domain.Role;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.service.OrganizationService;
import com.ebeijia.zl.basics.system.service.RoleService;
import com.ebeijia.zl.basics.system.service.UserService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.common.utils.tools.MD5Utils;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.oms.sys.service.UserRoleResourceService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "sys/user")
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleResourceService userRoleResourceService;
	
	@RequestMapping(value = "/listUser")
	public ModelAndView listUser(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/user/listUser");
		
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		String loginName = StringUtil.nullToString(req.getParameter("loginName"));
		String userName = StringUtil.nullToString(req.getParameter("userName"));
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		
		PageInfo<User> pageList = null;
		User bizUser = new User();
		try {
			bizUser.setUserName(userName);
			bizUser.setLoginName(loginName);
			bizUser.setLoginType(LoginType.LoginType1.getCode());
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
	
	@RequestMapping(value = "/intoAddUser")
	public ModelAndView intoAddUser(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/user/addUser");
		List<Organization> organizationList = organizationService.getOrganizationList(new Organization());
		Role role = new Role();
		role.setLoginType(LoginType.LoginType1.getCode());
		List<Role> roleList = roleService.getRoleList(role);
		mv.addObject("organizationList", organizationList);
		mv.addObject("roleList", roleList);
		return mv;
	}
	
	/**
	 * 添加提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addUserCommit")
	@ResponseBody
	public Map<String, Object> addUserCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("status", Boolean.TRUE);
		String [] rolesIds=req.getParameterValues("rolesIds[]");

		try {
			User user = getUserInfo(req);
			
			User loginNameUser=userService.getUserByName("", user.getLoginName(), LoginType.LoginType1.getCode());
			if(loginNameUser != null){
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "当前登陆名已存在，请重新输入");
				return resultMap;
			}
			user.setPassword(MD5Utils.MD5(user.getPassword()));
			if(userRoleResourceService.insertUserRole(user, rolesIds) < 1){
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增用户失败，请重新添加");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增用户失败，请重新添加");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 修改密码
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updatePwdCommit")
	@ResponseBody
	public Map<String, Object> updatePwdCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("status", Boolean.TRUE);
		String  oldPasswrod=req.getParameter("oldPasswrod");
		String  newPasswordPage=req.getParameter("newPasswordPage");
		String  newPassword2Page=req.getParameter("newPassword2Page");
		if(!newPasswordPage.equals(newPassword2Page)){
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "修改密码失败，请重新提交");
			return resultMap;
		}
		try {
			HttpSession session = req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			User currUser = userService.getById(user.getId().toString());
			if(currUser != null){
				if(!currUser.getPassword().equals(oldPasswrod)){
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "请输入正确的旧密码");
					return resultMap;
				}
				currUser.setPassword(newPasswordPage);
				userService.updateById(currUser);
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "修改密码失败，请重新提交");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/intoEditUser")
	public ModelAndView intoEditUser(HttpServletRequest req, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("sys/user/editUser");
		String userId=req.getParameter("userId");
		User user=userService.getById(userId);
		
		List<Organization> organizationList=organizationService.getOrganizationList(new Organization());
		Role role = new Role();
		role.setLoginType(LoginType.LoginType1.getCode());
		List<Role> roleList=roleService.getRoleList(role);
		
		List<Role> userRoleList=roleService.getUserRoleByUserId(user.getId().toString());
		mv.addObject("userRoleList", userRoleList);
		
		mv.addObject("organizationList", organizationList);
		mv.addObject("roleList", roleList);
		mv.addObject("currUser", user);
		return mv;
	}
	
	
	/**
	 * 用户编辑 提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editUserCommit")
	@ResponseBody
	public Map<String, Object> editUserCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("status", Boolean.TRUE);
		String [] rolesIds = req.getParameterValues("rolesIds[]");

		try {
			User user = getUserInfo(req);
			
			User loginNameUser = userService.getUserByName("", user.getLoginName(), LoginType.LoginType1.getCode());
			if(loginNameUser != null){
				if(!user.getId().equals(loginNameUser.getId())){
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "当前登陆名已存在，请重新输入");
					return resultMap;
				}
			}
			if (userRoleResourceService.updateUserRole(user, rolesIds) < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑用户失败，请重新操作");
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑用户失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}

	
	/**
	* @Title: getUserInfo
	* @Description: 用户表封装
	* @param @param req
	* @param @return
	* @param @throws Exception
	* @return User    返回类型
	* @throws
	*/ 
	private User getUserInfo(HttpServletRequest req) throws Exception {
		String userId = StringUtil.nullToString(req.getParameter("userId"));
		
		HttpSession session = req.getSession();
		User u = (User)session.getAttribute(Constants.SESSION_USER);
		
		User user = null;
		if(!StringUtil.isNullOrEmpty(userId)){
			user = userService.getById(userId);
		}else{
			user = new User();
			user.setId(UUID.randomUUID().toString());//后面用uuid设置userId值
			user.setCreateUser(u.getId());
			user.setCreateTime(System.currentTimeMillis());
			user.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			user.setLockVersion(0);
			user.setIsdefault("1");
		}
		
		user.setLoginName(StringUtil.nullToString(req.getParameter("loginName")));
		user.setUserName(StringUtil.nullToString(req.getParameter("userName")));
		String password = StringUtil.nullToString(req.getParameter("password"));
		if(!StringUtil.isNullOrEmpty(password)){
			user.setPassword(password);
		}
		user.setLoginType(LoginType.LoginType1.getCode());
		user.setOrganizationId(StringUtil.nullToString(req.getParameter("organizationId")));
		user.setUpdateUser(u.getId());
		user.setUpdateTime(System.currentTimeMillis());
		return user;
	}

	
	/**
	 * 删除用户 commit
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteUserCommit")
	@ResponseBody
	public Map<String, Object> deleteUserCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String userId=req.getParameter("userId");
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
	
	public static void main(String[] args) {
		/*System.out.println(UUID.randomUUID().toString());
		String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long("1543741291");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        System.out.println(res);
        System.out.println(System.currentTimeMillis());*/
		
		System.out.println(UUID.randomUUID().toString());
	}
	
}
