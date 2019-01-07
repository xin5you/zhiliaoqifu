package com.ebeijia.zl.web.oms.providerChnl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.providerDefaultRoute;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.web.oms.providerChnl.service.ProviderInfService;
import com.github.pagehelper.PageInfo;

import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;

@Controller
@RequestMapping(value = "provider/providerInf")
public class ProviderInfController {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProviderInfFacade providerInfFacade;
	
	@Autowired
	private ProviderInfService providerInfService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CompanyInfFacade companyInfFacade;

	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

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
			company.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
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
			/*ProviderInf provider = providerInfFacade.getProviderInfBylawCode(providerInf.getLawCode());
			if (provider != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "供应商代码已存在，请重新输入");
				return resultMap;
			}*/
			if(!providerInfFacade.saveProviderInf(providerInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "新增供应商失败，请稍后再试");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统异常，请稍后再试");
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
	public Map<String, Object> editProviderInfCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		try {
			if (StringUtil.isNullOrEmpty(providerId)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑失败,供应商id为空");
				logger.error("## 编辑供应商信息异常,供应商providerId:[{}]为空", providerId);
			}
			
			ProviderInf providerInf = this.getProviderInf(req);
			/*ProviderInf provider = providerInfFacade.getProviderInfBylawCode(providerInf.getLawCode());
			if (provider != null && !provider.getProviderId().equals(provider.getProviderId())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "供应商代码已存在，请重新输入");
				return resultMap;
			}*/
			if (!providerInfFacade.updateProviderInf(providerInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑失败，请联系管理员");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑失败，请联系管理员");
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
	public Map<String, Object> deleteProviderInfCommit(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		try {
			if (StringUtil.isNullOrEmpty(providerId)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除失败,供应商id为空");
				logger.error("## 删除供应商信息异常,供应商providerId:[{}]为空", providerId);
			}
			providerInfFacade.deleteProviderInfById(providerId);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除失败，请联系管理员");
			logger.error("## 删除供应商信息异常", e);
		}
		return resultMap;
	}

	/**
	 * 供应商开户
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/providerOpenAccount")
	@ResponseBody
	public Map<String, Object> providerOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
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

	/**
	 * 供应商上账
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "intoAddProviderTransfer")
	public ModelAndView intoAddProviderTransfer(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("provider/providerInf/addProviderTransfer");

		String providerId = StringUtil.nullToString(request.getParameter("providerId"));
		InaccountOrder order = new InaccountOrder();
		order.setProviderId(providerId);
		order.setOrderType(UserType.TYPE300.getCode());
		try {
			int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);
			PageInfo<InaccountOrder> pageList = inaccountOrderService.getInaccountOrderByOrderPage(startNum, pageSize, order);
			ProviderInf provider = providerInfFacade.getProviderInfById(providerId);
			mv.addObject("pageInfo", pageList);
			mv.addObject("providerRate", provider.getProviderRate());
		} catch (Exception e) {
			logger.error("## 查询供应商上账信息详情异常", e);
		}
		mv.addObject("providerId", providerId);
		return mv;
	}

	/**
	 * 供应商上账订单添加
	 * @param req
	 * @param response
	 * @param evidenceUrlFile
	 * @return
	 */
	@RequestMapping(value = "/addProviderTransfer")
	@ResponseBody
	public Map<String, Object> addProviderTransfer(HttpServletRequest req, HttpServletResponse response,
										@RequestParam(value = "evidenceUrlFile", required = false)MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String companyCode = StringUtil.nullToString(req.getParameter("companyCode"));
		String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
		String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));
		try {
			ProviderInf provider = providerInfFacade.getProviderInfById(providerId);
			if (provider == null || provider.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，该供应商信息不存在或未开户");
				return resultMap;
			}
			CompanyInf company = companyInfFacade.getCompanyInfByLawCode(companyCode);
			if (company == null || company.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，企业识别码"+companyCode+"不存在或未开户");
				return resultMap;
			}

			BigDecimal providerFee = provider.getProviderRate().add(new BigDecimal(1));
			BigDecimal inAccountAmt = new BigDecimal(remitAmt).divide(providerFee, 2, BigDecimal.ROUND_HALF_UP);
			//inAccountAmt = inAccountAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
			if (!inAccountAmt.toString().equals(inaccountAmt)) {
				logger.error("## 供应商{}上账{}金额不正确，应上账{}", providerId, inaccountAmt, inAccountAmt);
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，供应商上账金额不正确");
				return resultMap;
			}

			int i = providerInfService.addProviderTransfer(req, evidenceUrlFile);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请稍后再试");
			}
		} catch (Exception e) {
			logger.error(" ## 添加供应商上账信息出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加供应商上账信息失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 供应商上账订单提交（调用交易系统充值）
	 * @param req
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/addProviderTransferCommit")
    @ResponseBody
    public Map<String, Object> addProviderTransferCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        try {
            if (providerInfService.addProviderTransferCommit(req) < 1) {
                resultMap.put("status", Boolean.FALSE);
                resultMap.put("msg", "供应商上账失败，请稍后再试");
            }
        } catch (Exception e) {
            logger.error("## 供应商上账异常");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "供应商上账失败，请稍后再试");
            return resultMap;
        }
        return resultMap;
    }

	/**
	 * 更新供应商上账审核状态
	 * @param req
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/updateProviderCheckStatCommit")
    @ResponseBody
    public Map<String, Object> updateProviderCheckStatCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String orderId = StringUtil.nullToString(req.getParameter("orderId"));

        InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
        order.setCheckStat(CheckStatEnum.CHECK_TRUE.getCode());
        order.setUpdateUser(user.getId());
        order.setUpdateTime(System.currentTimeMillis());
        order.setLockVersion(order.getLockVersion() + 1);

        if (!inaccountOrderService.updateById(order)) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "更新上账信息审核状态失败，请稍后再试");
        }
        return resultMap;
    }

	/**
	 * 查询供应商上账订单信息（根据订单ID查询）
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getProviderByOrderId")
	@ResponseBody
	public Map<String, Object> getProviderByOrderId(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		try {
			InaccountOrder order  = inaccountOrderService.getInaccountOrderByOrderId(orderId);
			if (order != null) {
				resultMap.put("msg", order);
			}
		} catch (Exception e) {
			logger.error("## 查询供应商订单异常");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 更新供应商入账订单打款状态（打给平台）
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateProviderRemitStatCommit")
	@ResponseBody
	public Map<String, Object> updateProviderRemitStatCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		try {
			resultMap = providerInfService.updateProviderRemitStatCommit(req);
		} catch (Exception e) {
			logger.error("## 供应商{}打款至平台账户发生异常", providerId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 查询供应商打款订单明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "viewProviderTransferDetail")
	public ModelAndView viewProviderTransferDetail(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("provider/providerInf/viewProviderTransfer");

		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
		if (order != null) {
			order.setCheckStat(CheckStatEnum.findByBId(order.getCheckStat()).getName());
			order.setRemitCheck(RemitCheckEnum.findByBId(order.getRemitCheck()).getName());
			order.setInaccountCheck(InaccountCheckEnum.findByBId(order.getInaccountCheck()).getName());
			order.setTransferCheck(TransferCheckEnum.findByBId(order.getTransferCheck()).getName());
			order.setPlatformReceiverCheck(ReceiverEnum.findByBId(order.getPlatformReceiverCheck()).getName());
			order.setCompanyReceiverCheck(ReceiverEnum.findByBId(order.getCompanyReceiverCheck()).getName());
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
			logger.error("## 查询供应商上账订单明细信息详情异常", e);
		}
		mv.addObject("order", order);
		return mv;
	}

	/**
	 * 更新供应商上账订单信息
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoEditProviderTransfer")
	@ResponseBody
	public Map<String, Object> intoEditProviderTransfer(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		try {
			InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
			CompanyInf company = companyInfFacade.getCompanyInfById(order.getCompanyId());
			if (order != null) {
				order.setRemitAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getRemitAmt().toString())));
				order.setInaccountAmt(new BigDecimal(NumberUtils.RMBCentToYuan(order.getInaccountAmt().toString())));
				order.setCompanyCode(company.getLawCode());
				if (!StringUtil.isNullOrEmpty(order.getEvidenceUrl())) {
					String imgUrl = commonService.getImageStrFromPath(order.getEvidenceUrl());
					if (!StringUtil.isNullOrEmpty(imgUrl)) {
						order.setEvidenceUrl(imgUrl);
					} else {
						order.setEvidenceUrl("");
					}
				}
			}
			List<InaccountOrderDetail> orderDetail = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);
			if (orderDetail != null && orderDetail.size() >= 1) {
				for (InaccountOrderDetail d : orderDetail) {
					d.setTransAmt(new BigDecimal(NumberUtils.RMBCentToYuan(d.getTransAmt().toString())));
				}
			}
			resultMap.put("order", order);
			resultMap.put("orderDetail", orderDetail);
		} catch (Exception e) {
			logger.error("## 编辑---》查询供应商上账信息异常");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询供应商上账信息异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 更新供应商上账订单信息提交
	 * @param req
	 * @param response
	 * @param evidenceUrlFile
	 * @return
	 */
	@RequestMapping(value = "/editProviderTransfer")
	@ResponseBody
	public Map<String, Object> editProviderTransfer(HttpServletRequest req, HttpServletResponse response,
										@RequestParam(value = "evidenceUrlFile", required = false)MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String providerId = StringUtil.nullToString(req.getParameter("providerId"));
		String companyCode = StringUtil.nullToString(req.getParameter("companyCode"));
		try {
			ProviderInf provider = providerInfFacade.getProviderInfById(providerId);
			if (provider == null || provider.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，该供应商信息不存在或未开户");
				return resultMap;
			}
			CompanyInf company = companyInfFacade.getCompanyInfByLawCode(companyCode);
			if (company == null || company.getIsOpen().equals(IsOpenAccountEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，企业识别码"+companyCode+"不存在或未开户");
				return resultMap;
			}
			int i = providerInfService.editProviderTransfer(req, evidenceUrlFile);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请稍后再试");
			}
		} catch (Exception e) {
			logger.error(" ## 编辑供应商上账信息出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑供应商上账信息失败，请稍后再试");
		}
		return resultMap;
	}

	/**
	 * 查询供应商账户余额
	 * @param request
	 * @return
	 */
	@RequestMapping("/listProviderAccBal")
	public ModelAndView listProviderAccBal(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("provider/providerInf/listProviderAccBal");
		String providerId = StringUtil.nullToString(request.getParameter("providerId"));

		try {
			Map<String, Object> resultMap = commonService.getAccountInfPage(request);
			mv.addObject("pageInfo", resultMap.get("pageInfo"));
		} catch (Exception e) {
			logger.error("## 供应商账户列表查询异常", e);
		}
		mv.addObject("providerId", providerId);
		return mv;
	}

	/**
	 * 删除供应商上账订单
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteProviderTransfer")
	@ResponseBody
	public Map<String, Object> deleteProviderTransfer(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = providerInfService.deleteProviderTransfer(req);
		return resultMap;
	}

	/**
	 * 查询供应商账户余额明细
	 * @param request
	 * @return
	 */
	@RequestMapping("/listProviderAccBalDetail")
	public ModelAndView viewProviderAccBalDetail(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("provider/providerInf/listProviderAccBalDetail");
		String providerId = StringUtil.nullToString(request.getParameter("providerId"));

		try {
			Map<String, Object> resultMap = commonService.getAccountLogInfPage(request);
			mv.addObject("pageInfo", resultMap.get("pageInfo"));
		} catch (Exception e) {
			logger.error("## 供应商账户列表查询异常", e);
		}
		mv.addObject("providerId", providerId);
		return mv;
	}

	/**
	 * 封装供应商实体类
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
		providerInf.setLawCode(StringUtil.nullToString(req.getParameter("lawCode")));
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
