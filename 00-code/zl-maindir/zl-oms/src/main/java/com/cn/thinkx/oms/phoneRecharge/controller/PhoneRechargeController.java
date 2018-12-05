package com.cn.thinkx.oms.phoneRecharge.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.oms.phoneRecharge.model.PhoneRechargeOrder;
import com.cn.thinkx.oms.phoneRecharge.model.PhoneRechargeOrderUpload;
import com.cn.thinkx.oms.phoneRecharge.service.PhoneRechargeService;
import com.cn.thinkx.oms.utils.ExcelUtil;
import com.cn.thinkx.pms.base.utils.BaseConstants;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.common.utils.tools.UploadUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "phone/phoneRecharge")
public class PhoneRechargeController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("phoneRechargeService")
	private PhoneRechargeService phoneRechargeService;

	/**
	 * 手机充值订单交易明细列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getPhoneRechargeList")
	public ModelAndView getPhoneRechargeList(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("trans/phoneRecharge/listPhoneRecharge");
		PageInfo<PhoneRechargeOrder> pageList = null;
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PhoneRechargeOrder pro = this.getPhoneRechargeOrder(req);
		pageList = phoneRechargeService.getPhoneRechargeListPage(startNum, pageSize, pro);
		mv.addObject("phoneRechargeOrder", pro);
		mv.addObject("pageInfo", pageList);
		mv.addObject("supplierList", BaseConstants.phoneRechargeSupplier.values());
		mv.addObject("transStatList", BaseConstants.phoneRechargeTransStat.values());
		mv.addObject("orderTypeList", BaseConstants.phoneRechargeOrderType.values());
		mv.addObject("reqChannelList", BaseConstants.phoneRechargeReqChnl.values());
		return mv;
	}

	/**
	 * 导出excle表格
	 * 
	 * @param req
	 * @param response
	 */
	@RequestMapping(value = "/uploadListPhoneRecharge")
	public void uploadListPhoneRecharge(HttpServletRequest req, HttpServletResponse response) {
		try {
			PhoneRechargeOrder pro = this.getPhoneRechargeOrder(req);
			List<PhoneRechargeOrderUpload> listProLog = phoneRechargeService.getPhoneRechargeList(pro);

			String title = "手机充值交易明细列表";
			String titlerow = "手机充值交易报表";
			String[] titlehead = new String[] { "订单号", "供应商订单号", "交易流水号", "用户名", "会员手机号", "供应商", "手机充值号码", "充值金额（元）",
					"交易金额（元）", "优惠金额（元）", "供应商价格（元）", "流量面额（元）", "交易状态", "订单类型", "支付渠道", "交易时间" };
			ExcelUtil<PhoneRechargeOrderUpload> ex = new ExcelUtil<PhoneRechargeOrderUpload>();
			HSSFWorkbook workBook = ex.exportExcel(title, titlerow, pro.getStartTime(), pro.getEndTime(), titlehead,
					null, null, listProLog, PhoneRechargeOrderUpload.class, null);
			UploadUtil.upLoad(workBook, title, response);
		} catch (Exception e) {
			logger.error("## 手机充值交易报表出错", e);
		}
	}
	
	/**
	 * 手机充值退款
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/refundPhoneRecharge")
	@ResponseBody
	public ModelMap refundPhoneRecharge(HttpServletRequest req){
		ModelMap resultMap = new ModelMap();
		String rId = StringUtil.nullToString(req.getParameter("rId"));
		String resultStr = phoneRechargeService.doPhoneRechargeRefund(rId);
		resultMap = JSONArray.parseObject(resultStr, ModelMap.class);
		return resultMap;
	}

	/**
	 * 封装手机充值订单表
	 * 
	 * @param req
	 * @return
	 */
	public PhoneRechargeOrder getPhoneRechargeOrder(HttpServletRequest req) {
		PhoneRechargeOrder pro = new PhoneRechargeOrder();
		pro.setrId(StringUtil.nullToString(req.getParameter("rId")));
		pro.setSupplierOrderNo(StringUtil.nullToString(req.getParameter("supplierOrderNo")));
		pro.setChannelOrderNo(StringUtil.nullToString(req.getParameter("channelOrderNo")));
		pro.setMobilePhoneNo(StringUtil.nullToString(req.getParameter("mobilePhoneNo")));
		pro.setPersonalName(StringUtil.nullToString(req.getParameter("personalName")));
		pro.setPhone(StringUtil.nullToString(req.getParameter("phone")));
		pro.setSupplier(StringUtil.nullToString(req.getParameter("supplier")));
		pro.setTransStat(StringUtil.nullToString(req.getParameter("transStat")));
		pro.setOrderType(StringUtil.nullToString(req.getParameter("orderType")));
		pro.setReqChannel(StringUtil.nullToString(req.getParameter("reqChannel")));
		pro.setStartTime(StringUtil.nullToString(req.getParameter("startTime")));
		pro.setEndTime(StringUtil.nullToString(req.getParameter("endTime")));
		return pro;
	}

}
