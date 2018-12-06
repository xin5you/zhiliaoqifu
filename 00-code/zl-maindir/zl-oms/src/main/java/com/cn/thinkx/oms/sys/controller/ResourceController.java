package com.cn.thinkx.oms.sys.controller;

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
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.oms.sys.service.ResourceService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.Constants.LoginType;
import com.ebeijia.zl.common.utils.tools.StringUtil;

@Controller
@RequestMapping(value = "sys/resource")
public class ResourceController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ResourceService resourceService;

	
	@RequestMapping(value = "/listResource")
	public ModelAndView listResource(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/resource/listResource");
		String operStatus=StringUtil.nullToString(req.getParameter("operStatus"));
		
		List<Resource> pageList = null;
		try {
			Resource resource = new Resource();
			resource.setLoginType(LoginType.LoginType1.getCode());
			pageList=resourceService.getResourceList(resource);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询列表信息出错", e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}
	
	
	@RequestMapping(value = "/intoAddResource")
	public ModelAndView intoAddResource(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/resource/addResource");
		Resource resource=new Resource();
		resource.setLoginType(LoginType.LoginType1.getCode());
		resource.setResourceType("0");
		List<Resource>  resourceList=resourceService.getResourceList(resource);
		mv.addObject("resourceList", resourceList);
		
		String resourceId=StringUtil.nullToString(req.getParameter("resourceId"));
		Resource parantRes=resourceService.getResourceById(resourceId);
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
			Resource resource=getResourceInfo(req);
			Resource checkResource=resourceService.getResourceByKey(resource.getResourceKey(), LoginType.LoginType1.getCode());
			if(checkResource !=null){
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", checkResource.getResourceKey()+"已经存在，请重新输入");
				return resultMap;
			}
			resourceService.insertResource(resource);
		} catch (Exception e) {
			e.printStackTrace();
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
		String resourceId=req.getParameter("resourceId");
		Resource resource=resourceService.getResourceById(resourceId);
		
		//查找上级菜单列表
		Resource resource1=new Resource();
		resource1.setResourceType("0");
		resource1.setLoginType(LoginType.LoginType1.getCode());
		List<Resource>  resourceList=resourceService.getResourceList(resource1);
		
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
			Resource resource=getResourceInfo(req);
			Resource checkResource=resourceService.getResourceByKey(resource.getResourceKey(), resource.getLoginType());
			if(checkResource !=null){
				if(!resource.getId().equals(checkResource.getId())){
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", checkResource.getResourceKey()+"已经存在，请重新输入");
					return resultMap;
				}
			}
			resourceService.updateResource(resource);
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
		Resource resource=null;
		String resourceId=StringUtil.nullToString(req.getParameter("resourceId"));
		HttpSession session = req.getSession();
		User u = (User)session.getAttribute(Constants.SESSION_USER);
		if(!StringUtil.isNullOrEmpty(resourceId)){
			resource=resourceService.getResourceById(resourceId);
		}else{
			resource=new Resource();
			resource.setId(UUID.randomUUID().toString());
			resource.setCreateUser(u.getId());
			resource.setCreateTime(System.currentTimeMillis());
		}
		resource.setUpdateUser(u.getId());
		resource.setResourceName(StringUtil.nullToString(req.getParameter("resourceName")));
		resource.setResourceKey(StringUtil.nullToString(req.getParameter("resourceKey")).toUpperCase());
		resource.setUrl(StringUtil.nullToString(req.getParameter("url")));
		resource.setDescription(StringUtil.nullToString(req.getParameter("description")));
		resource.setIcon(StringUtil.nullToString(req.getParameter("icon")));
		resource.setSeq(Integer.parseInt(StringUtil.nullToString(req.getParameter("seq"))));
		resource.setResourceType(StringUtil.nullToString(req.getParameter("resourceType")));
		resource.setPid(StringUtil.nullToString(req.getParameter("pid")));
		resource.setDataStat(StringUtil.nullToString(req.getParameter("dataStat")));
		resource.setLoginType(LoginType.LoginType1.getCode());
		resource.setUpdateTime(System.currentTimeMillis());
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
		String resourceId=req.getParameter("resourceId");
		try {
			resourceService.deleteResource(resourceId);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除资源失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
}
