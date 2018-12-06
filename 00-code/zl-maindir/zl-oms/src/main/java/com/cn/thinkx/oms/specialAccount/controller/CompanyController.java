package com.cn.thinkx.oms.specialAccount.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.oms.common.util.OmsEnum.C1;
import com.cn.thinkx.oms.common.util.OmsEnum.C2;
import com.cn.thinkx.oms.common.util.OmsEnum.C3;
import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.CompanyBillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyBillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyInfService;
import com.cn.thinkx.oms.sys.model.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "specialAccount/company")
public class CompanyController {

	Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;

	@Autowired
	@Qualifier("companyInfService")
	private CompanyInfService companyInfService;

	@Autowired
	@Qualifier("companyBillingTypeInfService")
	private CompanyBillingTypeInfService companyBillingTypeInfService;

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
		CompanyInf companyInf = this.getCompanyInf(req, null);//通过封装类将前台查询条件用对象接收
	     
		try {
			int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
			pageList = companyInfService.getCompanyInfList(startNum, pageSize, companyInf);//将企业信息对象作为参数传入，实现通过条件查询列表
		} catch (Exception e) {
			logger.error("## 查询企业列表信息出错", e);
		}
		
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("companyInf", companyInf);
		return mv;
	}

	/**
	 * 进入企业信息新增页面
	 * 
	 * @param req
	 * @param response
	 * @return ModelAndView对象
	 */
	@RequestMapping(value = "/intoAddCompany")
	public ModelAndView intoAddCompany(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/company/addCompany");
		List<BillingTypeInf> bList = billingTypeInfService.getBillingTypeInfList(null);
		mv.addObject("bList", bList);
		//将全部企业代码数据传向前台，提供给下拉框，供客户选择
		C1[] cs1 = C1.values();
		mv.addObject("cityCodes1", cs1);
		C2[] cs2 = C2.values();
		mv.addObject("cityCodes2", cs2);
		C3[] cs3 = C3.values();
		mv.addObject("cityCodes3", cs3);
		return mv;
	}

	/**
	 * 企业信息添加提交
	 * 
	 * @param req
	 * @param resp
	 * @return JSON格式的String字符串
	 */
	@RequestMapping(value = "/addCompany")
	@ResponseBody
	public String addCompany(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		String str = null;
		
		try {
			CompanyInf companyInf = this.getCompanyInf(req, user);
			String[] bIds = req.getParameterValues("bId");
			if (companyInf == null) {//通过封装类得到的对象是空，表示企业信息已存在，不能新增
				str = "repeat";
			} else {//不为空就执行新增
				companyInfService.insertCompanyInf(companyInf, bIds);
				str = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "false";
			logger.error("## 新增企业信息出错", e);
		}
		return str;
	}


	/**
	 * 进入企业信息编辑页面
	 * 
	 * @param req
	 * @param response
	 * @return ModelAndView对象
	 * @throws IOException 
	 */
	@RequestMapping(value = "/intoEditCompany")
	public ModelAndView intoEditCompany(HttpServletRequest req, HttpServletResponse response)
			throws Exception {
		ModelAndView mv = new ModelAndView("specialAccount/company/editCompany");
		String cId = req.getParameter("cId");

		CompanyInf companyInf = companyInfService.getCompanyInfById(cId);
		mv.addObject("companyInf", companyInf);

		List<BillingTypeInf> bList = billingTypeInfService.getBillingTypeInfList(null);
		mv.addObject("bList", bList);//将现有全部的专用账户类型传向前台
		
		List<CompanyBillingTypeInf> cbList = companyBillingTypeInfService.getCompanyBillingTypeInfList(cId);
		mv.addObject("cbList", cbList);//将该企业拥有的专用账户类型传向前台
		return mv;
	}

	/**
	 * 企业信息编辑提交
	 * 
	 * @param req
	 * @param response
	 * @return JSON格式的String字符串
	 */
	@RequestMapping(value = "/editCompany")
	@ResponseBody
	public String editCompany(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		String str = null;
		
		try {
			CompanyInf companyInf = this.getCompanyInf(req, user);
			String[] bIds = req.getParameterValues("bId");
			companyInfService.updateCompanyInf(companyInf, bIds);
			str = "success";
		} catch (Exception e) {
			str = "false";
			logger.error("## 修改企业信息出错", e);
		}
		return str;
	}

	/**
	 * 删除企业信息
	 * 
	 * @param req
	 * @param response
	 * @return JSON格式的Map对象
	 */
	@RequestMapping(value = "/deleteCompany")
	@ResponseBody
	public Map<String, Object> deleteCompany(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String cId = req.getParameter("cId");
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		try {
			companyInfService.deleteCompanyInf(cId,user);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 删除企业信息出错", e);
		}
		return resultMap;
	}

	/**
	 * 企业信息对象封装
	 * 
	 * @param req
	 * @param user
	 *        当前用户
	 * @return CompanyInf对象
	 */
	private CompanyInf getCompanyInf(HttpServletRequest req, User user) {
		CompanyInf companyInf = null;
		String cId = StringUtil.nullToString(req.getParameter("cId"));
		if (!StringUtil.isNullOrEmpty(cId)) {//cId如果不是空，表示请求是从更新页面过来的，需要组装一个更新用的对象
			companyInf = companyInfService.getCompanyInfById(cId);
			if (user != null) {
				companyInf.setUpdateUser(user.getId().toString());
			}
			companyInf.setName(StringUtil.nullToString(req.getParameter("name")));
			companyInf.setFlag(StringUtil.nullToString(req.getParameter("flag")));
			companyInf.setLawCode(StringUtil.nullToString(req.getParameter("lawCode")));
			/*companyInf.setType(StringUtil.nullToString(req.getParameter("type")));*/
			companyInf.setAddress(StringUtil.nullToString(req.getParameter("address")));
			companyInf.setRemarks(StringUtil.nullToString(req.getParameter("remarks")));
			companyInf.setPhoneNO(StringUtil.nullToString(req.getParameter("phoneNO")));
			companyInf.setContacts(StringUtil.nullToString(req.getParameter("contacts")));
		} else {//cId如果是空，表示请求是从新增页面，后者查询页面过来的，需要组装一个新增用，或查询用的对象
			companyInf = companyInfService.getCompanyInfByLawCode(StringUtil.nullToString(req.getParameter("lawCode")));
			
			if (companyInf == null) {
				companyInf = new CompanyInf();
			} else {
				return null;//返回null说明企业的LawCode的已经存在，不能新增
			}
			
			if (user != null) {
				companyInf.setCreateUser(user.getId().toString());
			}
			
			companyInf.setComCode(StringUtil.nullToString(req.getParameter("comCode")));
			companyInf.setLawCode(StringUtil.nullToString(req.getParameter("lawCode")));
			companyInf.setName(StringUtil.nullToString(req.getParameter("name")));
			companyInf.setFlag(StringUtil.nullToString(req.getParameter("flag")));
			/*companyInf.setType(StringUtil.nullToString(req.getParameter("type")));*/
			companyInf.setAddress(StringUtil.nullToString(req.getParameter("address")));
			companyInf.setRemarks(StringUtil.nullToString(req.getParameter("remarks")));
			companyInf.setPhoneNO(StringUtil.nullToString(req.getParameter("phoneNO")));
			companyInf.setContacts(StringUtil.nullToString(req.getParameter("contacts")));
		}
		return companyInf;
	}
	
	
}
