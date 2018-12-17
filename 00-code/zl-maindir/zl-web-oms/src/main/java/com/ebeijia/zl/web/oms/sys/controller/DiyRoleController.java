package com.ebeijia.zl.web.oms.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.web.oms.sys.model.Resource;
import com.ebeijia.zl.web.oms.sys.model.Role;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.sys.service.ResourceService;
import com.ebeijia.zl.web.oms.sys.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.web.oms.sys.model.ZTreeResource;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.Constants.LoginType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageInfo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value="diy/diyRole")
public class DiyRoleController {
	
	Logger logger = LoggerFactory.getLogger(DiyRoleController.class);
		
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 商户角色列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listDiyRole")
	public ModelAndView listDiyRole(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyRole/listDiyRole");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		
		PageInfo<Role> pageList = null;
		Role diyRole=new Role();
		diyRole.setLoginType(LoginType.LoginType3.getCode());
		try {
			pageList=roleService.getRolePage(startNum, pageSize, diyRole);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询角色列表信息出错", e);
		}
		mv.addObject("pageInfo", pageList);
		return mv;
	}
	
	/**
	 * 进入商户角色新增
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddDiyRole")
	public ModelAndView intoAddDiyRole(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyRole/addDiyRole");
		return mv;
	}
	
	/**
	 * 角色添加提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addDiyRoleCommit")
	@ResponseBody
	public Map<String, Object> addDiyRoleCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String roleName=req.getParameter("roleName");
		String seq=req.getParameter("seq");
		String description=req.getParameter("description");
		try{
			HttpSession session=req.getSession();
			User user=(User)session.getAttribute(Constants.SESSION_USER);
			Role role=new Role();
			role.setId(UUID.randomUUID().toString());
			role.setRoleName(roleName);
			role.setSeq(Integer.parseInt(seq));
			role.setDescription(description);
			role.setLoginType(LoginType.LoginType3.getCode());
			role.setCreateUser(user.getId());
			role.setUpdateUser(user.getId());
			role.setCreateTime(System.currentTimeMillis());
			role.setUpdateTime(System.currentTimeMillis());
			roleService.save(role);
			resultMap.put("status", Boolean.TRUE);
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg","添加失败，请稍微再试");
		}
		return resultMap;
	}
	
	/**
	 * 进入商户角色编辑
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoEditDiyRole")
	public ModelAndView intoEditDiyRole(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyRole/editDiyRole");
		String id=req.getParameter("id");
		Role diyRole=roleService.getById(id);
		mv.addObject("diyRole", diyRole);
		return mv;
	}
	
	/**
	 * 商户角色编辑 提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editDiyRoleCommit")
	@ResponseBody
	public Map<String, Object> editDiyRoleCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			Role diyRole = null;
			HttpSession session=req.getSession();
			User user=(User)session.getAttribute(Constants.SESSION_USER);
			String id = StringUtil.nullToString(req.getParameter("id"));
			if(!StringUtil.isNullOrEmpty(id)){
				diyRole = roleService.getById(id);
			}else{
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg","添加失败，请稍微再试");
				return resultMap;
			}
			diyRole.setId(id);
			diyRole.setRoleName(StringUtil.nullToString(req.getParameter("roleName")));
			diyRole.setSeq(Integer.parseInt(StringUtil.nullToString(req.getParameter("seq"))));
			diyRole.setDescription(StringUtil.nullToString(req.getParameter("description")));
			diyRole.setLoginType(LoginType.LoginType3.getCode());
			diyRole.setUpdateUser(user.getId().toString());
			diyRole.setUpdateTime(System.currentTimeMillis());
			roleService.updateById(diyRole);
			resultMap.put("status", Boolean.TRUE);
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg","添加失败，请稍微再试");
		}
		return resultMap;
	}
	
	/**
	 * 删除角色 commit
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteDiyRoleCommit")
	@ResponseBody
	public Map<String, Object> deleteDiyRoleCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String id=StringUtil.nullToString(req.getParameter("id"));
		try {
			roleService.removeById(id);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "角色授权失败，请重新选择权限");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 点击授权按钮，进入授权页面
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/diyRoleAuthorization")
	public ModelAndView diyRoleAuthorization(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyRole/diyRoleAuthorization");
		String id=req.getParameter("id");
		mv.addObject("roleId", id);
		return mv;
	}
	
	/**
	 * 获取授权所有的资源列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getDiyRoleResources")
	@ResponseBody
	public Map<String, Object> getDiyRoleResources(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String roleId=req.getParameter("id");
		//所有的资源列表
		Resource resource = new Resource();
		resource.setLoginType(LoginType.LoginType3.getCode());
		List<Resource> allResourceList=resourceService.getResourceList(resource); 
		//根据角色的id查看对应的资源信息
		List<Resource> roleResList=resourceService.getRoleResourceByRoleId(roleId); 
		
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
	
	/**
	 * 授权保存提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/submitDiyRoleAuthorization")
	@ResponseBody
	public Map<String, Object> submitDiyRoleAuthorization(HttpServletRequest req, HttpServletResponse response) {
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
	
}
