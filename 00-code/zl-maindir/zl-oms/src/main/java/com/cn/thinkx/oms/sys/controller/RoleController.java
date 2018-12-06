package com.cn.thinkx.oms.sys.controller;

import java.util.ArrayList;
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

import com.cn.thinkx.oms.sys.model.Resource;
import com.cn.thinkx.oms.sys.model.Role;
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.oms.sys.model.ZTreeResource;
import com.cn.thinkx.oms.sys.service.ResourceService;
import com.cn.thinkx.oms.sys.service.RoleService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.TransCode.LoginType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "sys/role")
public class RoleController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/listRole")
	public ModelAndView loginIndex(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/role/listRole");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		
		PageInfo<Role> pageList = null;
		Role role=new Role();
		role.setLoginType(LoginType.LoginType1.getCode());
		try {
			pageList=roleService.getRolePage(startNum, pageSize, role);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询角色列表信息出错", e);
		}
		mv.addObject("pageInfo", pageList);
		return mv;
	}
	
	/**
	 * 授权
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/roleAuthorization")
	public ModelAndView roleAuthorization(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/role/roleAuthorization");
		String roleId=req.getParameter("roleId");
		mv.addObject("roleId", roleId);
		return mv;
	}
	
	
	@RequestMapping(value = "/submitRoleAuthorization")
	@ResponseBody
	public Map<String, Object> submitRoleAuthorization(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String[] resourceIds=req.getParameterValues("ids[]");
		String roleId=req.getParameter("roleId");
		resultMap.put("status", Boolean.TRUE);
		try {

		roleService.editRoleResource(roleId, resourceIds);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "角色授权失败，请重新选择权限");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	

	
	/**
	 * 获取授权所有的资源列表
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getRoleResources")
	@ResponseBody
	public Map<String, Object> getRoleResources(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String roleId=req.getParameter("roleId");
		Resource resource = new Resource();
		resource.setLoginType(LoginType.LoginType1.getCode());
		List<Resource> allResourceList=resourceService.getResourceList(resource); //所有的资源列表
		
		List<Resource> roleResList=resourceService.getRoleResourceByRoleId(roleId); //当前角色的权限
		
		List<ZTreeResource> list=new ArrayList<ZTreeResource>();
		ZTreeResource entity=null;
		if(roleResList !=null && allResourceList.size()>0){
			for(int i=0;i<allResourceList.size();i++){
				entity=new ZTreeResource();
				entity.setId(allResourceList.get(i).getId());
				entity.setName(allResourceList.get(i).getResourceName());
				entity.setpId(allResourceList.get(i).getPid());
				for(int j=0;j<roleResList.size();j++){
					if(roleResList.get(j).getId().equals(allResourceList.get(i).getId())){
						entity.setChecked(true);
					}
				}
				list.add(entity);
			}
		}
		JSONArray jsonArray=JSONArray.fromObject(list);
		resultMap.put("json", jsonArray);
		return resultMap;
	}
	
	
	@RequestMapping(value = "/intoAddRole")
	public ModelAndView intoAddRole(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/role/addRole");
		
		return mv;
	}
	
	/**
	 * 角色添加提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addRoleCommit")
	@ResponseBody
	public Map<String, Object> addRoleCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String roleName=req.getParameter("roleName");
		String seq=req.getParameter("seq");
		String description=req.getParameter("description");
		try{
			Role role=new Role();
			role.setId(UUID.randomUUID().toString());
			role.setRoleName(roleName);
			role.setSeq(Integer.parseInt(seq));
			role.setDescription(description);
			role.setLoginType(LoginType.LoginType1.getCode());
			HttpSession session = req.getSession();
			User u = (User)session.getAttribute(Constants.SESSION_USER);
			role.setCreateUser(u.getId());
			role.setUpdateUser(u.getId());
			role.setCreateTime(System.currentTimeMillis());
			role.setUpdateTime(System.currentTimeMillis());
			roleService.insertRole(role);
			resultMap.put("status", Boolean.TRUE);
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg","添加失败，请稍微再试");
		}
		return resultMap;
	}
	
	
	@RequestMapping(value = "/intoEditRole")
	public ModelAndView intoEditRole(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/role/editRole");
		String roleId=req.getParameter("roleId");
		Role role=roleService.getRoleById(roleId);
		mv.addObject("role", role);
		return mv;
	}
	
	
	/**
	 * 角色编辑 提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editRoleCommit")
	@ResponseBody
	public Map<String, Object> editRoleCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String roleId=req.getParameter("roleId");
		String roleName=req.getParameter("roleName");
		String seq=req.getParameter("seq");
		String description=req.getParameter("description");
		try{
			Role role=roleService.getRoleById(roleId);
			role.setRoleName(roleName);
			role.setDescription(description);
			role.setSeq(Integer.parseInt(seq));
			role.setLoginType(LoginType.LoginType1.getCode());
			HttpSession session = req.getSession();
			User u = (User)session.getAttribute(Constants.SESSION_USER);
			role.setUpdateUser(u.getId());
			role.setUpdateTime(System.currentTimeMillis());
			roleService.updateRole(role);
			resultMap.put("status", Boolean.TRUE);
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg","编辑失败，请稍微再试");
		}
		return resultMap;
	}

	
	
	/**
	 * 删除角色 commit
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteRoleCommit")
	@ResponseBody
	public Map<String, Object> deleteRoleCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String roleId=req.getParameter("roleId");
		try {
			roleService.deleteRoleById(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "角色删除失败，请重试");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
}
