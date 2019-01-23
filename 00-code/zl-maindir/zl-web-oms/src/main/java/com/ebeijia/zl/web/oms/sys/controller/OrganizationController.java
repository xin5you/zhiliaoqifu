package com.ebeijia.zl.web.oms.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.basics.system.domain.Resource;
import com.ebeijia.zl.basics.system.service.UserService;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.web.oms.sys.service.UserRoleResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.system.domain.Organization;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.service.OrganizationService;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.tools.StringUtil;

@Controller
@RequestMapping(value = "sys/organization")
public class OrganizationController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleResourceService userRoleResourceService;

	public static void main(String[] args) {
		String str = null;
		System.out.println(str+"");
	}

	@RequestMapping(value = "/listOrganization")
	public ModelAndView listorganization(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/organization/listOrganization");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		
		try {
			List<Organization> pageList = userRoleResourceService.getOmsOrganization();
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询列表信息出错", e);
		}

		mv.addObject("operStatus", operStatus);
		return mv;
	}
	
	@RequestMapping(value = "/intoAddOrganization")
	public ModelAndView intoAddOrganization(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/organization/addOrganization");
		Organization entity = new Organization();
		List<Organization> entityList = userRoleResourceService.getOmsOrganization();
		if (entityList != null) {
			entityList = entityList.stream().filter(r -> !"0".equals(r.getId())).collect(Collectors.toList());
		}
		mv.addObject("entityList", entityList);
		return mv;
	}

	/**
	 * 部门添加提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOrganizationCommit")
	@ResponseBody
	public Map<String, Object> addOrganizationCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("status", Boolean.TRUE);
		try {
			Organization organization = getOrganizationInfo(req);
			if (!organizationService.save(organization)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增部门失败，请重新添加");
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增部门失败，请重新添加");
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
	@RequestMapping(value = "/intoEditOrganization")
	public ModelAndView intoEditOrganization(HttpServletRequest req, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("sys/organization/editOrganization");
		String organId = req.getParameter("organId");
		Organization organ = organizationService.getById(organId);
		
		//查找上级菜单列表
		List<Organization> entityList = userRoleResourceService.getOmsOrganization();
		if (entityList != null) {
			entityList = entityList.stream().filter(r -> !"0".equals(r.getId())).collect(Collectors.toList());
		}
		mv.addObject("entityList", entityList);
		mv.addObject("organ", organ);
		return mv;
	}
	
	
	/**
	 * 部门编辑 提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editOrganizationCommit")
	@ResponseBody
	public Map<String, Object> editOrganizationCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			Organization organ = getOrganizationInfo(req);
			if (!organizationService.updateById(organ)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑部门失败，请重新操作");
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑部门失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}

	
	/**
	* @Title: getorganizationInfo
	* @Description: 部门表封装
	* @param @param req
	* @param @return
	* @param @throws Exception
	* @return organization    返回类型
	* @throws
	*/ 
	private Organization getOrganizationInfo(HttpServletRequest req) throws Exception {
		HttpSession session = req.getSession();
		User u = (User)session.getAttribute(Constants.SESSION_USER);
		
		String organId = StringUtil.nullToString(req.getParameter("organId"));
		
		Organization organ = null;
		if(!StringUtil.isNullOrEmpty(organId)){
			organ = organizationService.getById(organId);
		}else{
			organ = new Organization();
			organ.setId(IdUtil.getNextId());
			organ.setCreateTime(System.currentTimeMillis());
			organ.setCreateUser(u.getId());
			organ.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			organ.setLockVersion(0);
		}
	
		organ.setName(StringUtil.nullToString(req.getParameter("name")));
		organ.setCode(StringUtil.nullToString(req.getParameter("code")));
		organ.setAddress(StringUtil.nullToString(req.getParameter("address")));
		organ.setIcon(StringUtil.nullToString(req.getParameter("icon")));
		organ.setSeq(Integer.parseInt(req.getParameter("seq")));
		organ.setPid(StringUtil.nullToString(req.getParameter("pid")));
		organ.setUpdateTime(System.currentTimeMillis());
		organ.setUpdateUser(u.getId());
		return organ;
	}

	
	/**
	 * 删除部门 commit
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteOrganizationCommit")
	@ResponseBody
	public Map<String, Object> deleteOrganizationCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String organId = req.getParameter("organId");
		User user = new User();
		user.setOrganizationId(organId);
		user.setLoginType(LoginType.LoginType1.getCode());

		try {
			User u = userService.getUserByOrganId(user);
			if (u != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除部门失败，该部门下已有用户");
				return resultMap;
			}
			if (!organizationService.removeById(organId)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除部门失败，请重新操作");
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除部门失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
}
