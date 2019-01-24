package com.ebeijia.zl.web.oms.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.basics.system.domain.RoleResource;
import com.ebeijia.zl.basics.system.service.RoleResourceService;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.web.oms.sys.service.UserRoleResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.system.domain.Resource;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.service.ResourceService;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.LoginType;
import com.ebeijia.zl.common.utils.tools.StringUtil;

@Controller
@RequestMapping(value = "sys/resource")
public class ResourceController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ResourceService resourceService;

	@Autowired
	private RoleResourceService roleResourceService;

	@Autowired
	private UserRoleResourceService userRoleResourceService;

	/**
	 * 查询资源信息列表（分页）
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listResource")
	public ModelAndView listResource(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/resource/listResource");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		
		try {
			List<Resource> resourcesList = userRoleResourceService.getOmsResource();
			mv.addObject("pageInfo", resourcesList);
		} catch (Exception e) {
			logger.error("查询列表信息出错", e);
		}

		mv.addObject("operStatus", operStatus);
		return mv;
	}

	/**
	 * 跳转新增资源信息页面
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddResource")
	public ModelAndView intoAddResource(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/resource/addResource");
		
		String resourceId = StringUtil.nullToString(req.getParameter("resourceId"));
		
		Resource resource = new Resource();
		resource.setLoginType(LoginType.LoginType1.getCode());
		resource.setResourceType("0");
		
		/*List<Resource> resourceList = resourceService.getResourceList(resource);*/
		List<Resource> resourceList = userRoleResourceService.getOmsResource();
		resourceList = resourceList.stream().filter(r -> !"1".equals(r.getResourceType())).collect(Collectors.toList());
		
		Resource parantRes = resourceService.getById(resourceId);
		
		mv.addObject("resourceList", resourceList);
		mv.addObject("parantRes", parantRes);
		return mv;
	}
	
	/**
	 * 资源添加提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addResourceCommit")
	@ResponseBody
	public Map<String, Object> addResourceCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		try {
			Resource resource = getResourceInfo(req);
			Resource checkResource = resourceService.getResourceByKey(resource.getResourceKey(), LoginType.LoginType1.getCode());
			if(checkResource != null){
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", checkResource.getResourceKey()+"已经存在，请重新输入");
				return resultMap;
			}
			if (!resourceService.save(resource)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增资源失败，请重新添加");
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增资源失败，请重新添加");
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
	@RequestMapping(value = "/intoEditResource")
	public ModelAndView intoEditResource(HttpServletRequest req, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("sys/resource/editResource");
		
		String resourceId = req.getParameter("resourceId");
		Resource resource = resourceService.getById(resourceId);
		
		//查找菜单列表（资源类型是‘菜单’的）
		Resource resource1 = new Resource();
		resource1.setResourceType("0");
		resource1.setLoginType(LoginType.LoginType1.getCode());
		/*List<Resource> resourceList = resourceService.getResourceList(resource1);*/
		List<Resource> resourceList = userRoleResourceService.getOmsResource();
		resourceList = resourceList.stream().filter(r -> !"1".equals(r.getResourceType())).collect(Collectors.toList());


		mv.addObject("resourceList", resourceList);
		mv.addObject("resource", resource);
		return mv;
	}
	
	
	/**
	 * 资源编辑 提交
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editResourceCommit")
	@ResponseBody
	public Map<String, Object> editResourceCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			Resource resource = getResourceInfo(req);
			Resource checkResource = resourceService.getResourceByKey(resource.getResourceKey(), resource.getLoginType());
			if(checkResource != null){
				if(!resource.getId().equals(checkResource.getId())){
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", checkResource.getResourceKey()+"已经存在，请重新输入");
					return resultMap;
				}
			}
			if (!resourceService.updateById(resource)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑资源失败，请重新添加");
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑资源失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
	/**
	* @Title: getResourceInfo
	* @Description: 资源表封装
	* @param @param req
	* @param @return
	* @param @throws Exception
	* @return resource    返回类型
	* @throws
	*/ 
	private Resource getResourceInfo(HttpServletRequest req) throws Exception {
		String resourceId = StringUtil.nullToString(req.getParameter("resourceId"));
		String seq = StringUtil.nullToString(req.getParameter("seq"));
		
		HttpSession session = req.getSession();
		User u = (User)session.getAttribute(Constants.SESSION_USER);
		
		Resource resource = null;
		if (!StringUtil.isNullOrEmpty(resourceId)) {
			resource = resourceService.getById(resourceId);
		} else {
			resource = new Resource();
			resource.setId(IdUtil.getNextId());
			resource.setCreateUser(u.getId());
			resource.setCreateTime(System.currentTimeMillis());
			resource.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			resource.setLockVersion(0);
		}
		
		resource.setResourceName(StringUtil.nullToString(req.getParameter("resourceName")));
		resource.setResourceKey(StringUtil.nullToString(req.getParameter("resourceKey")).toUpperCase());
		resource.setUrl(StringUtil.nullToString(req.getParameter("url")));
		resource.setDescription(StringUtil.nullToString(req.getParameter("description")));
		resource.setIcon(StringUtil.nullToString(req.getParameter("icon")));
		if (!StringUtil.isNullOrEmpty(seq)) {
			resource.setSeq(Integer.parseInt(seq));
		}
		resource.setResourceType(StringUtil.nullToString(req.getParameter("resourceType")));
		resource.setPid(StringUtil.nullToString(req.getParameter("pid")));
		resource.setLoginType(LoginType.LoginType1.getCode());
		resource.setUpdateTime(System.currentTimeMillis());
		resource.setUpdateUser(u.getId());
		return resource;
	}

	/**
	 * 删除资源 commit
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteResourceCommit")
	@ResponseBody
	public Map<String, Object> deleteResourceCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		String resourceId = req.getParameter("resourceId");
		try {
			List<RoleResource> roleResourceList = roleResourceService.getRoleResourceByResourceId(resourceId);
			if (roleResourceList != null && roleResourceList.size() >= 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "该资源已被角色使用，不能删除");
				return resultMap;
			}
			if (!resourceService.removeById(resourceId)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除资源失败，请重新操作");
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除资源失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}

}
