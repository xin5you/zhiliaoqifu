package com.ebeijia.zl.web.oms.billingtype.controller;

import java.math.BigDecimal;
import java.util.HashMap;
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

import com.ebeijia.zl.basics.billingtype.domain.BillingType;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeService;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "billingType")
public class BillingTypeController {
	Logger logger = LoggerFactory.getLogger(BillingTypeController.class);
	
	@Autowired
	private BillingTypeService billingTypeInfService;

	/**
	 * 专用账户类型列表查询
	 * 
	 * @param req
	 * @param response
	 * @return ModelAndView对象
	 */
	@RequestMapping(value = "/listBillingType")
	public ModelAndView listBillingType(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("billingType/listBillingType");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		PageInfo<BillingType> pageList = null;
		BillingType billingTypeInf = new BillingType();
		billingTypeInf.setBName(StringUtil.nullToString(req.getParameter("bName")));//创建专用账户类型对象，并设置name属性
		
		try {
			int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
			pageList = billingTypeInfService.getBillingTypeInfListPage(startNum, pageSize, billingTypeInf);//将创建的专用账户类型对象传入查询专用账户类型列表的参数中，实现通过专用账户类型名称来模糊查询列表的功能，name为空则为查询所有
		
		} catch (Exception e) {
			logger.error("## 查询专用账户类型列表信息出错", e);
		}
		
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("billingTypeInf", billingTypeInf);
		return mv;
	}
	
	@RequestMapping(value = "/intoAddBillingType")
	public ModelAndView intoAddBillingType(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("billingType/addBillingType");
		mv.addObject("billingTypeCodeList", SpecAccountTypeEnum.values());
		return mv;
	}
	
	@RequestMapping(value = "/addBillingType")
	@ResponseBody
	public Map<String, Object> addBillingType(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String bName = StringUtil.nullToString(req.getParameter("bName"));
		BillingType billingType = billingTypeInfService.getBillingTypeInfByName(bName);
		if (!StringUtil.isNullOrEmpty(billingType)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "账户类型已存在，请重新输入");
			return resultMap;
		}
		
		BillingType billingTypeInfo = getBillingTypeInf(req);
		int i = billingTypeInfService.insertBillingTypeInf(billingTypeInfo);
		resultMap.put("status", Boolean.TRUE);
		if (i < 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增账户类型失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/intoEditBillingType")
	public ModelAndView intoEditBillingType(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("billingType/editBillingType");
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		BillingType billingType = billingTypeInfService.getBillingTypeInfById(bId);
		mv.addObject("billingType", billingType);
		mv.addObject("billingTypeCodeList", SpecAccountTypeEnum.values());
		return mv;
	}
	
	@RequestMapping(value = "/editBillingType")
	@ResponseBody
	public Map<String, Object> editBillingType(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		String bName = StringUtil.nullToString(req.getParameter("bName"));
		BillingType billingTypeName = billingTypeInfService.getBillingTypeInfById(bId);
		if (!billingTypeName.getBName().equals(bName)) {
			BillingType billingType = billingTypeInfService.getBillingTypeInfByName(bName);
			if (!StringUtil.isNullOrEmpty(billingType)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "账户类型已存在，请重新输入");
				return resultMap;
			}
		}
		
		resultMap.put("status", Boolean.TRUE);
		BillingType billingTypeInfo = getBillingTypeInf(req);
		int i = billingTypeInfService.updateBillingTypeInf(billingTypeInfo);
		if (i < 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑账户类型失败");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/deleteBillingTypeCommit")
	@ResponseBody
	public Map<String, Object> deleteUserCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		BillingType billingType = new BillingType();
		billingType.setBId(bId);
		billingType.setUpdateTime(System.currentTimeMillis());
		billingType.setUpdateUser(user.getId());
		
		int i = billingTypeInfService.deleteBillingTypeInf(billingType);
		if (i < 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除账户类型失败，请重新操作");
		}
		return resultMap;
	}
	
	private BillingType getBillingTypeInf(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		String bName = StringUtil.nullToString(req.getParameter("bName"));
		String code = StringUtil.nullToString(req.getParameter("code"));
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));
		String loseFee = StringUtil.nullToString(req.getParameter("loseFee"));
		String buyFee = StringUtil.nullToString(req.getParameter("buyFee"));
		
		BillingType billingType = null;
		if (!StringUtil.isNullOrEmpty(bId)) {
			billingType = billingTypeInfService.getBillingTypeInfById(bId);
		} else {
			billingType = new BillingType();
			billingType.setBId(IdUtil.getNextId());
			billingType.setCreateUser(user.getId());
			billingType.setCreateTime(System.currentTimeMillis());
		}
		billingType.setBName(bName);
		billingType.setCode(code);
		billingType.setRemarks(remarks);
		billingType.setLoseFee(new BigDecimal(NumberUtils.RMBYuanToCent(loseFee)));
		billingType.setBuyFee(new BigDecimal(NumberUtils.RMBYuanToCent(buyFee)));
		billingType.setUpdateUser(user.getId());
		billingType.setUpdateTime(System.currentTimeMillis());
		return billingType;
	}
	
}
