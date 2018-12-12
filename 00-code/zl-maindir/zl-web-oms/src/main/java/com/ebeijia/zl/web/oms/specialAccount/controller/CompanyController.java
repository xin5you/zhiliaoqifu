package com.ebeijia.zl.web.oms.specialAccount.controller;

import java.io.IOException;
import java.util.HashMap;
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

import com.alibaba.dubbo.config.annotation.Reference;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.web.oms.specialAccount.service.CompanyService;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "specialAccount/company")
public class CompanyController {

	Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Reference(check=false)
	private CompanyInfFacade companyInfFacade;
	
	@Autowired
	private CompanyService companyService;
	
	/**
	 * 企业信息列表查询
	 * 
	 * @param req
	 * @param response
	 * @return ModelAndView对象
	 * @throws IOException 
	 */
	@RequestMapping(value = "/listCompany")
	public ModelAndView listCompany(HttpServletRequest req, HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView("specialAccount/company/listCompany");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		PageInfo<CompanyInf> pageList = null;
		CompanyInf companyInf = this.getCompanyInf(req);//通过封装类将前台查询条件用对象接收
	     
		try {
			int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
			pageList = companyInfFacade.getCompanyInfList(startNum, pageSize, companyInf);//将企业信息对象作为参数传入，实现通过条件查询列表
		} catch (Exception e) {
			logger.error("## 查询企业列表信息出错", e);
		}
		
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("companyInf", companyInf);
		return mv;
	}

	@RequestMapping(value = "/intoAddCompany")
	public ModelAndView intoAddCompany(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/company/addCompany");
		return mv;
	}

	@RequestMapping(value = "/addCompany")
	@ResponseBody
	public Map<String, Object> addCompany(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		String lawCode = StringUtil.nullToString(req.getParameter("lawCode"));
		CompanyInf company = companyInfFacade.getCompanyInfByLawCode(lawCode);
		if (!StringUtil.isNullOrEmpty(company)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "社会信用代码已存在，请重新输入");
			return resultMap;
		}
		CompanyInf companyInf = getCompanyInf(req);
		try {
			if (companyInfFacade.insertCompanyInf(companyInf)) {
				
			} else {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增企业信息失败");
			}
		} catch (Exception e) {
			logger.error("## 新增企业信息出错", e);
		}
		return resultMap;
	}

	@RequestMapping(value = "/intoEditCompany")
	public ModelAndView intoEditCompany(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/company/editCompany");
		String companyId = req.getParameter("companyId");
		CompanyInf companyInf = companyInfFacade.getCompanyInfById(companyId);
		mv.addObject("companyInf", companyInf);
		return mv;
	}

	@RequestMapping(value = "/editCompany")
	@ResponseBody
	public Map<String, Object> editCompany(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		String lawCode = StringUtil.nullToString(req.getParameter("lawCode"));
		CompanyInf companyInfCode = companyInfFacade.getCompanyInfById(companyId);
		if (!companyInfCode.getLawCode().equals(lawCode)) {
			CompanyInf company = companyInfFacade.getCompanyInfByLawCode(lawCode);
			if (!StringUtil.isNullOrEmpty(company)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "社会信用代码已存在，请重新输入");
				return resultMap;
			}
		}
		
		try {
			CompanyInf companyInf = getCompanyInf(req);
			if (companyInfFacade.updateCompanyInf(companyInf)) {
				
			} else {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "修改企业信息失败");
			}
		} catch (Exception e) {
			logger.error("## 修改企业信息出错", e);
		}
		return resultMap;
	}

	@RequestMapping(value = "/deleteCompany")
	@ResponseBody
	public Map<String, Object> deleteCompany(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		CompanyInf company = new CompanyInf();
		company.setCompanyId(companyId);;
		company.setUpdateTime(System.currentTimeMillis());
		company.setUpdateUser(user.getId());
		try {
			if (companyInfFacade.deleteCompanyInf(company)) {
				
			} else {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除企业信息失败");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 删除企业信息出错", e);
		}
		return resultMap;
	}

	private CompanyInf getCompanyInf(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		CompanyInf companyInf = null;
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		if (!StringUtil.isNullOrEmpty(companyId)) {
			companyInf = companyInfFacade.getCompanyInfById(companyId);
		} else {
			companyInf = new CompanyInf();
			companyInf.setCompanyId(UUID.randomUUID().toString());
			companyInf.setCreateUser(user.getId());
			companyInf.setCreateTime(System.currentTimeMillis());
		}
		companyInf.setLawCode(StringUtil.nullToString(req.getParameter("lawCode")));
		companyInf.setName(StringUtil.nullToString(req.getParameter("name")));
		companyInf.setTransFlag(StringUtil.nullToString(req.getParameter("transFlag")));
		companyInf.setAddress(StringUtil.nullToString(req.getParameter("address")));
		companyInf.setRemarks(StringUtil.nullToString(req.getParameter("remarks")));
		companyInf.setPhoneNo(StringUtil.nullToString(req.getParameter("phoneNo")));
		companyInf.setContacts(StringUtil.nullToString(req.getParameter("contacts")));
		companyInf.setUpdateUser(user.getId());
		companyInf.setUpdateTime(System.currentTimeMillis());
		return companyInf;
	}
	
	@RequestMapping(value = "/openAccountCompany")
	@ResponseBody
	public Map<String, Object> openAccountCompany(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			int i = companyService.openAccountCompany(req);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("status", "企业开户失败，请稍后再试");
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("## 新增企业信息出错", e);
		}
		return resultMap;
	}
	
}
