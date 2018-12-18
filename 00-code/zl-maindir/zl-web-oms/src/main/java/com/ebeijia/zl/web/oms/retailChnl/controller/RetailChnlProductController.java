package com.ebeijia.zl.web.oms.retailChnl.controller;

import java.math.BigDecimal;

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

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelProductAreaFlag;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelProductProType;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.OperatorType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;
import com.ebeijia.zl.web.oms.retailChnl.service.RetailChnlProductService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "retailChnl/retailChnlProduct")
public class RetailChnlProductController {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RetailChnlProductInfFacade retailChnlProductInfFacade;

	@Autowired
	private RetailChnlProductService retailChnlProductService;

	/**
	 * 分销商产品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listRetailChnlProduct")
	public ModelAndView listRetailChnlProduct(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlProduct/listRetailChnlProduct");
		String operStatus = StringUtil.nullToString(request.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);

		RetailChnlProductInf product = this.getRetailChnlProductInf(request);
		try {
			PageInfo<RetailChnlProductInf> pageList = retailChnlProductInfFacade.getRetailChnlProductInfPage(startNum, pageSize, product);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 分销商产品信息列表查询异常", e);
		}
		mv.addObject("telCPInf", product);
		mv.addObject("areaFlagList", ChannelProductAreaFlag.values());
		mv.addObject("productTypeList", ChannelProductProType.values());
		mv.addObject("operIdList", OperatorType.values());
		mv.addObject("operStatus", operStatus);
		return mv;
	}

	/**
	 * 进入添加分销商产品信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddRetailChnlProduct")
	public ModelAndView intoAddTelChannelProduct(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlProduct/addretailChnlProduct");
		mv.addObject("productTypeList", ChannelProductProType.values());
		mv.addObject("areaFlagList", ChannelProductAreaFlag.values());
		mv.addObject("operIdList", OperatorType.values());
		return mv;
	}

	/**
	 * 添加分销商产品信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/addRetailChnlProductCommit")
	@ResponseBody
	public ModelMap addRetailChnlProductCommit(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		try {
			RetailChnlProductInf retailChnlProductInf = this.getRetailChnlProductInf(req);
			retailChnlProductInf.setDataStat("0");
			HttpSession session = req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			if (user != null) {
				retailChnlProductInf.setCreateUser(user.getId().toString());
				retailChnlProductInf.setUpdateUser(user.getId().toString());
			}
			retailChnlProductInf.setProductId(IdUtil.getNextId());
			retailChnlProductInfFacade.saveTelChannelProductForId(retailChnlProductInf);
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "新增失败，请重新添加");
			logger.error("## 添加分销商产品信息异常", e);
		}
		return resultMap;
	}

	/**
	 * 进入编辑分销商产品信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoEditRetailChnlProduct")
	public ModelAndView intoEditRetailChnlProduct(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlProduct/editRetailChnlProduct");
		String productId = StringUtil.nullToString(req.getParameter("productId"));
		try {
			RetailChnlProductInf retailChnlProductInf = retailChnlProductInfFacade.getRetailChnlProductInfById(productId);
			mv.addObject("telCPInf", retailChnlProductInf);
		} catch (Exception e) {
			logger.error("## 通过id查找分销商产品信息异常", e);
		}
		mv.addObject("areaFlagList", ChannelProductAreaFlag.values());
		mv.addObject("productTypeList", ChannelProductProType.values());
		mv.addObject("operIdList", OperatorType.values());
		return mv;
	}

	/**
	 * 编辑分销商产品信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/editRetailChnlProductCommit")
	@ResponseBody
	public ModelMap editRetailChnlProductCommit(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String productId = StringUtil.nullToString(req.getParameter("productId"));
		try {
			if (StringUtil.isNullOrEmpty(productId)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "编辑失败,分销商产品id为空");
				logger.error("## 编辑分销商产品信息异常,分销商产品productId:[{}]为空", productId);
			}
			RetailChnlProductInf telCPInf = retailChnlProductInfFacade.getRetailChnlProductInfById(productId);
			RetailChnlProductInf retailChnlProductInf = this.getRetailChnlProductInf(req);
			HttpSession session = req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			if (user != null) {
				telCPInf.setUpdateUser(user.getId().toString());
			}
			telCPInf.setOperName(retailChnlProductInf.getOperName());
			telCPInf.setOperId(retailChnlProductInf.getOperId());
			telCPInf.setAreaFlag(retailChnlProductInf.getAreaFlag());
			telCPInf.setProductAmt(retailChnlProductInf.getProductAmt());
			telCPInf.setProductPrice(retailChnlProductInf.getProductPrice());
			telCPInf.setProductType(retailChnlProductInf.getProductType());
			telCPInf.setRemarks(retailChnlProductInf.getRemarks());
			retailChnlProductInfFacade.updateRetailChnlProductInf(telCPInf);
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "编辑失败，请联系管理员");
			logger.error("## 编辑分销商产品信息异常", e);
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
	@RequestMapping(value = "intoViewRetailChnlProduct")
	public ModelAndView intoViewRetailChnlProduct(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlProduct/viewRetailChnlProduct");
		String productId = StringUtil.nullToString(req.getParameter("productId"));
		try {
			if (!StringUtil.isNullOrEmpty(productId)) {
				RetailChnlProductInf telCPInf = retailChnlProductInfFacade.getRetailChnlProductInfById(productId);
				mv.addObject("telCPInf", telCPInf);
			}
		} catch (Exception e) {
			logger.error("## 查询分销商产品信息详情异常", e);
		}
		mv.addObject("areaFlagList", ChannelProductAreaFlag.values());
		mv.addObject("productTypeList", ChannelProductProType.values());
		mv.addObject("operIdList", OperatorType.values());
		return mv;
	}

	/**
	 * 删除分销商产品信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/deleteRetailChnlProductCommit")
	@ResponseBody
	public ModelMap deleteRetailChnlProductCommit(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String productId = StringUtil.nullToString(req.getParameter("productId"));
		try {
			if (StringUtil.isNullOrEmpty(productId)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "删除失败,分销商产品id为空");
				logger.error("## 删除分销商产品信息异常,供应商productId:[{}]为空", productId);
			}
			retailChnlProductService.deleteRetailChnlProductInf(productId);
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "删除失败，请联系管理员");
			logger.error("## 删除分销商产品信息异常", e);
		}
		return resultMap;
	}
	
	public RetailChnlProductInf getRetailChnlProductInf(HttpServletRequest req) {
		String productAmt = StringUtil.nullToString(req.getParameter("productAmt"));
		String productPrice = StringUtil.nullToString(req.getParameter("productPrice"));
		RetailChnlProductInf product = new RetailChnlProductInf();
		product.setOperName(StringUtil.nullToString(req.getParameter("operName")));
		product.setOperId(StringUtil.nullToString(req.getParameter("operId")));
		product.setAreaFlag(StringUtil.nullToString(req.getParameter("areaFlag")));
		if (!StringUtil.isNullOrEmpty(productAmt))
			product.setProductAmt(new BigDecimal(productAmt));
		if (!StringUtil.isNullOrEmpty(productPrice))
			product.setProductPrice(new BigDecimal(productPrice));
		product.setProductType(StringUtil.nullToString(req.getParameter("productType")));
		product.setRemarks(StringUtil.nullToString(req.getParameter("remarks")));
		return product;
	}
}
