package com.ebeijia.zl.web.oms.retailChnl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.facade.telrecharge.domain.*;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
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
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelProductAreaFlag;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelProductProType;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.OperatorType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;
import com.ebeijia.zl.web.oms.retailChnl.model.RetailChnlProductCheck;
import com.ebeijia.zl.web.oms.retailChnl.service.RetailChnlInfService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "retailChnl/retailChnlInf")
public class RetailChnlInfController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RetailChnlInfFacade retailChnlInfFacade;

	@Autowired
	private RetailChnlProductInfFacade retailChnlProductInfFacade;

	@Autowired
	private RetailChnlInfService retailChnlInfService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private RetailChnlItemListFacade retailChnlItemListFacade;

	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

	/**
	 * 分销商信息列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/listRetailChnlInf")
	public ModelAndView listRetailChnlInf(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/listRetailChnlInf");
		String operStatus = StringUtil.nullToString(request.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);

		String channelName = StringUtil.nullToString(request.getParameter("channelName"));
		RetailChnlInf retailChnlInf = new RetailChnlInf();
		retailChnlInf.setChannelName(channelName);
		PageInfo<RetailChnlInf> pageList = null;
		try {
			pageList = retailChnlInfFacade.getRetailChnlInfPage(startNum, pageSize, retailChnlInf);
		} catch (Exception e) {
			logger.error(" ## 查询分销商信息列表出错",e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("retailChnlInf", retailChnlInf);
		return mv;
	}

	@RequestMapping(value = "/intoAddRetailChnlInf")
	public ModelAndView intoAddRetailChnlInf(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/addRetailChnlInf");

		return mv;
	}

	@RequestMapping(value = "/addRetailChnlInfCommit")
	@ResponseBody
	public Map<String, Object> addRetailChnlInfCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			RetailChnlInf retailChnlInf = getRetailChnlInf(req);
			if (!retailChnlInfFacade.saveRetailChnlInf(retailChnlInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加分销商信息失败");
			}
		} catch (Exception e) {
			logger.error(" ## 添加分销商信息出错",e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加分销商信息失败");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "/intoEditRetailChnlInf")
	public ModelAndView intoEditTelChannelInf(HttpServletRequest req, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/editRetailChnlInf");

		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		RetailChnlInf retailChnlInf = null;
		try {
			retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(channelId);
		} catch (Exception e) {
			logger.error(" ## 跳转分销商信息编辑页面出错",e);
		}
		mv.addObject("retailChnlInf", retailChnlInf);
		return mv;
	}

	@RequestMapping(value = "/editRetailChnlInfCommit")
	@ResponseBody
	public Map<String, Object> editTelChannelInfCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);

		try {
			RetailChnlInf retailChnlInf = getRetailChnlInf(req);
			if (!retailChnlInfFacade.updateRetailChnlInf(retailChnlInf)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑分销商信息失败");
				return resultMap;
			}
		} catch (Exception e) {
			logger.error(" ## 编辑分销商信息列表出错",e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑分销商信息失败");
		}

		return resultMap;
	}

	@RequestMapping(value = "/intoViewRetailChnlInf")
	public ModelAndView intoViewTelChannelInf(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/viewRetailChnlInf");
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		RetailChnlInf retailChnlInf = null;
		try {
			retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(channelId);
		} catch (Exception e) {
			logger.error(" ## 查看分销商信息详情出错",e);
		}
		mv.addObject("retailChnlInf", retailChnlInf);
		return mv;
	}

	@RequestMapping(value = "/deleteRetailChnlInfCommit")
	@ResponseBody
	public Map<String, Object> deleteRetailChnlInfCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		try {
			if (!retailChnlInfFacade.deleteRetailChnlInfById(channelId)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除分销商信息失败，请重新操作");
			}
		} catch (Exception e) {
			logger.error(" ## 删除分销商信息出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除分销商信息失败，请重新操作");
		}
		return resultMap;
	}

	/**
	 * 进入分销商添加折扣率
	 *
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/intoAddRetailChnlRate")
	public ModelAndView intoAddRetailChnlRate(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/listRetailChnlProductRate");

		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		if (StringUtil.isNullOrEmpty(channelId)) {
			logger.error("## 进入分销商添加折扣率出错,分销商主键channelId：[{}]是空", channelId);
			return mv;
		}
		List<RetailChnlProductInf> listAll = null;
		RetailChnlInf retailChnlInf = null;
		List<RetailChnlProductInf> retailChnlProductList = null;

		RetailChnlProductCheck check = null;
		List<RetailChnlProductCheck> retailChnlProductistCheck = new ArrayList<RetailChnlProductCheck>();

		RetailChnlProductInf retailProduct = new RetailChnlProductInf();
		try {
			// 查询分销商信息
			retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(channelId);

			// 全部的手机充值产品
			retailProduct.setOperId(StringUtil.nullToString(req.getParameter("operId")));
			listAll = retailChnlProductInfFacade.getRetailChnlProductInfList(retailProduct);
			// 当前分销商有手机充值产品
			retailChnlProductList = retailChnlProductInfFacade.getChannelProductListByChannelId(channelId);
			if (!StringUtil.isNullOrEmpty(retailChnlProductList) && listAll.size() > 0) {
				for (RetailChnlProductInf channelProductAll : listAll) {
					check = new RetailChnlProductCheck();
					check.setProductId(channelProductAll.getProductId());
					check.setChannelRate(channelProductAll.getChannelRate());
					check.setOperId(OperatorType.findByCode(channelProductAll.getOperId()));
					check.setProductType(ChannelProductProType.findByCode(channelProductAll.getProductType()));
					/*check.setOperName(channelProductAll.getOperName());*/
					/*check.setProductAmt(channelProductAll.getProductAmt());*/
					check.setProductPrice(channelProductAll.getProductPrice());
					check.setCreateTime(channelProductAll.getCreateTime());
					check.setUpdateTime(channelProductAll.getUpdateTime());
					check.setAreaFlag(ChannelProductAreaFlag.findByCode(channelProductAll.getAreaFlag()));
					check.setChannelRate(channelProductAll.getChannelRate());
					for (RetailChnlProductInf retailChnlProduct : retailChnlProductList) {
						if (channelProductAll.getProductId().equals(retailChnlProduct.getProductId())) {
							check.setChecked(true);
							check.setChannelRate(retailChnlProduct.getChannelRate());
							check.setId(retailChnlProduct.getProductId());
							break;
						}else{
							check.setChannelRate(new BigDecimal("1"));
						}
					}
					retailChnlProductistCheck.add(check);
				}
			}
		} catch (Exception e) {
			logger.error(" ## 进入分销商添加折扣率出错", e);
		}
		mv.addObject("operIdList", OperatorType.values());
		mv.addObject("retailChnlProductistCheck", retailChnlProductistCheck);
		mv.addObject("retailChnlInf", retailChnlInf);
		mv.addObject("telCPInf", retailProduct);
		return mv;
	}

	/**
	 * 添加分销商添加折扣率
	 *
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addRetailChnlRateCommit")
	@ResponseBody
	public Map<String, Object> addRetailChnlRateCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));
		String channelRate = StringUtil.nullToString(req.getParameter("channelRate"));
		String ids = req.getParameter("ids");
		if (StringUtil.isNullOrEmpty(ids)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加分销商折扣率失败,没有选中的数据");
			return resultMap;
		}
		if (StringUtil.isNullOrEmpty(channelId)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加分销商折扣率失败,分销商是空");
			return resultMap;
		}
		if (StringUtil.isNullOrEmpty(channelRate)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加分销商折扣率失败,分销商折扣率是空");
			return resultMap;
		}
		if (!retailChnlInfService.addRetailChnlRate(req, channelId, channelRate, ids)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加分销商折扣率失败,请联系管理员");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 进入分销商折扣率编辑页面
	 *
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoEditRetailChnlProductRate")
	public ModelAndView intoEditRetailChnlProductRate(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/editRetailChnlProductRate");
		String id = StringUtil.nullToString(req.getParameter("id"));
		try {
			if(StringUtil.isNullOrEmpty(id)){
				return mv;
			}
			RetailChnlProductInf retailChnlProductInf = retailChnlProductInfFacade.getChannelProductByItemId(id);
			if(!StringUtil.isNullOrEmpty(retailChnlProductInf)){
				if(!StringUtil.isNullOrEmpty(retailChnlProductInf.getAreaFlag()))
					retailChnlProductInf.setAreaFlag(ChannelProductAreaFlag.findByCode(retailChnlProductInf.getAreaFlag()));
				if(!StringUtil.isNullOrEmpty(retailChnlProductInf.getOperId()))
					retailChnlProductInf.setOperId(OperatorType.findByCode(retailChnlProductInf.getOperId()));
				if(!StringUtil.isNullOrEmpty(retailChnlProductInf.getProductType()))
					retailChnlProductInf.setProductType(ChannelProductProType.findByCode(retailChnlProductInf.getProductType()));
			}
			mv.addObject("retailChnlProductInf", retailChnlProductInf);
		} catch (Exception e) {
			logger.error("## 通过id查找分销商产品信息异常", e);
		}
		mv.addObject("areaFlagList", ChannelProductAreaFlag.values());
		mv.addObject("productTypeList", ChannelProductProType.values());
		mv.addObject("operIdList", OperatorType.values());
		return mv;
	}

	/**
	 * 分销商折扣率编辑提交
	 *
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editRetailChnlProductRateCommit")
	@ResponseBody
	public Map<String, Object> editRetailChnlProductRateCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String itemId = StringUtil.nullToString(req.getParameter("itemId"));
		try {
			if (StringUtil.isNullOrEmpty(itemId)) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑失败,分销商产品折扣率id为空");
				logger.error("## 编辑分销商产品信息折扣率异常,分销商产品折扣率id:[{}]为空", itemId);
			}
			RetailChnlItemList retailChnlItemList = retailChnlItemListFacade.getRetailChnlItemListById(itemId);
			HttpSession session = req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			if (user != null) {
				retailChnlItemList.setUpdateUser(user.getId().toString());
			}
			if(!StringUtil.isNullOrEmpty(req.getParameter("channelRate")))
				retailChnlItemList.setChannelRate(new BigDecimal(req.getParameter("channelRate")));
			retailChnlItemListFacade.updateRetailChnlItemList(retailChnlItemList);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑失败，请联系管理员");
			logger.error("## 编辑分销商产品信息异常", e);
		}
		return resultMap;
	}

	@RequestMapping(value = "/retailChnlOpenAccount")
	@ResponseBody
	public Map<String, Object> retailChnlOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			int i = retailChnlInfService.retailChnlOpenAccount(req);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "开户失败，请重新操作");
			}
		} catch (Exception e) {
			logger.error(" ## 分销商开户出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "分销商开户失败，请重新操作");
		}
		return resultMap;
	}

	@RequestMapping("/listRetailChnlAccBal")
	public ModelAndView listRetailChnlAccBal(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/listRetailChnlAccBal");
		try {
			Map<String, Object> resultMap = commonService.getAccountInfPage(request);
			mv.addObject("pageInfo", resultMap.get("pageInfo"));
		} catch (Exception e) {
			logger.error("## 分销商账户列表查询异常", e);
		}
		return mv;
	}

	@RequestMapping(value = "intoAddRetailChnlTransfer")
	public ModelAndView intoAddRetailChnlTransfer(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/addRetailChnlTransfer");

		String channelId = StringUtil.nullToString(request.getParameter("channelId"));
		InaccountOrder order = new InaccountOrder();
		order.setProviderId(channelId);
		order.setOrderType(UserType.TYPE400.getCode());
		try {
			int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);
			PageInfo<InaccountOrder> pageList = inaccountOrderService.getInaccountOrderByOrderPage(startNum, pageSize, order);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询分销商上账信息详情异常", e);
		}
		mv.addObject("channelId", channelId);
		return mv;
	}

	@RequestMapping(value = "/addRetailChnlTransfer")
	@ResponseBody
	public Map<String, Object> addRetailChnlTransfer(HttpServletRequest req, HttpServletResponse response,
													 @RequestParam(value = "evidenceUrlFile", required = false)MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));
		try {
			RetailChnlInf retailChnl = retailChnlInfFacade.getRetailChnlInfById(channelId);
			if (retailChnl == null || retailChnl.getIsOpen().equals(IsOpenEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，该分销商信息不存在或未开户");
				return resultMap;
			}
			int i = retailChnlInfService.addRetailChnlTransfer(req, evidenceUrlFile);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加上账信息失败，请稍后再试");
			}
		} catch (Exception e) {
			logger.error(" ## 添加分销商上账信息出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "添加分销商上账信息失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "/addRetailChnlTransferCommit")
	@ResponseBody
	public Map<String, Object> addRetailChnlTransferCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			if (retailChnlInfService.addRetailChnlTransferCommit(req) < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "分销商上账失败，请稍后再试");
			}
		} catch (Exception e) {
			logger.error("## 分销商上账异常");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "分销商上账失败，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "/updateRetailChnlCheckStatCommit")
	@ResponseBody
	public Map<String, Object> updateRetailChnlCheckStatCommit(HttpServletRequest req, HttpServletResponse response) {
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

	@RequestMapping(value = "/getRetailChnlByOrderId")
	@ResponseBody
	public Map<String, Object> getRetailChnlByOrderId(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		try {
			InaccountOrder order  = inaccountOrderService.getInaccountOrderByOrderId(orderId);
			if (order != null) {
				resultMap.put("msg", order);
			}
		} catch (Exception e) {
			logger.error("## 查询分销商订单异常");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "viewRetailChnlTransferDetail")
	public ModelAndView viewRetailChnlTransferDetail(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlInf/viewRetailChnlTransfer");

		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
		if (order != null) {
			order.setCheckStat(CheckStatEnum.findByBId(order.getCheckStat()).getName());
			order.setRemitCheck(RemitCheckEnum.findByBId(order.getRemitCheck()).getName());
			order.setInaccountCheck(InaccountCheckEnum.findByBId(order.getInaccountCheck()).getName());
			order.setTransferCheck(TransferCheckEnum.findByBId(order.getTransferCheck()).getName());
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
			logger.error("## 查询分销商上账订单明细信息详情异常", e);
		}
		mv.addObject("order", order);
		return mv;
	}

	@RequestMapping(value = "/intoEditRetailChnlTransfer")
	@ResponseBody
	public Map<String, Object> intoEditRetailChnlTransfer(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		try {
			InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
			List<InaccountOrderDetail> orderDetail = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);
			if (orderDetail != null && orderDetail.size() >= 1) {
				for (InaccountOrderDetail d : orderDetail) {
					d.setTransAmt(new BigDecimal(NumberUtils.RMBCentToYuan(d.getTransAmt().toString())));
				}
			}
			resultMap.put("order", order);
			resultMap.put("orderDetail", orderDetail);
		} catch (Exception e) {
			logger.error("## 编辑---》查询分销商上账信息异常");
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "查询分销商上账信息异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "/editRetailChnlTransfer")
	@ResponseBody
	public Map<String, Object> editRetailChnlTransfer(HttpServletRequest req, HttpServletResponse response,
													  @RequestParam(value = "evidenceUrlFile", required = false)MultipartFile evidenceUrlFile) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));
		try {
			RetailChnlInf retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(channelId);
			if (retailChnlInf == null || retailChnlInf.getIsOpen().equals(IsOpenEnum.ISOPEN_FALSE.getCode())) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，该分销商信息不存在或未开户");
				return resultMap;
			}
			int i = retailChnlInfService.editRetailChnlTransfer(req, evidenceUrlFile);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "编辑上账信息失败，请稍后再试");
			}
		} catch (Exception e) {
			logger.error(" ## 编辑分销商上账信息出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑分销商上账信息失败，请稍后再试");
		}
		return resultMap;
	}

	@RequestMapping(value = "/deleteRetailChnlTransfer")
	@ResponseBody
	public Map<String, Object> deleteRetailChnlTransfer(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = retailChnlInfService.deleteRetailChnlTransfer(req);
		return resultMap;
	}

	private RetailChnlInf getRetailChnlInf(HttpServletRequest req) throws Exception {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String channelId = StringUtil.nullToString(req.getParameter("channelId"));
		String channelName = StringUtil.nullToString(req.getParameter("channelName"));
		String channelCode = StringUtil.nullToString(req.getParameter("channelCode"));
		String channelKey = StringUtil.nullToString(req.getParameter("channelKey"));
		String channelReserveAmt = StringUtil.nullToString(req.getParameter("channelReserveAmt"));
		String channelPrewarningAmt = StringUtil.nullToString(req.getParameter("channelPrewarningAmt"));
		String phoneNo = StringUtil.nullToString(req.getParameter("phoneNo"));
		String email = StringUtil.nullToString(req.getParameter("email"));
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));
		String lockVersion = StringUtil.nullToString(req.getParameter("lockVersion"));

		RetailChnlInf retailChnl = null;
		if (!StringUtil.isNullOrEmpty(channelId)) {
			retailChnl = retailChnlInfFacade.getRetailChnlInfById(channelId);
			if (!StringUtil.isNullOrEmpty(lockVersion)) {
				retailChnl.setLockVersion(Integer.valueOf(lockVersion) + 1);
			}
		} else {
			retailChnl = new RetailChnlInf();
			retailChnl.setChannelId(IdUtil.getNextId());
			retailChnl.setCreateUser(user.getId());
			retailChnl.setCreateTime(System.currentTimeMillis());
			retailChnl.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			retailChnl.setLockVersion(0);
		}

		retailChnl.setChannelId(channelId);
		retailChnl.setChannelName(channelName);
		retailChnl.setChannelCode(channelCode);
		retailChnl.setChannelKey(channelKey);
		if (!StringUtil.isNullOrEmpty(channelReserveAmt)) {
			String reserveAmtCent = NumberUtils.RMBYuanToCent(channelReserveAmt);
			retailChnl.setChannelReserveAmt(new BigDecimal(reserveAmtCent));
		}
		if (!StringUtil.isNullOrEmpty(channelPrewarningAmt)) {
			String prewarningAmtCent = NumberUtils.RMBYuanToCent(channelPrewarningAmt);
			retailChnl.setChannelPrewarningAmt(new BigDecimal(prewarningAmtCent));
		}
		retailChnl.setPhoneNo(phoneNo);
		retailChnl.setEmail(email);
		retailChnl.setRemarks(remarks);
		retailChnl.setUpdateUser(user.getId());
		retailChnl.setUpdateTime(System.currentTimeMillis());
		return retailChnl;
	}

}
