package com.cn.thinkx.ecom.front.api.returns.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.EcomExpressConfirm;
import com.cn.thinkx.ecom.basics.order.domain.EcomRefundAddress;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.OrderRefund;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsInf;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.service.EcomExpressConfirmService;
import com.cn.thinkx.ecom.basics.order.service.EcomRefundAddressService;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.OrderRefundService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.common.constants.Constants.ApplyReturnState;
import com.cn.thinkx.ecom.common.constants.Constants.ConfirmReturnsStatus;
import com.cn.thinkx.ecom.common.constants.Constants.ReturnsStatus;
import com.cn.thinkx.ecom.common.constants.Constants.returnType;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.front.api.returns.service.ReturnsGoodsService;

@RestController
@RequestMapping(value = "ecom/returns")
public class ReturnsGoodsController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ReturnsOrderService returnsOrderService;
	
	@Autowired
	private ReturnsGoodsService returnsGoodsService;
	
	@Autowired
	private OrderProductItemService orderProductItemService;
	
	@Autowired
	private EcomRefundAddressService ecomRefundAddressService;
	
	@Autowired
	private EcomExpressConfirmService ecomExpressConfirmService;
	
	@Autowired
	private OrderRefundService orderRefundService;
	
	@GetMapping(value = "/toReturnsGoods")
	public ModelAndView toReturnsGoods(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = null;
		try {
			String itemId = req.getParameter("itemId");
			ReturnsOrder rOrder = new ReturnsOrder();
			rOrder.setoItemId(itemId);
			ReturnsOrder rerurnsOrder = returnsOrderService.getReturnsOrderByItemId(rOrder);
			if (rerurnsOrder == null) {
				mv = new ModelAndView("ecom/returns/applyForRefund");
				OrderProductItem item = orderProductItemService.getOrderProductItemByItemId(itemId);
				mv.addObject("item", item);
				mv.addObject("returnTypeList", returnType.values());
			} else {
				if (ReturnsStatus.RS0.getCode().equals(rerurnsOrder.getReturnsStatus())
						&& ApplyReturnState.ARS0.getCode().equals(rerurnsOrder.getApplyReturnState())) {//申请中，未退货
					mv = new ModelAndView("redirect:/ecom/returns/waitApply");
					mv.addObject("returnsId", rerurnsOrder.getReturnsId());
				}
				if (ReturnsStatus.RS2.getCode().equals(rerurnsOrder.getReturnsStatus())
						&& ApplyReturnState.ARS1.getCode().equals(rerurnsOrder.getApplyReturnState())) {//已同意，待发货
					mv = new ModelAndView("redirect:/ecom/returns/mconsentApplication");
					mv.addObject("returnsId", rerurnsOrder.getReturnsId());
				}
				if ((ReturnsStatus.RS2.getCode().equals(rerurnsOrder.getReturnsStatus()) 
						&& ApplyReturnState.ARS2.getCode().equals(rerurnsOrder.getApplyReturnState())) 
						|| (ReturnsStatus.RS2.getCode().equals(rerurnsOrder.getReturnsStatus()) 
								&& ApplyReturnState.ARS3.getCode().equals(rerurnsOrder.getApplyReturnState()))) {//已同意，待收获、已同意，已收获
					mv = new ModelAndView("redirect:/ecom/returns/waitForReceiving");
					mv.addObject("returnsId", rerurnsOrder.getReturnsId());
				}
				if (ReturnsStatus.RS9.getCode().equals(rerurnsOrder.getReturnsStatus())
						&& ApplyReturnState.ARS4.getCode().equals(rerurnsOrder.getApplyReturnState())) {//已完成、已退款
					mv = new ModelAndView("redirect:/ecom/returns/returnsSuccess");
					mv.addObject("returnsId", rerurnsOrder.getReturnsId());
				}
				if (ReturnsStatus.RS4.getCode().equals(rerurnsOrder.getReturnsStatus())
						&& ApplyReturnState.ARS5.getCode().equals(rerurnsOrder.getApplyReturnState())) {//已取消、取消退货
					
				}
				if (ReturnsStatus.RS1.getCode().equals(rerurnsOrder.getReturnsStatus())
						&& ApplyReturnState.ARS9.getCode().equals(rerurnsOrder.getApplyReturnState())) {//已拒绝、拒绝退货
					mv = new ModelAndView("redirect:/ecom/returns/refuseReturns");
					mv.addObject("returnsId", rerurnsOrder.getReturnsId());
				}				
			} 
		} catch (Exception e) {
			logger.error(" ## 跳转申请退货页面出错 ", e);
		}
		return mv;
	}
	
	/**
	 * 发起退货请求
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/returnsGoods")
	public ModelAndView returnsGoods(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = null;
		
		String itemId = req.getParameter("itemId");
		String reason = req.getParameter("reason");
		String reasonDesc = req.getParameter("reasonDesc");
		
		ReturnsInf returnsInf = returnsOrderService.getReturnsInfByItemId(itemId);
		
		try {
			BaseResult<Object> result = returnsGoodsService.handleReturnsOrder(returnsInf, reason, reasonDesc);
			if (ExceptionEnum.SUCCESS_CODE.equals(result.getCode())) {
				mv = new ModelAndView("redirect:/ecom/returns/waitApply");
				mv.addObject("returnsId", result.getObject());
			}
		} catch (Exception e) {
			logger.error(" ## 申请退货异常 ", e);
		}
		return mv;
	}
	
	@GetMapping(value = "/waitApply")
	public ModelAndView waitApply(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new ModelAndView("ecom/returns/waitApply");
		
		try {
			String returnsId = req.getParameter("returnsId");
			ReturnsOrder rOrder = new ReturnsOrder();
			rOrder.setReturnsId(returnsId);
			ReturnsOrder rerurnsOrder = returnsOrderService.getReturnsOrderByItemId(rOrder);
			mv.addObject("rerurnsOrder", rerurnsOrder);
		} catch (Exception e) {
			logger.error(" ## 跳转等待商家处理页面出错 ", e);
		}
		return mv;
	}
	
	
	/**
	 * 填写物流单号
	 * @param req
	 * @param resp
	 * @return
	 */
	@GetMapping(value = "/mconsentApplication")
	public ModelAndView mconsentApplication(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new ModelAndView("ecom/returns/mconsentApplication");
		try {
			String returnsId = req.getParameter("returnsId");
			ReturnsOrder rOrder = new ReturnsOrder();
			rOrder.setReturnsId(returnsId);
			ReturnsOrder rerurnsOrder = returnsOrderService.getReturnsOrderByItemId(rOrder);
			
			EcomRefundAddress address = ecomRefundAddressService.selectByReturnsId(returnsId);
			
			mv.addObject("rerurnsOrder", rerurnsOrder);
			mv.addObject("address", address);
		} catch (Exception e) {
			logger.error(" ## 跳转填写物流单号页面出错 ", e);
		}
		return mv;
	}
	
	/**
	 * 退货绑定物流信息
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/offerExpress")
	public ModelAndView offerExpress(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new ModelAndView();
		
		String returnsId = req.getParameter("returnsId");
		String trackingCompany = req.getParameter("trackingCompany");
		String trackingNum = req.getParameter("trackingNum");
		try {
			ReturnsOrder returnsOrder = returnsOrderService.selectByPrimaryKey(returnsId);
			
			EcomExpressConfirm confirm = new EcomExpressConfirm();
			confirm.setReturnsId(returnsOrder.getReturnsId());
			confirm.setTrackingCompany(trackingCompany);
			confirm.setTrackingNum(trackingNum);
			confirm.setReturnsState(ConfirmReturnsStatus.RS10.getCode());
			
			BaseResult<Object> result = returnsGoodsService.handleOfferExpress(returnsOrder, confirm);
			
			if (ExceptionEnum.SUCCESS_CODE.equals(result.getCode())) {
				mv = new ModelAndView("redirect:/ecom/returns/waitForReceiving");
				mv.addObject("returnsId", returnsId);
			}
			
		} catch (Exception e) {
			logger.error(" ## 退货绑定物流信息出错 ", e);
		}
		return mv;
	}
	
	/**
	 * 待商家收货
	 * @param req
	 * @param resp
	 * @return
	 */
	@GetMapping(value = "/waitForReceiving")
	public ModelAndView waitForReceiving(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new  ModelAndView("ecom/returns/waitForReceiving");
		try {
			String returnsId = req.getParameter("returnsId");
			ReturnsOrder rOrder = new ReturnsOrder();
			rOrder.setReturnsId(returnsId);
			
			ReturnsOrder rerurnsOrder = returnsOrderService.getReturnsOrderByItemId(rOrder);
			
			EcomRefundAddress address = ecomRefundAddressService.selectByReturnsId(returnsId);
			
			EcomExpressConfirm confirm =  ecomExpressConfirmService.selectByReturnsId(returnsId);
			
			mv.addObject("rerurnsOrder", rerurnsOrder);
			mv.addObject("address", address);
			mv.addObject("confirm", confirm);
		} catch (Exception e) {
			logger.error(" ## 跳转填待商家收货页面出错 ", e);
		}
		return mv;
	}
	
	/**
	 * 退货成功
	 * @param req
	 * @param resp
	 * @return
	 */
	@GetMapping(value = "/returnsSuccess")
	public ModelAndView returnsSuccess(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new  ModelAndView("ecom/returns/returnsSuccess");
		try {
			String returnsId = req.getParameter("returnsId");
			ReturnsOrder rOrder = new ReturnsOrder();
			rOrder.setReturnsId(returnsId);
			
			ReturnsOrder rerurnsOrder = returnsOrderService.getReturnsOrderByItemId(rOrder);
			
			OrderRefund refund = orderRefundService.getOrderRefundByReturnsId(returnsId);
			mv.addObject("rerurnsOrder", rerurnsOrder);
			mv.addObject("refundAmt", NumberUtils.RMBYuanToCent(refund.getRefundAmt()));
		} catch (Exception e) {
			logger.error(" ## 跳转退货成功页面出错 ", e);
		}
		return mv;
	}
	
	/**
	 * 拒绝退货
	 * @param req
	 * @param resp
	 * @return
	 */
	@GetMapping(value = "/refuseReturns")
	public ModelAndView refuseReturns(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new  ModelAndView("ecom/returns/refuseReturns");
		try {
			String returnsId = req.getParameter("returnsId");
			ReturnsOrder rOrder = new ReturnsOrder();
			rOrder.setReturnsId(returnsId);
			
			ReturnsOrder rerurnsOrder = returnsOrderService.getReturnsOrderByItemId(rOrder);
			
			mv.addObject("rerurnsOrder", rerurnsOrder);
		} catch (Exception e) {
			logger.error(" ## 跳转拒绝退货页面出错 ", e);
		}
		return mv;
	}
	
	/**
	 * 取消退货
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value="/cancelReturns")
	public ModelAndView cancelReturns(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mv = new ModelAndView();
		
		String returnsId = req.getParameter("returnsId");
		
		try {
			ReturnsOrder returnsOrder = returnsOrderService.selectByPrimaryKey(returnsId);
			BaseResult<Object> result = returnsGoodsService.handleCancelReturns(returnsOrder);
			
			if (!ExceptionEnum.SUCCESS_CODE.equals(result.getCode())) {
//				mv = new ModelAndView();
			}
			
		} catch (Exception e) {
//			logger.error(" ## 退货绑定物流信息出错 ", e);
		}
		return mv;
	}
	
	
	/**
	 * 查询退货详情
	 * @param req
	 * @param resp
	 * @return
	 */
//	@GetMapping(value="/queryReturns")
//	public BaseResult<Object> queryReturns(HttpServletRequest req, HttpServletResponse resp){
//		ModelAndView mv = new ModelAndView();
//		String returnsId = req.getParameter("returnsId");
//		String applyId = req.getParameter("applyId");
//		
//		BaseResult<Object> result = null;
//		try {
//			result = returnsGoodsService.handleQueryReturnsOrder(applyId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return result;
//		return mv;
//	}
}
