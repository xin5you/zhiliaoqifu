/*package com.ebeijia.zl.web.oms.basedict.controller;

import java.math.BigDecimal;
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

import com.ebeijia.zl.basics.billingtype.domain.BaseDict;
import com.ebeijia.zl.basics.billingtype.domain.BillingTypeInf;
import com.ebeijia.zl.basics.billingtype.service.BaseDictService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "baseDict")
public class BaseDictController {
	Logger logger = LoggerFactory.getLogger(BaseDictController.class);
	
	@Autowired
	private BaseDictService baseDictService;

	@RequestMapping(value = "/listBaseDict")
	public ModelAndView listBillingType(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("baseDict/listBaseDict");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		PageInfo<BaseDict> pageList = null;
		BaseDict baseDict = new BaseDict();
		baseDict.setbName(StringUtil.nullToString(req.getParameter("bName")));
		
		try {
			int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
			pageList = billingTypeInfService.getBillingTypeInfList(startNum, pageSize, billingTypeInf);
		
		} catch (Exception e) {
			logger.error("## 查询专用账户类型列表信息出错", e);
		}
		
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("billingTypeInf", billingTypeInf);
		return mv;
	}
	
	@RequestMapping(value = "/intoEditBillingType")
	public ModelAndView intoEditBillingType(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("billingType/editBillingType");
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		BillingTypeInf billingType = billingTypeInfService.getBillingTypeInfById(bId);
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
		BillingTypeInf billingTypeName = billingTypeInfService.getBillingTypeInfById(bId);
		if (!billingTypeName.getbName().equals(bName)) {
			BillingTypeInf billingType = billingTypeInfService.getBillingTypeInfByName(bName);
			if (!StringUtil.isNullOrEmpty(billingType)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "账户类型已存在，请重新输入");
				return resultMap;
			}
		}
		
		resultMap.put("status", Boolean.TRUE);
		BillingTypeInf billingTypeInfo = getBillingTypeInf(req);
		int i = billingTypeInfService.updateBillingTypeInf(billingTypeInfo);
		if (i < 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑账户类型失败");
		}
		return resultMap;
	}
	
	private BillingTypeInf getBillingTypeInf(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		String bName = StringUtil.nullToString(req.getParameter("bName"));
		String code = StringUtil.nullToString(req.getParameter("code"));
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));
		String loseFee = StringUtil.nullToString(req.getParameter("loseFee"));
		String buyFee = StringUtil.nullToString(req.getParameter("buyFee"));
		
		BillingTypeInf billingType = null;
		if (!StringUtil.isNullOrEmpty(bId)) {
			billingType = billingTypeInfService.getBillingTypeInfById(bId);
		} else {
			billingType = new BillingTypeInf();
			billingType.setbId(UUID.randomUUID().toString());
			billingType.setCreateUser(user.getId());
			billingType.setCreateTime(System.currentTimeMillis());
		}
		billingType.setbName(bName);
		billingType.setCode(code);
		billingType.setRemarks(remarks);
		billingType.setLoseFee(new BigDecimal(NumberUtils.RMBYuanToCent(loseFee)));
		billingType.setBuyFee(new BigDecimal(NumberUtils.RMBYuanToCent(buyFee)));
		billingType.setUpdateUser(user.getId());
		billingType.setUpdateTime(System.currentTimeMillis());
		return billingType;
	}
	
}
*/