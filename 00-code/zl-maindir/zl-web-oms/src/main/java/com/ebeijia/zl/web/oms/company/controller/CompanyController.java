package com.ebeijia.zl.web.oms.company.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.basics.billingtype.service.BillingTypeService;
import com.ebeijia.zl.common.core.domain.BillingType;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyBillingTypeInf;
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

	@Autowired
	private BillingTypeService billingTypeInfService;

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
		CompanyInf companyInf = new CompanyInf();//通过封装类将前台查询条件用对象接收
		companyInf.setName(name);
		/*companyInf.setTransFlag(transFlag);*/
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

		CompanyInf companyInf = getCompanyInf(req);
		try {
			resultMap = companyService.addCompanyInf(companyInf);
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

		CompanyInf companyInf = getCompanyInf(req);
		try {
			resultMap = companyService.editCompanyInf(companyInf);
		} catch (Exception e) {
			logger.error("## 编辑企业信息异常", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业信息失败");
			return resultMap;
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

	/**
	 * 企业基本信息封装方法
	 * @param req
	 * @return
	 */
	private CompanyInf getCompanyInf(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		CompanyInf companyInf = null;
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		if (!StringUtil.isNullOrEmpty(companyId)) {
			companyInf = companyInfFacade.getCompanyInfById(companyId);
			//companyInf.setLockVersion(companyInf.getLockVersion() + 1);
		} else {
			companyInf = new CompanyInf();
			companyInf.setCompanyId(IdUtil.getNextId());
			companyInf.setTransFlag("0");
			companyInf.setIsOpen(IsOpenAccountEnum.ISOPEN_FALSE.getCode());
			companyInf.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			companyInf.setCreateUser(user.getId());
			companyInf.setCreateTime(System.currentTimeMillis());
			companyInf.setLockVersion(0);
		}
		companyInf.setLawCode(StringUtil.nullToString(req.getParameter("lawCode")));
		companyInf.setName(StringUtil.nullToString(req.getParameter("name")));
		//companyInf.setTransFlag(StringUtil.nullToString(req.getParameter("transFlag")));
		companyInf.setAddress(StringUtil.nullToString(req.getParameter("address")));
		companyInf.setRemarks(StringUtil.nullToString(req.getParameter("remarks")));
		companyInf.setPhoneNo(StringUtil.nullToString(req.getParameter("phoneNo")));
		companyInf.setContacts(StringUtil.nullToString(req.getParameter("contacts")));
		String isPlatForm = StringUtil.nullToString(req.getParameter("isPlatform"));
		if (!StringUtil.isNullOrEmpty(isPlatForm)) {
			companyInf.setIsPlatform(isPlatForm);
		}
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
		if (IsPlatformEnum.IsPlatformEnum_0.getCode().equals(company.getIsPlatform())) {
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
			if (IsPlatformEnum.IsPlatformEnum_1.getCode().equals(company.getIsPlatform())) {
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
		if (IsPlatformEnum.IsPlatformEnum_0.getCode().equals(company.getIsPlatform())) {
			inaccountOrder.setCompanyId(company.getCompanyId());
		}
		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
		if (order != null) {
			order.setCheckStatName(CheckStatEnum.findByBId(order.getCheckStat()).getName());
			order.setRemitCheckName(RemitCheckEnum.findByBId(order.getRemitCheck()).getName());
			order.setInaccountCheckName(InaccountCheckEnum.findByBId(order.getInaccountCheck()).getName());
			order.setTransferCheckName(TransferCheckEnum.findByBId(order.getTransferCheck()).getName());
			order.setPlatformReceiverCheckName(ReceiverEnum.findByBId(order.getPlatformReceiverCheck()).getName());
			order.setCompanyReceiverCheckName(ReceiverEnum.findByBId(order.getCompanyReceiverCheck()).getName());
			order.setRemitAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getRemitAmt().toString())));
			order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getInaccountSumAmt().toString())));
			order.setPlatformInSumAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getPlatformInSumAmt().toString())));
			order.setCompanyInSumAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getCompanyInSumAmt().toString())));
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

	/**
	 * 查询企业专项余额列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/listCompanyAccBal")
	public ModelAndView listCompanyAccBal(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("company/listCompanyAccBal");
		String companyId = StringUtil.nullToString(request.getParameter("companyId"));

		try {
			Map<String, Object> resultMap = commonService.getAccountInfPage(request);
            if (String.valueOf(resultMap.get("status").toString()).equals("true")) {
                mv.addObject("pageInfo", resultMap.get("pageInfo"));
            } else {
                logger.error("## 远程调用查询账户余额失败，msg--->{}", resultMap.get("msg"));
            }
		} catch (Exception e) {
			logger.error("## 企业账户列表查询异常", e);
		}
		mv.addObject("companyId", companyId);
		return mv;
	}

	/**
	 * 添加企业专项费率信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/listCompanyFee")
	public ModelAndView listCompanyFee(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("company/addCompanyFee");
		String companyId = StringUtil.nullToString(request.getParameter("companyId"));
		String bName = StringUtil.nullToString(request.getParameter("bName"));
		CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
		cbt.setCompanyId(companyId);
		cbt.setBName(bName);
		PageInfo<CompanyBillingTypeInf> pageList = null;
		List<BillingType> billingTypeList = new ArrayList<>();
		try {
			int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);
			pageList = companyInfFacade.getCompanyBillingTypeInfPage(startNum, pageSize, cbt);
			List<BillingType> bList = billingTypeInfService.getBillingTypeInfList(new BillingType());
			billingTypeList = bList.stream().filter(t -> !SpecAccountTypeEnum.A01.getbId().equals(t.getBId())).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("## 企业专项费率信息列表查询异常", e);
		}
		mv.addObject("companyId", companyId);
		mv.addObject("pageInfo", pageList);
		mv.addObject("billingTypeList", billingTypeList);
		mv.addObject("companyBillingTypeInf", cbt);
		return mv;
	}

	/**
	 * 根据主键查询企业专项类型信息
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/getCompanyFee")
	@ResponseBody
	public CompanyBillingTypeInf getCompanyFee(HttpServletRequest req, HttpServletResponse resp) {
		String id = StringUtil.nullToString(req.getParameter("companyBillingId"));
		CompanyBillingTypeInf cbt = companyInfFacade.getCompanyBillingTypeInfById(id);
		return cbt;
	}

	/**
	 * 添加企业专项类型信息
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/addCompanyFee")
	@ResponseBody
	public Map<String, Object> addCompanyFee(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		CompanyBillingTypeInf cbt = new CompanyBillingTypeInf();
		cbt.setCompanyId(companyId);
		cbt.setBId(bId);
		CompanyBillingTypeInf companyBType = companyInfFacade.getCompanyBillingTypeInfByBIdAndCompanyId(cbt);
		if (!StringUtil.isNullOrEmpty(companyBType)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "企业专项类型费率信息已存在，请重新输入");
			return resultMap;
		}
		CompanyBillingTypeInf companyBillingTypeInf = getCompanyBillingTypeInf(req);
		try {
			if (!companyInfFacade.insertCompanyBillingTypeInf(companyBillingTypeInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增企业专项类型费率信息失败");
			}
		} catch (Exception e) {
			logger.error("## 新增企业专项类型费率信息出错", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增企业专项类型费率信息失败");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 编辑企业专项类型信息
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/editCompanyFee")
	@ResponseBody
	public Map<String, Object> editCompanyFee(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		CompanyBillingTypeInf companyBillingTypeInf = getCompanyBillingTypeInf(req);
		try {
			if (!companyInfFacade.updateCompanyBillingTypeInf(companyBillingTypeInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑企业专项类型费率信息失败");
			}
		} catch (Exception e) {
			logger.error("## 编辑企业专项类型费率信息出错", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业专项类型费率信息失败");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 删除企业专项类型信息
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/deleteCompanyFee")
	@ResponseBody
	public Map<String, Object> deleteCompanyFee(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		String id = StringUtil.nullToString(req.getParameter("companyBillingId"));

		try {
			if (!companyInfFacade.deleteCompanyBillingTypeInf(id)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除企业专项类型费率信息失败");
			}
		} catch (Exception e) {
			logger.error("## 删除企业专项类型费率信息出错", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除企业专项类型费率信息失败");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 编辑企业收款金额
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/editCompanyInAmtCommit")
	@ResponseBody
	public Map<String, Object> editCompanyInAmtCommit(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			resultMap = companyService.editCompanyInAmtCommit(req);
		} catch (Exception e) {
			logger.error("## 编辑企业收款金额出错", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑企业收款金额失败");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 企业专项类型信息封装类
	 * @param req
	 * @return
	 */
	private CompanyBillingTypeInf getCompanyBillingTypeInf(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		CompanyBillingTypeInf cbt = null;
		String id = StringUtil.nullToString(req.getParameter("companyBillingTypeId"));
		String companyId = StringUtil.nullToString(req.getParameter("companyId"));
		String bId = StringUtil.nullToString(req.getParameter("bId"));
		String fee = StringUtil.nullToString(req.getParameter("fee"));
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));
		if (!StringUtil.isNullOrEmpty(id)) {
			cbt = companyInfFacade.getCompanyBillingTypeInfById(id);
			cbt.setLockVersion(cbt.getLockVersion());
		} else {
			cbt = new CompanyBillingTypeInf();
			cbt.setId(IdUtil.getNextId());
			cbt.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			cbt.setCreateUser(user.getId());
			cbt.setCreateTime(System.currentTimeMillis());
			cbt.setLockVersion(0);
		}
		cbt.setCompanyId(companyId);
		if (!StringUtil.isNullOrEmpty(bId)) {
			cbt.setBId(bId);
		}
		cbt.setFee(fee);
		cbt.setRemarks(remarks);
		cbt.setUpdateUser(user.getId());
		cbt.setUpdateTime(System.currentTimeMillis());
		return cbt;
	}

    /**
     * 查询企业专项余额明细
     * @param req
     * @param response
     * @return
     * @throws IOException
     */
	@RequestMapping(value = "/listCompanyAccBalDetail")
	public ModelAndView listCompanyAccBalDetaillistCompanyAccBal(HttpServletRequest req, HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView("company/listCompanyAccBalDetail");
		String companyId = req.getParameter("companyId");
		try {
            Map<String, Object> resultMap = commonService.getAccountLogInfPage(req);
            if (String.valueOf(resultMap.get("status").toString()).equals("true")) {
                mv.addObject("pageInfo", resultMap.get("pageInfo"));
            } else {
                logger.error("## 远程调用查询账户余额明细失败，msg--->{}", resultMap.get("msg"));
            }
		} catch (Exception e) {
			logger.error("## 查询企业列表信息出错", e);
		}
        mv.addObject("companyId", companyId);
		return mv;
	}


}
