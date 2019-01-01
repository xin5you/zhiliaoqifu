package com.ebeijia.zl.web.oms.company.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.web.oms.company.service.CompanyService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "company")
public class CompanyController {

	Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	private CompanyInfFacade companyInfFacade;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

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
		ModelAndView mv = new ModelAndView("company/listCompany");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		String name = StringUtil.nullToString(req.getParameter("name"));
		String transFlag = StringUtil.nullToString(req.getParameter("transFlag"));
		String contacts = StringUtil.nullToString(req.getParameter("contacts"));
		/*System.out.println("====================================================="+
		req.getServletContext() + "excel/batchRecharge.xlxs");*/
		CompanyInf companyInf = new CompanyInf();//通过封装类将前台查询条件用对象接收
		companyInf.setName(name);
		companyInf.setTransFlag(transFlag);
		companyInf.setContacts(contacts);

		PageInfo<CompanyInf> pageList = null;
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
		ModelAndView mv = new ModelAndView("company/addCompany");

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
		if (IsPlatformEnum.ISOPEN_TRUE.getCode().equals(companyInf.getIsPlatform())) {
			CompanyInf c = companyInfFacade.getCompanyInfByIsPlatform(companyInf.getIsPlatform());
			if (c != null && IsOpenAccountEnum.ISOPEN_TRUE.getCode().equals(c.getIsOpen())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "平台标识已有开户企业，请重新选择");
				return resultMap;
			}
		}
		try {
			if (!companyInfFacade.insertCompanyInf(companyInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增企业信息失败");
			}
		} catch (Exception e) {
			logger.error("## 新增企业信息出错", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增企业信息失败");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "/intoEditCompany")
	public ModelAndView intoEditCompany(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("company/editCompany");
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
		CompanyInf companyInf = getCompanyInf(req);
		if (!companyInfCode.getIsPlatform().equals(companyInf.getIsPlatform()) && !companyInf.getIsPlatform().equals(IsPlatformEnum.ISOPEN_FALSE.getCode())) {
			CompanyInf c = companyInfFacade.getCompanyInfByIsPlatform(companyInf.getIsPlatform());
			if (c != null && IsOpenAccountEnum.ISOPEN_TRUE.getCode().equals(c.getIsOpen())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "平台标识已有开户企业，请重新选择");
				return resultMap;
			}
		}
		try {
			if (!companyInfFacade.updateCompanyInf(companyInf)) {
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
			if (!companyInfFacade.deleteCompanyInf(company)) {
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
			companyInf.setLockVersion(companyInf.getLockVersion() + 1);
		} else {
			companyInf = new CompanyInf();
			companyInf.setCompanyId(IdUtil.getNextId());
			companyInf.setIsOpen(IsOpenAccountEnum.ISOPEN_FALSE.getCode());
			companyInf.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			companyInf.setCreateUser(user.getId());
			companyInf.setCreateTime(System.currentTimeMillis());
			companyInf.setLockVersion(0);
		}
		companyInf.setLawCode(StringUtil.nullToString(req.getParameter("lawCode")));
		companyInf.setName(StringUtil.nullToString(req.getParameter("name")));
		companyInf.setTransFlag(StringUtil.nullToString(req.getParameter("transFlag")));
		companyInf.setAddress(StringUtil.nullToString(req.getParameter("address")));
		companyInf.setRemarks(StringUtil.nullToString(req.getParameter("remarks")));
		companyInf.setPhoneNo(StringUtil.nullToString(req.getParameter("phoneNo")));
		companyInf.setContacts(StringUtil.nullToString(req.getParameter("contacts")));
		companyInf.setIsPlatform(StringUtil.nullToString(req.getParameter("isPlatform")));
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
			}
		} catch (Exception e) {
			logger.error("## 企业开户异常", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("status", "企业开户失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "/intoAddCompanyTransfer")
	public ModelAndView intoAddCompanyTransfer(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("company/addCompanyTransfer");
		String companyId = req.getParameter("companyId");
		CompanyInf company = companyInfFacade.getCompanyInfById(companyId);

		InaccountOrder order = new InaccountOrder();
		order.setTransferCheck(TransferCheckEnum.INACCOUNT_TRUE.getCode());
		if (IsOpenAccountEnum.ISOPEN_FALSE.getCode().equals(company.getIsOpen())) {
			order.setCompanyId(companyId);
		}
		try {
			int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
			PageInfo<InaccountOrder> pageList = inaccountOrderService.getInaccountOrderByOrderPage(startNum, pageSize, order);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询打款订单信息详情异常", e);
		}

		mv.addObject("company", company);
		return mv;
	}

	@RequestMapping(value = "/addCompanyTransferCommit")
	@ResponseBody
	public Map<String, Object> addCompanyTransferCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		try {
			CompanyInf company = companyInfFacade.getCompanyInfById(companyId);
			if (IsPlatformEnum.ISOPEN_TRUE.getCode().equals(company.getIsPlatform())) {
				resultMap = companyService.addCompanyTransferCommit(req);
			} else {
				resultMap = companyService.updateCompanyTransferStat(req);
			}
		} catch (Exception e) {
			logger.error(" ## 平台{}打款至企业异常 ", companyId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "平台打款失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "viewCompanyTransferDetail")
	public ModelAndView viewCompanyTransferDetail(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("company/viewCompanyTransfer");

		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String companyId = StringUtil.nullToString(request.getParameter("companyId"));
		InaccountOrder inaccountOrder = new InaccountOrder();
		inaccountOrder.setOrderId(orderId);
		CompanyInf company = companyInfFacade.getCompanyInfById(companyId);
		if (IsPlatformEnum.ISOPEN_FALSE.getCode().equals(company.getIsPlatform())) {
			inaccountOrder.setCompanyId(company.getCompanyId());
		}
		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
		if (order != null) {
			order.setCheckStat(CheckStatEnum.findByBId(order.getCheckStat()).getName());
			order.setRemitCheck(RemitCheckEnum.findByBId(order.getRemitCheck()).getName());
			order.setInaccountCheck(InaccountCheckEnum.findByBId(order.getInaccountCheck()).getName());
			order.setTransferCheck(TransferCheckEnum.findByBId(order.getTransferCheck()).getName());
			order.setPlatformReceiverCheck(ReceiverEnum.findByBId(order.getPlatformReceiverCheck()).getName());
			order.setCompanyReceiverCheckName(ReceiverEnum.findByBId(order.getCompanyReceiverCheck()).getName());
			order.setRemitAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getRemitAmt().toString())));
			order.setInaccountAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getInaccountAmt().toString())));
		}
		try {
			int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);
			InaccountOrderDetail orderDetail = new InaccountOrderDetail();
			orderDetail.setOrderId(orderId);
			PageInfo<InaccountOrderDetail> pageList = inaccountOrderDetailService.getInaccountOrderDetailByOrderPage(startNum, pageSize, orderDetail);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询企业订单明细信息详情异常", e);
		}
		mv.addObject("order", order);
		mv.addObject("company", company);
		return mv;
	}

	@RequestMapping(value = "/addCompanyInvoiceCommit")
	@ResponseBody
	public Map<String, Object> addCompanyInvoiceCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String orderListId = StringUtil.nullToString(req.getParameter("orderListId"));
		String invoiceInfo = StringUtil.nullToString(req.getParameter("invoiceInfo"));
		try {
			InaccountOrderDetail orderDetail = inaccountOrderDetailService.getById(orderListId);
			if (orderDetail != null) {
				orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_TRUE.getCode());
				orderDetail.setInvoiceInfo(invoiceInfo);
				orderDetail.setUpdateTime(System.currentTimeMillis());
				orderDetail.setUpdateUser(user.getId());
				orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);
				if (!inaccountOrderDetailService.saveOrUpdate(orderDetail)) {
					logger.error(" ## 更新订单{}开票失败 ", orderListId);
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "开票异常，请稍后再试");
				}
			}
		} catch (Exception e) {
			logger.error(" ## 订单{}开票异常 ", orderListId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "开票失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping("/listCompanyAccBal")
	public ModelAndView listCompanyAccBal(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("company/listCompanyAccBal");
		String companyId = StringUtil.nullToString(request.getParameter("companyId"));

		try {
			Map<String, Object> resultMap = commonService.getAccountInfPage(request);
			mv.addObject("pageInfo", resultMap.get("pageInfo"));
		} catch (Exception e) {
			logger.error("## 企业账户列表查询异常", e);
		}
		mv.addObject("companyId", companyId);
		return mv;
	}

}
