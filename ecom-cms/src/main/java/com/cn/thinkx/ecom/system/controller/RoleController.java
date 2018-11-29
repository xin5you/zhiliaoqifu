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
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.system.domain.Resource;
import com.cn.thinkx.ecom.system.domain.Role;
import com.cn.thinkx.ecom.system.domain.User;
import com.cn.thinkx.ecom.system.service.ResourceService;
import com.cn.thinkx.ecom.system.service.RoleService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "system/role")
public class RoleController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	/**
	 * 角色列表
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listRole")
	public ModelAndView listRole(HttpServletRequest req, Role role) {
		ModelAndView mv = new ModelAndView("system/role/listRole");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Role> pageList = roleService.getRolePage(startNum, pageSize, role);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询角色列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 角色列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/listRole")
	public ModelAndView listRole(HttpServletRequest req, HttpServletResponse response, Role role) {
		ModelAndView mv = new ModelAndView("system/role/listRole");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Role> pageList = roleService.getRolePage(startNum, pageSize, role);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询角色列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 根据编号查询角色信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getRole")
	public Role getRole(HttpServletRequest req) {
		String roleId = req.getParameter("roleId");
		Role role = new Role();
		try {
			role = roleService.selectByPrimaryKey(roleId);
		} catch (Exception e) {
			logger.error("## 查询主键为[" + roleId + "]的角色信息出错", e);
		}
		return role;
	}

	/**
	 * 新增角色信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/addRole")
	public BaseResult<Object> addRole(HttpServletRequest req, Role role) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			Role r = roleService.selectByName(role.getName());
			if (r != null) {
				return ResultsUtil.error(ExceptionEnum.roleNews.REN05.getCode(), ExceptionEnum.roleNews.REN05.getMsg());
			} else {
				role.setIsdefault("0");
				role.setCreateUser("" + user.getId());
				role.setUpdateUser("" + user.getId());
				if (roleService.insert(role) > 0) {
					return ResultsUtil.success();
				} else {
					return ResultsUtil.error(ExceptionEnum.roleNews.REN01.getCode(),
							ExceptionEnum.roleNews.REN01.getMsg());
				}
			}
		} catch (BizHandlerException e) {
			logger.error("## 新增角色出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增角色出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 修改角色信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/updateRole")
	public BaseResult<Object> updateRole(HttpServletRequest req, Role role) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			Role r = roleService.selectByName(role.getName());
			Role oldRole = roleService.selectByPrimaryKey(role.getId() + "");

			if (oldRole.getName().equals(role.getName())) {
				role.setUpdateUser("" + user.getId());
			} else {
				if (r != null)
					return ResultsUtil.error(ExceptionEnum.roleNews.REN05.getCode(), ExceptionEnum.roleNews.REN05.getMsg());
				else
					role.setUpdateUser("" + user.getId());
			}
			if (roleService.updateByPrimaryKey(role) > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.roleNews.REN02.getCode(), ExceptionEnum.roleNews.REN02.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 编辑角色出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 编辑角色出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 删除角色信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/deleteRole")
	public BaseResult<Object> deleteRole(HttpServletRequest req) {
		String roleId = req.getParameter("roleId");
		try {
			roleService.deleteRole(roleId);
			return ResultsUtil.success();
		} catch (BizHandlerException e) {
			logger.error("## 删除角色出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除角色出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	/**
	 * 角色资源列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/listRoleResource/{type}")
	public ModelAndView listRoleResource(HttpServletRequest req, @PathVariable("type") String roleId) {
		ModelAndView mv = new ModelAndView("system/role/listRoleResource");
		try {
			List<Resource> roleResList = resourceService.getRoleResourceByRoleId(roleId); // 当前角色的权限
			List<Resource> allResourceList = resourceService.getList1(); // 所有的资源列表
			for (Resource r : allResourceList) {
				for (Resource s : roleResList) {
					if (r.getId().equals(s.getId()))
						r.setChecked("1");
				}
			}
			mv.addObject("roleId", roleId);
			mv.addObject("allResourceList", allResourceList);
		} catch (Exception e) {
			logger.error("## 查询角色资源列表信息出错", e);
		}
		return mv;
	}

	// 提交授权信息
	@PostMapping(value = "/addRoleResource")
	public BaseResult<Object> addRoleResource(HttpServletRequest req) {
		int count = 0;
		try {
			String roleId = req.getParameter("roleId");
			roleService.deleteRoleResourceByRoleId(roleId);
			String ids = req.getParameter("ids");
			if (ids == null || ids == "") {
				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
			} else {
				String[] resourceId = ids.split(",");
				for (int i = 0; i < resourceId.length; i++)
					count = roleService.addRoleResource(roleId, resourceId[i]);
			}
			if (count > 0)
				return ResultsUtil.success();
			else
				return ResultsUtil.error(ExceptionEnum.roleNews.REN06.getCode(), ExceptionEnum.roleNews.REN06.getMsg());
		} catch (BizHandlerException e) {
			logger.error("## 新增角色资源出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增角色资源出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

}
