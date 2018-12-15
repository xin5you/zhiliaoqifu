package com.ebeijia.zl.web.oms.providerChnl.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.billingtype.service.BillingTypeInfService;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.IsOpenEnum;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.providerDefaultRoute;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.web.oms.providerChnl.service.ProviderInfService;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "provider/providerInf")
public class ProviderInfController {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProviderInfFacade providerInfFacade;
	
	@Autowired
	private BillingTypeInfService billingTypeInfService;
	
	@Autowired
	private ProviderInfService providerInfService;
	
	@Autowired
	private CompanyInfFacade companyInfFacade;

	/**
	 * 供应商列表 信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/listProviderInf")
	public ModelAndView listProviderInf(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("provider/providerInf/listProviderInf");
		String operStatus = StringUtil.nullToString(request.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);
		try {
			ProviderInf providerInf = this.getProviderInf(request);
			PageInfo<ProviderInf> pageList = providerInfFacade.getProviderInfPage(startNum, pageSize, providerInf);
			CompanyInf company = new CompanyInf();
			company.setIsOpen(IsOpenEnum.ISOPEN_TRUE.getCode());
			List<CompanyInf> companyList = companyInfFacade.getCompanyInfList(company);
			mv.addObject("pageInfo", pageList);
			mv.addObject("providerInf", providerInf);
			mv.addObject("companyList", companyList);
		} catch (Exception e) {
			logger.error("## 供应商信息列表查询异常", e);
		}
		mv.addObject("operStatus", operStatus);
		return mv;
	}

	/**
	 * 进入添加供应商信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddProviderInf")
	public ModelAndView intoAddProviderInf(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("provider/providerInf/addProviderInf");
		ProviderInf telProviderInf = new ProviderInf();
		telProviderInf.setDefaultRoute(providerDefaultRoute.DefaultRoute0.getCode());
		try {
			List<ProviderInf> list = providerInfFacade.getProviderInfList(telProviderInf);
			if(list.size() == 0){
				mv.addObject("defaultRouteState", "0");
			}else{
				mv.addObject("defaultRouteState", "1");
			}
		} catch (Exception e) {
			logger.error("## 进入添加供应商信息异常", e);
		}
		mv.addObject("defaultRouteList", providerDefaultRoute.values());
		return mv;
	}

	/**
	 * 添加供应商信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/addProviderInfCommit")
	@ResponseBody
	public ModelMap addProviderInfCommit(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		try {
			ProviderInf providerInf = this.getProviderInf(req);
			if(!providerInfFacade.saveProviderInf(providerInf)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "新增失败，请重新添加");
			}
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "新增失败，请重新添加");
			logger.error("## 添加供应商信息异常", e);
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 进入编辑供应商信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoEditProviderInf")
	public ModelAndView intoEditProviderInf(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("provider/providerInf/editProviderInf");
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		try {
			ProviderInf providerInf = providerInfFacade.getProviderInfById(providerId);
			mv.addObject("providerInf", providerInf);
		} catch (Exception e) {
			logger.error("## 通过id查找供应商信息异常", e);
		}
		mv.addObject("defaultRouteList", providerDefaultRoute.values());
		return mv;
	}

	/**
	 * 编辑供应商信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/editProviderInfCommit")
	@ResponseBody
	public ModelMap editProviderInfCommit(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		try {
			if (StringUtil.isNullOrEmpty(providerId)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "编辑失败,供应商id为空");
				logger.error("## 编辑供应商信息异常,供应商providerId:[{}]为空", providerId);
			}
			
			ProviderInf providerInf = this.getProviderInf(req);
			if (!providerInfFacade.updateProviderInf(providerInf)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "编辑失败，请联系管理员");
			}
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "编辑失败，请联系管理员");
			logger.error("## 编辑供应商信息异常", e);
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 供应商信息详情
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "intoViewProviderInf")
	public ModelAndView intoViewProviderInf(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("provider/providerInf/viewProviderInf");
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		try {
			ProviderInf providerInf = providerInfFacade.getProviderInfById(providerId);
			mv.addObject("providerInf", providerInf);
		} catch (Exception e) {
			logger.error("## 查询供应商信息详情异常", e);
		}
		mv.addObject("defaultRouteList", providerDefaultRoute.values());
		return mv;
	}

	/**
	 * 删除供应商信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/deleteProviderInfCommit")
	@ResponseBody
	public ModelMap deleteProviderInfCommit(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		try {
			if (StringUtil.isNullOrEmpty(providerId)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "删除失败,供应商id为空");
				logger.error("## 删除供应商信息异常,供应商providerId:[{}]为空", providerId);
			}
			providerInfFacade.deleteProviderInfById(providerId);
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "删除失败，请联系管理员");
			logger.error("## 删除供应商信息异常", e);
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/providerOpenAccount")
	@ResponseBody
	public ModelMap providerOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		try {
			int i = providerInfService.providerOpenAccount(req);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "开户失败，请重新操作");
			}
		} catch (Exception e) {
			logger.error(" ## 供应商开户出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "供应商开户失败，请重新操作");
			return resultMap;
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/addProviderTransferCommit")
	@ResponseBody
	public ModelMap addProviderTransferCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		try {
			int i = providerInfService.addProviderTransferCommit(req);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "转账失败，请重新操作");
			}
		} catch (Exception e) {
			logger.error(" ## 供应商转账出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "供应商转账失败，请重新操作");
		}
		return resultMap;
	}

	/**
	 * 封装供应商实体
	 */
	public ProviderInf getProviderInf(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String lockVersion = StringUtil.nullToString(req.getParameter("lockVersion"));
		String providerRate = StringUtil.nullToString(req.getParameter("providerRate"));
		
		ProviderInf providerInf = null;
		try {
			if (!StringUtil.isNullOrEmpty(providerId)) {
				providerInf = providerInfFacade.getProviderInfById(providerId);
			} else {
				providerInf = new ProviderInf();
				providerInf.setProviderId(IdUtil.getNextId());
				providerInf.setCreateUser(user.getId());
				providerInf.setCreateTime(System.currentTimeMillis());
				providerInf.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
				providerInf.setLockVersion(0);
			}
		} catch (Exception e) {
			logger.error("## 查询供应商信息失败，providerId--->{}", providerId);
		}
		providerInf.setProviderName(StringUtil.nullToString(req.getParameter("providerName")));
		providerInf.setAppUrl(StringUtil.nullToString(req.getParameter("appUrl")));
		providerInf.setAppSecret(StringUtil.nullToString(req.getParameter("appSecret")));
		providerInf.setAccessToken(StringUtil.nullToString(req.getParameter("accessToken")));
		providerInf.setDefaultRoute(StringUtil.nullToString(req.getParameter("defaultRoute")));
		if (!StringUtil.isNullOrEmpty(providerRate)) {
			providerInf.setProviderRate(new BigDecimal(providerRate));
		}
		if (!StringUtil.isNullOrEmpty(req.getParameter("operSolr")))
			providerInf.setOperSolr(Integer.valueOf(req.getParameter("operSolr")));
		providerInf.setRemarks(StringUtil.nullToString(req.getParameter("remarks")));
		if (!StringUtil.isNullOrEmpty(lockVersion)) {
			providerInf.setLockVersion(Integer.valueOf(lockVersion) + 1);
		}
		providerInf.setUpdateUser(user.getId().toString());
		providerInf.setUpdateTime(System.currentTimeMillis());
		return providerInf;
	}
}