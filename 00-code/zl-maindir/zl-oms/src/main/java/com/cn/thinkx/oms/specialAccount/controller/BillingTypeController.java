package com.cn.thinkx.oms.specialAccount.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.GatewayInfService;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "specialAccount/billingType")
public class BillingTypeController {

	Logger logger = LoggerFactory.getLogger(BillingTypeController.class);

	@Autowired
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;

	@Autowired
	@Qualifier("gatewayInfService")
	private GatewayInfService gatewayInfService;

	/**
	 * 专用账户类型列表查询
	 * 
	 * @param req
	 * @param response
	 * @return ModelAndView对象
	 */
	@RequestMapping(value = "/listBillingType")
	public ModelAndView listBillingType(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/billingType/listBillingType");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		PageInfo<BillingTypeInf> pageList = null;
		BillingTypeInf billingTypeInf = new BillingTypeInf();
		billingTypeInf.setName(StringUtil.nullToString(req.getParameter("name")));//创建专用账户类型对象，并设置name属性
		
		try {
			int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
			pageList = billingTypeInfService.getBillingTypeInfList(startNum, pageSize, billingTypeInf);//将创建的专用账户类型对象传入查询专用账户类型列表的参数中，实现通过专用账户类型名称来模糊查询列表的功能，name为空则为查询所有
		
		} catch (Exception e) {
			logger.error("## 查询专用账户类型列表信息出错", e);
		}
		
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("billingTypeInf", billingTypeInf);
		return mv;
	}
}
