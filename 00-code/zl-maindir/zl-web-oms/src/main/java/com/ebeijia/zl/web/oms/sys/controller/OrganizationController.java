package com.ebeijia.zl.web.oms.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.web.oms.sys.model.Organization;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.sys.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.StringUtil;

@Controller
@RequestMapping(value = "sys/organization")
public class OrganizationController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrganizationService organizationService;

	
	@RequestMapping(value = "/listOrganization")
	public ModelAndView listorganization(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/organization/listOrganization");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		
		List<Organization> pageList = null;
		try {
			pageList=organizationService.getOrganizationList(new Organization());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询列表信息出错", e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}
	
	
	@RequestMapping(value = "/intoAddOrganization")
	public ModelAndView intoAddOrganization(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("sys/organization/addOrganization");
		Organization entity=new Organization();
		
		List<Organization>  entityList=organizationService.getOrganizationList(entity);
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
			HttpSession session = req.getSession();
			User u = (User)session.getAttribute(Constants.SESSION_USER);
			Organization organization=getOrganizationInfo(req);
			organization.setCreateUser(u.getId());
			organization.setUpdateUser(u.getId());
			organizationService.save(organization);
		} catch (Exception e) {
			e.printStackTrace();
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
		String organId=req.getParameter("organId");
		Organization organ=organizationService.getById(organId);
		
		//查找上级菜单列表
		Organization organ1=new Organization();
		List<Organization>  entityList=organizationService.getOrganizationList(organ1);
		
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
		HttpSession session = req.getSession();
		User u = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			Organization organ=getOrganizationInfo(req);
			organ.setUpdateUser(u.getId());
			organizationService.updateById(organ);
		} catch (Exception e) {
			e.printStackTrace();
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
		Organization organ=null;
		String organId=StringUtil.nullToString(req.getParameter("organId"));
		if(!StringUtil.isNullOrEmpty(organId)){
			organ=organizationService.getById(organId);
		}else{
			organ=new Organization();
			organ.setId(UUID.randomUUID().toString());
			organ.setCreateTime(System.currentTimeMillis());
		}
	
		organ.setName(StringUtil.nullToString(req.getParameter("name")));
		organ.setCode(StringUtil.nullToString(req.getParameter("code")));
		organ.setAddress(StringUtil.nullToString(req.getParameter("address")));
		organ.setIcon(StringUtil.nullToString(req.getParameter("icon")));
		organ.setSeq(Integer.parseInt(req.getParameter("seq")));
		organ.setPid(StringUtil.nullToString(req.getParameter("pid")));
		organ.setUpdateTime(System.currentTimeMillis());
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
		String organId=req.getParameter("organId");
		try {
			organizationService.removeById(organId);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除部门失败，请重新操作");
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultMap;
	}
	
}
