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

import com.ebeijia.zl.basics.system.domain.Resource;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.basics.system.service.ResourceService;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.LoginType;
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
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		
		List<Resource> pageList = null;
		try {
			Resource resource = new Resource();
			resource.setLoginType(LoginType.LoginType3.getCode());
			pageList = resourceService.getResourceList(resource);
		} catch (Exception e) {
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
		
		Resource DiyResource = new Resource();
		DiyResource.setLoginType(LoginType.LoginType3.getCode());
		List<Resource>  diyResourceList = resourceService.getResourceList(DiyResource);
		
		String resourceId = StringUtil.nullToString(req.getParameter("resourceId"));
		Resource parantRes = resourceService.getById(resourceId);
		
		mv.addObject("diyResourceList", diyResourceList);
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
			Resource resource = getDiyResourceInfo(req);
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
		
		String resourceId = req.getParameter("resourceId");
		Resource diyResource = resourceService.getById(resourceId);
		
		//查找上级菜单列表
		Resource diyResource1 = new Resource();
		diyResource1.setResourceType("0");
		diyResource1.setLoginType(LoginType.LoginType3.getCode());
		List<Resource>  diyResourceList = resourceService.getResourceList(diyResource1);
		
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
			Resource resource = getDiyResourceInfo(req);
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
	private Resource getDiyResourceInfo(HttpServletRequest req) throws Exception {
		HttpSession session = req.getSession();
		User u = (User)session.getAttribute(Constants.SESSION_USER);
		
		String resourceId = StringUtil.nullToString(req.getParameter("resourceId"));
		String seq = StringUtil.nullToString(req.getParameter("seq"));
		
		
		Resource diyResource = null;
		if(!StringUtil.isNullOrEmpty(resourceId)){
			diyResource = resourceService.getById(resourceId);
		}else{
			diyResource = new Resource();
			diyResource.setId(IdUtil.getNextId());
			diyResource.setCreateUser(u.getId());
			diyResource.setCreateTime(System.currentTimeMillis());
			diyResource.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			diyResource.setLockVersion(0);
		}
		diyResource.setResourceName(StringUtil.nullToString(req.getParameter("resourceName")));
		diyResource.setResourceKey(StringUtil.nullToString(req.getParameter("resourceKey")).toUpperCase());
		diyResource.setUrl(StringUtil.nullToString(req.getParameter("url")));
		diyResource.setDescription(StringUtil.nullToString(req.getParameter("description")));
		diyResource.setIcon(StringUtil.nullToString(req.getParameter("icon")));
		if (!StringUtil.isNullOrEmpty(seq)) {
			diyResource.setSeq(Integer.parseInt(seq));
		}
		diyResource.setResourceType(StringUtil.nullToString(req.getParameter("resourceType")));
		diyResource.setLoginType(LoginType.LoginType3.getCode());
		diyResource.setDataStat(StringUtil.nullToString(req.getParameter("dataStat")));
		diyResource.setPid(StringUtil.nullToString(req.getParameter("pid")));
		diyResource.setUpdateUser(u.getId().toString());
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
			if (!resourceService.removeById(resourceId)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除资源失败，请重新操作");
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除资源失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
}
