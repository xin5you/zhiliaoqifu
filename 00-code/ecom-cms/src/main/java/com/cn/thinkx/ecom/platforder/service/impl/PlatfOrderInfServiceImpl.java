package com.cn.thinkx.ecom.platforder.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.OrderRefundService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.basics.order.service.SellbackGoodslistService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.PlatfOrderPayStat;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.EcomOrderRefundNews;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.platforder.service.PlatfOrderInfService;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("platfOrderInfService")
public class PlatfOrderInfServiceImpl implements PlatfOrderInfService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlatfOrderService platfOrderService;

	@Autowired
	private PlatfShopOrderService platfShopOrderService;
	
	@Autowired
	private ReturnsOrderService returnsOrderService;

	/**
	 * 严选订单处理service
	 */
	@Autowired
	private YXOrderService yxOrderService;
	
	@Autowired
	private OrderProductItemService orderProductItemService;
	
	@Autowired
	private SellbackGoodslistService sellbackGoodslistService;
	
	@Autowired
	private OrderRefundService orderRefundService;

	@Override
	public PageInfo<PlatfOrder> getPlatforderListPage(int startNum, int pageSize, PlatfOrder entity) {
		List<PlatfOrder> platfOrderList = new ArrayList<PlatfOrder>();
		
		PageHelper.startPage(startNum, pageSize);
		platfOrderList = getPlatfOrderList(entity);
		if (platfOrderList != null && platfOrderList.size() > 0) {
			for (PlatfOrder platfOrder : platfOrderList) {
				if (!StringUtil.isNullOrEmpty(platfOrder.getOrderPrice()))
					platfOrder.setOrderPrice(NumberUtils.RMBCentToYuan("" + platfOrder.getOrderPrice()));
				if (!StringUtil.isNullOrEmpty(platfOrder.getPayAmt()))
					platfOrder.setPayAmt(NumberUtils.RMBCentToYuan("" + platfOrder.getPayAmt()));
				if (!StringUtil.isNullOrEmpty(platfOrder.getOrderFreightAmt()))
					platfOrder.setOrderFreightAmt(NumberUtils.RMBCentToYuan("" + platfOrder.getOrderFreightAmt()));
				platfOrder.setPayStatus(Constants.PlatfOrderPayStat.findByCode(platfOrder.getPayStatus()).getValue());
			}
		}
		PageInfo<PlatfOrder> page = new PageInfo<PlatfOrder>(platfOrderList);
		return page;
	}

	@Override
	public PageInfo<PlatfShopOrder> getPlatfShopOrderListPage(int startNum, int pageSize, PlatfShopOrder entity) {
		PageHelper.startPage(startNum, pageSize);
		List<PlatfShopOrder> platShopOrderList = this.getPlatfShopOrderList(entity, "1");
		PageInfo<PlatfShopOrder> page = new PageInfo<PlatfShopOrder>(platShopOrderList);
		return page;
	}

	@Override
	public PageInfo<PlatfShopOrder> getPlatfShopOrderListsPage(int startNum, int pageSize, PlatfShopOrder entity) {
		PageHelper.startPage(startNum, pageSize);
		List<PlatfShopOrder> platShopOrderList = this.getPlatfShopOrderList(entity, "2");
		PageInfo<PlatfShopOrder> page = new PageInfo<PlatfShopOrder>(platShopOrderList);
		return page;
	}

	public List<PlatfShopOrder> getPlatfShopOrderList(PlatfShopOrder entity, String temp) {
		List<PlatfShopOrder> platShopOrderList = null;
		if ("1".equals(temp)) {
			platShopOrderList = platfShopOrderService.getPlatfShopOrderList(entity);
		} else {
			platShopOrderList = getPlatfShopOrderLists(entity);
		}
		if (platShopOrderList != null && platShopOrderList.size() > 0) {
			for (PlatfShopOrder platfShopOrder : platShopOrderList) {
				platfShopOrder.setEcomCode(Constants.GoodsEcomCodeType.findByCode(platfShopOrder.getEcomCode()).getValue());
				platfShopOrder.setSubOrderStatusValue(Constants.SubOrderStatus.findByCode(platfShopOrder.getSubOrderStatus()).getValue());
				platfShopOrder.setPayAmt(StringUtil.isNullOrEmpty(platfShopOrder.getPayAmt())? "0.00" : NumberUtils.RMBCentToYuan(platfShopOrder.getPayAmt()));
				platfShopOrder.setChnlOrderPrice(StringUtil.isNullOrEmpty(platfShopOrder.getChnlOrderPrice())? "0.00" : NumberUtils.RMBCentToYuan(platfShopOrder.getChnlOrderPrice()));
				platfShopOrder.setChnlOrderPostage(StringUtil.isNullOrEmpty(platfShopOrder.getChnlOrderPostage())? "0.00" : NumberUtils.RMBCentToYuan(platfShopOrder.getChnlOrderPostage()));
				platfShopOrder.setShippingFreightPrice(StringUtil.isNullOrEmpty(platfShopOrder.getShippingFreightPrice())? "0.00" : NumberUtils.RMBCentToYuan(platfShopOrder.getShippingFreightPrice()));
				if (!StringUtil.isNullOrEmpty(platfShopOrder.getPayStatus()))
					platfShopOrder.setPayStatus(Constants.PlatfOrderPayStat.findByCode(platfShopOrder.getPayStatus()).getValue());
			}
		}
		return platShopOrderList;
	}

	/**
	 * 重新下单
	 */
	@Override
	public BaseResult<Object> placeOrder(String sOrderId) {
		PlatfShopOrder platfShopOrder = null;
		PlatfOrder platfOrder = null;
		try {
			if(StringUtil.isNullOrEmpty(sOrderId)){
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			platfShopOrder = platfShopOrderService.selectByPrimaryKey(sOrderId); // 通过二级订单号查询二级订单信息
			platfOrder = platfOrderService.selectByPrimaryKey(platfShopOrder.getOrderId()); // 查询一级订单信息
			// 订单是否已经支付
			if (PlatfOrderPayStat.PayStat02.getCode().equals(platfOrder.getPayStatus())) {
				if (GoodsEcomCodeType.ECOM01.getCode().equals(platfShopOrder.getEcomCode())
						&& Constants.SubOrderStatus.SOS44.getCode().equals(platfShopOrder.getSubOrderStatus())) {
					// 严选下单
					JsonResult jsonResult = yxOrderService.doCreateOrder(platfShopOrder);
					logger.info("******申请严选发货返回数据-->[{}]", jsonResult);
					if ("200".equals(jsonResult.getCode())) {	//如果下单成功，修改状态
//						platfShopOrder.setSubOrderStatus(Constants.SubOrderStatus.SOS10.getCode()); // 等待严选发货
//						platfShopOrder.setRemarks(jsonResult.getMessage());
//						platfShopOrderService.updateByPrimaryKey(platfShopOrder);
						return ResultsUtil.success();
					}else{
						return ResultsUtil.error(ExceptionEnum.placeOrderNews.PON01.getCode(), ExceptionEnum.placeOrderNews.PON01.getMsg());
					}
				}else{
					return ResultsUtil.error(ExceptionEnum.placeOrderNews.PON01.getCode(), ExceptionEnum.placeOrderNews.PON01.getMsg());
				}
			}else{
				return ResultsUtil.error(ExceptionEnum.placeOrderNews.PON01.getCode(), ExceptionEnum.placeOrderNews.PON01.getMsg());
			}
		} catch (Exception e) {
			logger.error("##电商平台网易严选重新下单失败,二级订单号sOrderId：[{}]", platfShopOrder.getsOrderId(), e);
			return ResultsUtil.error(ExceptionEnum.placeOrderNews.PON01.getCode(), ExceptionEnum.placeOrderNews.PON01.getMsg());
		}
	}

	/**
	 * 订单取消
	 */
	@Override
	public BaseResult<Object> cancellationOrder(String sOrderId) {
		try {
			if (!StringUtil.isNullOrEmpty(sOrderId)) { // 二级订单号（渠道订单号）
				PlatfShopOrder pso = platfShopOrderService.selectByPrimaryKey(sOrderId);
				if (!StringUtil.isNullOrEmpty(pso)) {
					if (Constants.SubOrderStatus.SOS44.getCode().equals(pso.getSubOrderStatus())) {
						/** 订单退换货申请表 */
						ReturnsOrder returnsOrder = new ReturnsOrder();
						returnsOrder.setsOrderId(pso.getsOrderId());
						returnsOrder.setMemberId(pso.getMemberId());
						returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS2.getCode()); // 已同意
						returnsOrder.setReturnsType(Constants.ReturnsType.RT6.getCode()); // 平台申请取消
						returnsOrder.setAddTime("");
						returnsOrderService.insert(returnsOrder);

						/** 退款表 */
						orderRefundService.saveOrderRefundInf(returnsOrder.getReturnsId(), pso);
						
						/** 订单货品明细表 */
						OrderProductItem opi = new OrderProductItem();
						opi.setsOrderId(pso.getsOrderId());
						List<OrderProductItem> orderProductItemList = orderProductItemService
								.getOrderProductItemList(opi);

						/** 退货商品表 */
						for (OrderProductItem orderProductItem : orderProductItemList) {
							SellbackGoodslist sg = new SellbackGoodslist();
							sg.setsOrderId(pso.getsOrderId());
							sg.setReturnsId(returnsOrder.getReturnsId());
							sg.setoItemId(orderProductItem.getoItemId());
							sg.setShipNum(orderProductItem.getProductNum());
							sg.setReturnNum(orderProductItem.getProductNum());
							sellbackGoodslistService.insert(sg);
						}
						/** 门店订单表 */
						pso.setSubOrderStatus(Constants.SubOrderStatus.SOS15.getCode()); // 作废
						platfShopOrderService.updateByPrimaryKey(pso);
						
						/** 调用退款 */
						if(!platfShopOrderService.doHkbStoreRefund(pso)){
							return ResultsUtil.error(EcomOrderRefundNews.PON01.getCode(), EcomOrderRefundNews.PON01.getMsg());
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("## 平台取消订单接口失败", e);
			return ResultsUtil.error(ExceptionEnum.placeOrderNews.PON02.getCode(), ExceptionEnum.placeOrderNews.PON02.getMsg());
		}
		return ResultsUtil.success();
	}

	@Override
	public List<PlatfShopOrder> getPlatfShopOrderLists(PlatfShopOrder entity) {
		return platfShopOrderService.getPlatfShopOrderLists(entity);
	}

	@Override
	public List<PlatfOrder> getPlatfOrderList(PlatfOrder po) {
		return platfOrderService.getPlatfOrderList(po);
	}

}
