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

import com.ebeijia.zl.web.oms.sys.model.Resource;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.sys.service.ResourceService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.constants.Constants.LoginType;
import com.ebeijia.zl.common.utils.tools.StringUtil;

@Controller
@RequestMapping(value="diy/diyResource")
public class DiyResourceController {
	
	Logger logger = LoggerFactory.getLogger(DiyResourceController.class);
	
	@Autowired
	private ResourceService resourceService;

	/**
	 * 查询资源列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listDiyResource")
	public ModelAndView listDiyResource(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyResource/listDiyResource");
		String operStatus=StringUtil.nullToString(req.getParameter("operStatus"));
		
		List<Resource> pageList = null;
		try {
			Resource resource = new Resource();
			resource.setLoginType(LoginType.LoginType3.getCode());
			pageList=resourceService.getResourceList(resource);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询列表信息出错", e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}
	
	/**
	 * 进入到新增资源
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddDiyResource")
	public ModelAndView intoAddDiyResource(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("diy/diyResource/addDiyResource");
		Resource DiyResource=new Resource();
		DiyResource.setLoginType(LoginType.LoginType3.getCode());
		List<Resource>  diyResourceList=resourceService.getResourceList(DiyResource);
		mv.addObject("diyResourceList", diyResourceList);
		
		String resourceId=StringUtil.nullToString(req.getParameter("resourceId"));
		Resource parantRes=resourceService.getResourceById(resourceId);
		mv.addObject("parantRes", parantRes);
		
		return mv;
	}
	
	/**
	 * 资源添加提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addDiyResourceCommit")
	@ResponseBody
	public Map<String, Object> addDiyResourceCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("status", Boolean.TRUE);
		try {
			HttpSession session = req.getSession();
			User u = (User)session.getAttribute(Constants.SESSION_USER);
			Resource diyResource=getDiyResourceInfo(req,u);
			Resource checkResource=resourceService.getResourceByKey(diyResource.getResourceKey(), LoginType.LoginType3.getCode());
			if(checkResource !=null){
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", checkResource.getResourceKey()+"已经存在，请重新输入");
				return resultMap;
			}
			resourceService.insertResource(diyResource);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增资源失败，请重新添加");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 进入编辑资源页面
	 * 
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/intoEditDiyResource")
	public ModelAndView intoEditDiyResource(HttpServletRequest req, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("diy/diyResource/editDiyResource");
		String resourceId=req.getParameter("resourceId");
		Resource diyResource=resourceService.getResourceById(resourceId);
		
		//查找上级菜单列表
		Resource diyResource1=new Resource();
		diyResource1.setResourceType("0");
		diyResource1.setLoginType(LoginType.LoginType3.getCode());
		List<Resource>  diyResourceList=resourceService.getResourceList(diyResource1);
		
		mv.addObject("diyResourceList", diyResourceList);
		mv.addObject("diyResource", diyResource);
		return mv;
	}
	
	
	/**
	 * 资源编辑 提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editDiyResourceCommit")
	@ResponseBody
	public Map<String, Object> editDiyResourceCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			HttpSession session = req.getSession();
			User u = (User)session.getAttribute(Constants.SESSION_USER);
			Resource diyResource=getDiyResourceInfo(req,u);
			Resource checkDiyResource=resourceService.getResourceByKey(diyResource.getResourceKey(), LoginType.LoginType3.getCode());
			if(checkDiyResource !=null){
				if(!diyResource.getId().equals(checkDiyResource.getId())){
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", checkDiyResource.getResourceKey()+"已经存在，请重新输入");
					return resultMap;
				}
			}
			resourceService.updateResource(diyResource);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑资源失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 对资源表进行封装
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	private Resource getDiyResourceInfo(HttpServletRequest req,User user) throws Exception {
		Resource diyResource=null;
		String resourceId=StringUtil.nullToString(req.getParameter("resourceId"));
		if(!StringUtil.isNullOrEmpty(resourceId)){
			diyResource=resourceService.getResourceById(resourceId);
		}else{
			diyResource=new Resource();
			diyResource.setId(UUID.randomUUID().toString());
			diyResource.setCreateUser(user.getId());
			diyResource.setCreateTime(System.currentTimeMillis());
		}
		diyResource.setId(resourceId);
		diyResource.setResourceName(StringUtil.nullToString(req.getParameter("resourceName")));
		diyResource.setResourceKey(StringUtil.nullToString(req.getParameter("resourceKey")).toUpperCase());
		diyResource.setUrl(StringUtil.nullToString(req.getParameter("url")));
		diyResource.setDescription(StringUtil.nullToString(req.getParameter("description")));
		diyResource.setIcon(StringUtil.nullToString(req.getParameter("icon")));
		diyResource.setSeq(Integer.parseInt(StringUtil.nullToString(req.getParameter("seq"))));
		diyResource.setResourceType(StringUtil.nullToString(req.getParameter("resourceType")));
		diyResource.setLoginType(LoginType.LoginType3.getCode());
		diyResource.setDataStat(StringUtil.nullToString(req.getParameter("dataStat")));
		diyResource.setPid(StringUtil.nullToString(req.getParameter("pid")));
		diyResource.setUpdateUser(user.getId().toString());
		diyResource.setUpdateTime(System.currentTimeMillis());
		return diyResource;
	}
	
	/**
	 * 删除资源 commit
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteDiyResourceCommit")
	@ResponseBody
	public Map<String, Object> deleteDiyResourceCommit(HttpServletRequest req, HttpServletResponse response) {
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
