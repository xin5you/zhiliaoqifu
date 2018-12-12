package com.ebeijia.zl.web.oms.phoneRecharge.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

import com.alibaba.dubbo.config.annotation.Reference;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelProductAreaFlag;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelProductProType;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.OperatorType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlItemList;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;
import com.ebeijia.zl.web.oms.phoneRecharge.model.TelChannelProductCheck;
import com.ebeijia.zl.web.oms.phoneRecharge.service.TelChannelInfService;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "channel/channelInf")
public class TelChannelInfController {
	
	public static void main(String[] args) {
		System.out.println(Long.toString(new Date().getTime()));
		/*//时间戳转化为Sting或Date
		SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		Long time1=new Long(445555555);
		String d = format1.format(time1);
		Date date1 = null;
		try {
			date1 = format1.parse(d);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Format To String(Date):"+d);
		System.out.println("Format To Date:"+date1);
		
		
		//Date或者String转化为时间戳
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String time="1970-01-06 11:45:55";
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.print("Format To times:"+date.getTime());*/
	}

	Logger logger = LoggerFactory.getLogger(getClass());

	@Reference(check=false)
	private RetailChnlInfFacade telChannelInfFacade;

	@Reference(check=false)
	private RetailChnlProductInfFacade telChannelProductInfFacade;

	@Autowired
	private TelChannelInfService telChannelInfService;
	
	@Reference(check=false)
	private RetailChnlItemListFacade telChannelItemListFacade;

