package com.ebeijia.zl.web.cms.system.controller;

import com.ebeijia.zl.basics.system.domain.*;
import com.ebeijia.zl.basics.system.service.ResourceService;
import com.ebeijia.zl.basics.system.service.RoleResourceService;
import com.ebeijia.zl.basics.system.service.RoleService;
import com.ebeijia.zl.basics.system.service.UserRoleService;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.web.cms.base.exception.BizHandlerException;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "system/role")
public class RoleController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private RoleResourceService roleResourceService;

	@Autowired
	private UserRoleService userRoleService;

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
			role.setLoginType(LoginType.LoginType2.getCode());
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
			role.setLoginType(LoginType.LoginType2.getCode());
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
			role = roleService.getById(roleId);
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
			role.setLoginType(LoginType.LoginType2.getCode());
			Role roleName = roleService.getRoleByName(role);
			if (roleName != null) {
				return ResultsUtil.error(ExceptionEnum.roleNews.REN05.getCode(), ExceptionEnum.roleNews.REN05.getMsg());
			}
			Role roleSeq = roleService.getRoleBySeq(role);
			if (roleSeq != null) {
				return ResultsUtil.error(ExceptionEnum.roleNews.REN07.getCode(), ExceptionEnum.roleNews.REN07.getMsg());
			}
			role.setId(UUID.randomUUID().toString());
			role.setLoginType(LoginType.LoginType2.getCode());
			role.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			role.setIsdefault("1");
			role.setCreateUser(user.getId());
			role.setUpdateUser(user.getId());
			role.setCreateTime(System.currentTimeMillis());
			role.setUpdateTime(System.currentTimeMillis());
			role.setLockVersion(0);
			if (roleService.save(role)) {
				return ResultsUtil.success();
			} else {
				return ResultsUtil.error(ExceptionEnum.roleNews.REN01.getCode(),
						ExceptionEnum.roleNews.REN01.getMsg());
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
			role.setLoginType(LoginType.LoginType2.getCode());
			Role oldRole = roleService.getById(role.getId());

			if (!oldRole.getRoleName().equals(role.getRoleName())) {
				Role roleName = roleService.getRoleByName(role);
				if (roleName != null) {
					return ResultsUtil.error(ExceptionEnum.roleNews.REN05.getCode(), ExceptionEnum.roleNews.REN05.getMsg());
				}
			}
			if (!oldRole.getSeq().equals(role.getSeq())) {
				Role roleSeq = roleService.getRoleBySeq(role);
				if (roleSeq != null) {
					return ResultsUtil.error(ExceptionEnum.roleNews.REN07.getCode(), ExceptionEnum.roleNews.REN07.getMsg());
				}
			}

			role.setUpdateUser(user.getId());
			role.setUpdateTime(System.currentTimeMillis());
			role.setLockVersion(oldRole.getLockVersion() + 1);
			if (roleService.updateById(role))
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
			List<UserRole> userRoleList = userRoleService.getUserRoleByRoleId(roleId);
			List<RoleResource> roleResourceList = roleResourceService.getRoleResourceByRoleId(roleId);
			if (userRoleList != null || roleResourceList != null || userRoleList.size() >= 1 || roleResourceList.size() >= 1) {
				return ResultsUtil.error(ExceptionEnum.roleNews.REN03.getCode(), ExceptionEnum.roleNews.REN03.getMsg());
			}
			if (roleService.removeById(roleId)) {
				return ResultsUtil.success();
			} else {
				return ResultsUtil.error(ExceptionEnum.roleNews.REN03.getCode(), ExceptionEnum.roleNews.REN03.getMsg());
			}
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
			Resource resource = new Resource();
			resource.setLoginType(LoginType.LoginType2.getCode());
			List<Resource> allResourceList = resourceService.getResourceListByResource(resource);// 所有的资源列表
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
			if (roleResourceService.deleteRoleResourceByRoleId(roleId) < 1) {
				return ResultsUtil.error(ExceptionEnum.roleNews.REN06.getCode(), ExceptionEnum.roleNews.REN06.getMsg());
			}
			String ids = req.getParameter("ids");
			List<RoleResource> roleResList = new ArrayList<>();
			if (ids == null || ids == "") {
				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
			} else {
				String[] resourceId = ids.split(",");
				for (int i = 0; i < resourceId.length; i++) {
					RoleResource roleRes = new RoleResource();
					roleRes.setId(IdUtil.getNextId());
					roleRes.setResourceId(resourceId[i]);
					roleRes.setRoleId(roleId);
					roleResList.add(roleRes);
				}
			}
			if (roleResourceService.saveBatch(roleResList))
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