	/**
	 * 分销商信息列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/listTelChannelInf")
	public ModelAndView listTelChannelInf(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelInf/listTelChannelInf");
		String operStatus = StringUtil.nullToString(request.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);

		String channelName = StringUtil.nullToString(request.getParameter("channelName"));
		RetailChnlInf telChannelInf = new RetailChnlInf();
		telChannelInf.setChannelName(channelName);
		PageInfo<RetailChnlInf> pageList = null;
		try {
			pageList = telChannelInfFacade.getRetailChnlInfPage(startNum, pageSize, telChannelInf);
		} catch (Exception e) {
			logger.error(" ## 查询分销商信息列表出错",e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("telChannelInf", telChannelInf);
		return mv;
	}

	@RequestMapping(value = "/intoAddTelChannelInf")
	public ModelAndView intoAddTelChannelInf(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelInf/addTelChannelInf");

		return mv;
	}

	@RequestMapping(value = "/addTelChannelInfCommit")
	public ModelAndView addTelChannelInfCommit(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("redirect:/channel/channelInf/listTelChannelInf.do");

		try {
			RetailChnlInf telChannelInf = getRetailChnlInf(req);
			HttpSession session = req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			telChannelInf.setCreateUser(user.getId().toString());
			telChannelInf.setUpdateUser(user.getId().toString());
			telChannelInf.setCreateTime(System.currentTimeMillis());
			telChannelInf.setUpdateTime(System.currentTimeMillis());
			telChannelInf.setChannelId(UUID.randomUUID().toString());
			if (telChannelInfFacade.saveRetailChnlInf(telChannelInf)) {
				mv.addObject("operStatus", 1);
			}
		} catch (Exception e) {
			logger.error(" ## 添加分销商信息出错",e);
		}
		return mv;
	}

	@RequestMapping(value = "/intoEditTelChannelInf")
	public ModelAndView intoEditTelChannelInf(HttpServletRequest req, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelInf/editTelChannelInf");

		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		RetailChnlInf telChannelInf = null;
		try {
			telChannelInf = telChannelInfFacade.getRetailChnlInfById(channelId);
		} catch (Exception e) {
			logger.error(" ## 跳转分销商信息编辑页面出错",e);
		}
		mv.addObject("telChannelInf", telChannelInf);
		return mv;
	}

	@RequestMapping(value = "/editTelChannelInfCommit")
	public ModelAndView editTelChannelInfCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("redirect:/channel/channelInf/listTelChannelInf.do");

		try {
			RetailChnlInf telChannelInf = getRetailChnlInf(req);
			HttpSession session = req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			telChannelInf.setUpdateUser(user.getId().toString());
			telChannelInf.setUpdateTime(System.currentTimeMillis());
			if (telChannelInfFacade.updateRetailChnlInf(telChannelInf)) {
				mv.addObject("operStatus", 2);
			}
		} catch (Exception e) {
			logger.error(" ## 编辑分销商信息列表出错",e);
		}

		return mv;
	}

	@RequestMapping(value = "/intoViewTelChannelInf")
	public ModelAndView intoViewTelChannelInf(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelInf/viewTelChannelInf");
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		RetailChnlInf telChannelInf = null;
		try {
			telChannelInf = telChannelInfFacade.getRetailChnlInfById(channelId);
		} catch (Exception e) {
			logger.error(" ## 查看分销商信息详情出错",e);
		}
		mv.addObject("telChannelInf", telChannelInf);
		return mv;
	}

	@RequestMapping(value = "/deleteTelChannelInfCommit")
	@ResponseBody
	public ModelMap deleteTelChannelInfCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		try {
			if (telChannelInfFacade.deleteRetailChnlInfById(channelId)) {
				
			} else {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除商品失败，请重新操作");
			}
		} catch (Exception e) {
			logger.error(" ## 删除手机充值商品出错 ", e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除商品失败，请重新操作");
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
	@RequestMapping(value = "/intoAddTelChannelRate")
	public ModelAndView intoAddTelChannelRate(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelInf/listTelChannelProductRate");

		String channelId = StringUtil.nullToString(req.getParameter("channelId"));

		if (StringUtil.isNullOrEmpty(channelId)) {
			logger.error("## 添加分销商信息出错,分销商主键channelId：[{}]是空", channelId);
			return mv;
		}
		List<RetailChnlProductInf> listAll = null;
		RetailChnlInf telChannelInf = null;
		List<RetailChnlProductInf> channelProductist = null;

		TelChannelProductCheck check = null;
		List<TelChannelProductCheck> channelProductistCheck = new ArrayList<TelChannelProductCheck>();

		RetailChnlProductInf telProduct = new RetailChnlProductInf();
		try {
			// 查询分销商信息
			telChannelInf = telChannelInfFacade.getRetailChnlInfById(channelId);

			// 全部的手机充值产品
			telProduct.setOperId(StringUtil.nullToString(req.getParameter("operId")));
			listAll = telChannelProductInfFacade.getRetailChnlProductInfList(telProduct);
			// 当前分销商有手机充值产品
			channelProductist = telChannelProductInfFacade.getChannelProductListByChannelId(channelId);
			if (!StringUtil.isNullOrEmpty(channelProductist) && listAll.size() > 0) {
				for (RetailChnlProductInf channelProductAll : listAll) {
					check = new TelChannelProductCheck();
					check.setProductId(channelProductAll.getProductId());
					check.setChannelRate(channelProductAll.getChannelRate());
					check.setOperId(OperatorType.findByCode(channelProductAll.getOperId()));
					check.setProductType(ChannelProductProType.findByCode(channelProductAll.getProductType()));
					check.setOperName(channelProductAll.getOperName());
					check.setProductAmt(channelProductAll.getProductAmt());
					check.setProductPrice(channelProductAll.getProductPrice());
					check.setCreateTime(channelProductAll.getCreateTime());
					check.setUpdateTime(channelProductAll.getUpdateTime());
					check.setAreaFlag(ChannelProductAreaFlag.findByCode(channelProductAll.getAreaFlag()));
					check.setChannelRate(channelProductAll.getChannelRate());
					for (RetailChnlProductInf telChannelProduct : channelProductist) {
						if (channelProductAll.getProductId().equals(telChannelProduct.getProductId())) {
							check.setChecked(true);
							check.setChannelRate(telChannelProduct.getChannelRate());
							check.setId(telChannelProduct.getProductId());
							break;
						}else{
							check.setChannelRate(new BigDecimal("1"));
						}
					}
					channelProductistCheck.add(check);
				}
			}
		} catch (Exception e) {
			logger.error(" ## 添加分销商信息出错", e);
		}
		mv.addObject("operIdList", OperatorType.values());
		mv.addObject("channelProductistCheck", channelProductistCheck);
		mv.addObject("telChannelInf", telChannelInf);
		mv.addObject("telCPInf", telProduct);
		return mv;
	}

	/**
	 * 添加分销商添加折扣率
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addTelChannelRateCommit")
	@ResponseBody
	public ModelMap addTelChannelRateCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
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
		if (!telChannelInfService.addTelChannelRate(req, channelId, channelRate, ids)) {
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
	@RequestMapping(value = "/intoEditTelChannelProductRate")
	public ModelAndView intoEditTelChannelProductRate(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelInf/editTelChannelProductRate");
		String id = StringUtil.nullToString(req.getParameter("id"));
		try {
			if(StringUtil.isNullOrEmpty(id)){
				return mv;
			}
			RetailChnlProductInf telProductInf = telChannelProductInfFacade.getChannelProductByItemId(id);
			if(!StringUtil.isNullOrEmpty(telProductInf)){
				if(!StringUtil.isNullOrEmpty(telProductInf.getAreaFlag()))
					telProductInf.setAreaFlag(ChannelProductAreaFlag.findByCode(telProductInf.getAreaFlag()));
				if(!StringUtil.isNullOrEmpty(telProductInf.getOperId()))
					telProductInf.setOperId(OperatorType.findByCode(telProductInf.getOperId()));
				if(!StringUtil.isNullOrEmpty(telProductInf.getProductType()))
					telProductInf.setProductType(ChannelProductProType.findByCode(telProductInf.getProductType()));
			}
			mv.addObject("telProductInf", telProductInf);
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
	@RequestMapping(value = "/editTelChannelProductRateCommit")
	@ResponseBody
	public ModelMap editTelChannelProductRateCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String itemId = StringUtil.nullToString(req.getParameter("itemId"));
		try {
			if (StringUtil.isNullOrEmpty(itemId)) {
				resultMap.addAttribute("status", Boolean.FALSE);
				resultMap.addAttribute("msg", "编辑失败,分销商产品折扣率id为空");
				logger.error("## 编辑分销商产品信息折扣率异常,分销商产品折扣率id:[{}]为空", itemId);
			}
			RetailChnlItemList telChannelItemList = telChannelItemListFacade.getRetailChnlItemListById(itemId);
			HttpSession session = req.getSession();
			User user = (User)session.getAttribute(Constants.SESSION_USER);
			if (user != null) {
				telChannelItemList.setUpdateUser(user.getId().toString());
			}
			if(!StringUtil.isNullOrEmpty(req.getParameter("channelRate")))
				telChannelItemList.setChannelRate(new BigDecimal(req.getParameter("channelRate")));
			telChannelItemListFacade.updateRetailChnlItemList(telChannelItemList);
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "编辑失败，请联系管理员");
			logger.error("## 编辑分销商产品信息异常", e);
		}
		return resultMap;
	}
	
	private RetailChnlInf getRetailChnlInf(HttpServletRequest req) throws Exception {
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

		RetailChnlInf telChannelInf = new RetailChnlInf();

		telChannelInf.setChannelId(channelId);
		telChannelInf.setChannelName(channelName);
		telChannelInf.setChannelCode(channelCode);
		telChannelInf.setChannelKey(channelKey);
		telChannelInf.setChannelReserveAmt(new BigDecimal(channelReserveAmt));
		telChannelInf.setChannelPrewarningAmt(new BigDecimal(channelPrewarningAmt));
		telChannelInf.setPhoneNo(phoneNo);
		telChannelInf.setEmail(email);
		telChannelInf.setRemarks(remarks);
		if (!StringUtil.isNullOrEmpty(lockVersion)) {
			telChannelInf.setLockVersion(Integer.valueOf(lockVersion));
		}
		return telChannelInf;
	}

}
