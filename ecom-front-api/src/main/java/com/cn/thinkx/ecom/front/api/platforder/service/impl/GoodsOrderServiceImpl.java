package com.cn.thinkx.ecom.front.api.platforder.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.cn.thinkx.ecom.basics.order.domain.OrderDeliveryDetailInfo;
import com.cn.thinkx.ecom.basics.order.domain.OrderDeliveryInfo;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.basics.order.service.ExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.OrderRefundService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.basics.order.service.SellbackGoodslistService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.PlatfOrderPayStat;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum.EcomOrderRefundNews;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.platforder.service.GoodsOrderService;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.service.YXOrderService;
import com.cn.thinkx.ecom.you163.api.vo.DeliveryDetailInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.DeliveryInfoVO;
import com.cn.thinkx.ecom.you163.api.vo.order.OrderCancelResult;

/**
 * 电商订单处理service
 * 
 * @author zhuqiuyou
 *
 */
@Service("goodsOrderService")
public class GoodsOrderServiceImpl implements GoodsOrderService {

	private Logger logger = LoggerFactory.getLogger(GoodsOrderServiceImpl.class);

	/**
	 * 严选订单处理service
	 */
	@Autowired
	private YXOrderService yxOrderService;

	/**
	 * 订单包裹信息service
	 */
	@Autowired
	private ExpressPlatfService expressPlatfService;

	/**
	 * 电商平台 二级订单service
	 */
	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Autowired
	private YXOpenApiService yxOpenApiService;

	@Autowired
	private OrderRefundService orderRefundService;

	@Autowired
	private ReturnsOrderService returnsOrderService;

	@Autowired
	private SellbackGoodslistService sellbackGoodslistService;

	@Autowired
	private OrderProductItemService orderProductItemService;

	@Autowired
	private PlatfOrderService platfOrderService;

	/**
	 * 电商系统，外部系统 购物下单操作
	 * 
	 * @param platfOrder
	 *            一级订单平台
	 * @return
	 */
	public void createEcomGoodsOrder(PlatfOrder platfOrder) {

		// 订单是否已经支付
		if (PlatfOrderPayStat.PayStat02.getCode().equals(platfOrder.getPayStatus())) {

			// 查找所有的二级订单
			List<PlatfShopOrder> shopOrderList = platfShopOrderService
					.getPlatfShopOrderByOIdAndStatus(platfOrder.getOrderId(), Constants.SubOrderStatus.SOS00.getCode());

			if (shopOrderList != null && shopOrderList.size() > 0) {

				for (PlatfShopOrder platfShopOrder : shopOrderList) {

					if (GoodsEcomCodeType.ECOM01.getCode().equals(platfShopOrder.getEcomCode())) {
						try {
							// 严选下单
							JsonResult jsonResult = yxOrderService.doCreateOrder(platfShopOrder);
							logger.info("## ******申请严选发货返回数据-->[{}]", jsonResult);



						} catch (Exception ex) {
							logger.error("## 电商系统网易严选购物下单error:[{}]", ex);
						}
					}
				}
			}
		}
	}

	/**
	 * 查询订单的物流轨迹信息
	 * 
	 * @param platfShopOrder
	 * @param packId
	 *            包裹信息ID
	 */
	public OrderDeliveryInfo getOrderExpress(PlatfShopOrder platfShopOrder, String packId) throws Exception {

		ExpressPlatf expressPlatf = expressPlatfService.selectByPrimaryKey(packId);

		OrderDeliveryInfo deliveryInfo = null;

		// 网易严选订单查询
		if (platfShopOrder != null && GoodsEcomCodeType.ECOM01.getCode().equals(platfShopOrder.getEcomCode())) {
			deliveryInfo = new OrderDeliveryInfo();
			deliveryInfo.setsOrderId(platfShopOrder.getsOrderId());
			deliveryInfo.setCompany(expressPlatf.getExpressCompanyName()); // 物流公司名称
			deliveryInfo.setPackageId(packId);
			deliveryInfo.setNumber(expressPlatf.getExpressNo());

			JsonResult jsonResult = yxOpenApiService.handleGetExpress(platfShopOrder.getsOrderId(),
					expressPlatf.getPackageNo(), expressPlatf.getExpressNo(), expressPlatf.getExpressCompanyName());
			logger.info("## 严选查询订单的物流轨迹信息返回数据-->[{}],订单号：[{}],包裹号：[{}]，物流单号：[{}]", jsonResult,
					platfShopOrder.getsOrderId(), expressPlatf.getPackageNo(), expressPlatf.getExpressNo());
			if ("200".equals(jsonResult.getCode())) {
				DeliveryInfoVO deliveryInfoVO = JSONArray.parseObject(jsonResult.getResult().toString(),
						DeliveryInfoVO.class);

				OrderDeliveryDetailInfo orderDeliveryDetailInfo = null;
				List<OrderDeliveryDetailInfo> orderDeliveLists = null;
				// 遍历
				if (deliveryInfoVO != null) {
					orderDeliveLists = new ArrayList<OrderDeliveryDetailInfo>();
					if (deliveryInfoVO.getContent() != null) {
						for (DeliveryDetailInfoVO deliVo : deliveryInfoVO.getContent()) {
							orderDeliveryDetailInfo = new OrderDeliveryDetailInfo();
							orderDeliveryDetailInfo.setTime(deliVo.getTime());
							orderDeliveryDetailInfo.setDesc(deliVo.getDesc());
							orderDeliveLists.add(orderDeliveryDetailInfo);
						}
					}
					deliveryInfo.setContent(orderDeliveLists);
				}
			}
		}
		return deliveryInfo;
	}

	@Override
	public JsonResult cancelOrder(String sOrderId) {
		JsonResult jr = null;
		try {
			if (!StringUtil.isNullOrEmpty(sOrderId)) { // 二级订单号（渠道订单号）
				PlatfShopOrder pso = platfShopOrderService.selectByPrimaryKey(sOrderId);
				if (!StringUtil.isNullOrEmpty(pso)) {
					if (Constants.SubOrderStatus.SOS00.getCode().equals(pso.getSubOrderStatus())
							|| Constants.SubOrderStatus.SOS10.getCode().equals(pso.getSubOrderStatus())) {
						/** 订单退换货申请表 */
						ReturnsOrder returnsOrder = new ReturnsOrder();
						returnsOrder.setsOrderId(pso.getsOrderId());
						returnsOrder.setMemberId(pso.getMemberId());
						returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS0.getCode()); // 未处理
						returnsOrder.setReturnsType(Constants.ReturnsType.RT3.getCode()); // 用户申请取消
						returnsOrder.setAddTime("");
						returnsOrderService.insert(returnsOrder);

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

						if (Constants.GoodsEcomCodeType.ECOM01.getCode().equals(pso.getEcomCode())) {
							// 调用严选【渠道取消订单接口】
							jr = yxOpenApiService.handleCancelOrder(sOrderId);
							logger.info("## 调用严选用户取消订单接口返回值:[{}]----->订单号：[{}]", jr, sOrderId);
							if ("200".equals(jr.getCode())) {
								OrderCancelResult orderCancelResult = JSONArray.parseObject(jr.getResult().toString(),
										OrderCancelResult.class);
								switch (orderCancelResult.getCancelStatus()) {
								case 0: // 0:不允许取消
									/** 门店订单表 */
									pso.setSubOrderStatus(Constants.SubOrderStatus.SOS28.getCode()); // 取消被拒
									if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
										/** 订单退换货申请表 */
										returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS1.getCode()); // 审核状态：已拒绝
										returnsOrderService.updateByPrimaryKey(returnsOrder);
									}
									break;
								case 1: // 允许取消
									/** 门店订单表 */
									pso.setSubOrderStatus(Constants.SubOrderStatus.SOS27.getCode()); // 已取消
									if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
										// 判断一级订单下的所有二级订单是否都是已取消状态
										List<PlatfShopOrder> psoList = platfShopOrderService
												.getPlatfShopOrderByOIdAndNotStatus(pso.getOrderId(),
														Constants.SubOrderStatus.SOS27.getCode());
										logger.info("## 查看一级订单下的所有二级订单是已取消状态的 个数：psoList.size():[{}]", psoList.size());
										if (psoList.size() == 0) {
											PlatfOrder po = new PlatfOrder();
											po.setOrderId(pso.getOrderId());
											po.setPayStatus(Constants.PlatfOrderPayStat.PayStat08.getCode()); // 已取消
											platfOrderService.updateByPrimaryKey(po);
										}
										/** 订单退换货申请表 */
										returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS2.getCode()); // 审核状态：已同意
										if (returnsOrderService.updateByPrimaryKey(returnsOrder) > 0) {
											/** 退款表 */
											orderRefundService.saveOrderRefundInf(returnsOrder.getReturnsId(), pso);
											/** 调用退款 */
											if (!platfShopOrderService.doHkbStoreRefund(pso)) {
												jr.setCode(99);
												jr.setMessage(EcomOrderRefundNews.PON01.getMsg());
											}
										}
									}
									break;
								case 2: // 待审核
									pso.setSubOrderStatus(Constants.SubOrderStatus.SOS26.getCode()); // 申请取消待审核
									platfShopOrderService.updateByPrimaryKey(pso);
									break;
								default:
									break;
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("## 渠道取消订单接口失败", e);
		}

		return jr;
	}

	@Override
	public JsonResult confirmOrder(String sOrderId) {
		JsonResult jr = null;
		try {
			if (!StringUtil.isNullOrEmpty(sOrderId)) {
				PlatfShopOrder pso = platfShopOrderService.selectByPrimaryKey(sOrderId); // 通过二级订单号查询订单信息
				if (!StringUtil.isNullOrEmpty(pso)) {
					List<ExpressPlatf> expressPlatfList = expressPlatfService.getOrderExpressPlatfBySOrderId(sOrderId); // 获取该订单下得所有包裹
					if (Constants.GoodsEcomCodeType.ECOM01.getCode().equals(pso.getEcomCode())) {
						// 点击确认收货按钮，默认是把该二级订单下的所有包裹修改成已完成状态
						for (ExpressPlatf expressPlatf : expressPlatfList) {
							// 调用严选订单确认收货接口
							jr = yxOpenApiService.handleConfirmOrder(sOrderId, expressPlatf.getPackageNo(),
									DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
							logger.info("## 用户确认收货，调用严选确认收货接口返回值:[{}]----->订单号:[{}]", jr, sOrderId);
							if ("200".equals(jr.getCode())) {
								// 修改订单包裹物流信息的状态
								expressPlatf.setPackageStat(Constants.PackageStatus.PACK_STAT90.getCode()); // 已完成
								expressPlatf.setIsSign(Constants.IsSign.IS1.getCode()); // 已经签收
								expressPlatf
										.setSignTime(DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
								expressPlatfService.updateByPrimaryKey(expressPlatf);
								// 修改电商平台门店订单状态
								pso.setSubOrderStatus(Constants.SubOrderStatus.SOS14.getCode()); // 已完成
								if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
									// 判断一级订单下的所有二级订单是否都是已完成状态
									List<PlatfShopOrder> psoList = platfShopOrderService
											.getPlatfShopOrderByOIdAndNotStatus(pso.getOrderId(),
													Constants.SubOrderStatus.SOS14.getCode());
									if (psoList.size() == 0) {
										PlatfOrder po = new PlatfOrder();
										po.setOrderId(pso.getOrderId());
										po.setPayStatus(Constants.PlatfOrderPayStat.PayStat09.getCode()); // 已完成
										platfOrderService.updateByPrimaryKey(po);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("## 用户确认收货失败,二级订单号sOrderId：[{}]", sOrderId, e);
			jr.setCode(500);
		}
		return jr;
	}

	@Override
	public void doRefreshPlatOrderInf() {
		try {
			List<ExpressPlatf> epList = expressPlatfService.getExpressPlatfBySignTimeJob();
			for (ExpressPlatf expressPlatf : epList) {
				if (Constants.GoodsEcomCodeType.ECOM01.getCode().equals(expressPlatf.getEcomCode())) { // 严选的包裹信息
					// 调用严选订单确认收货接口
					JsonResult jr = yxOpenApiService.handleConfirmOrder(expressPlatf.getsOrderId(),
							expressPlatf.getPackageNo(),
							DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
					logger.info("## 定时刷新确认收货，调用严选确认收货接口返回值:[{}]----->订单号：[{}]", jr, expressPlatf.getsOrderId());
					if ("200".equals(jr.getCode())) {
						expressPlatf.setPackageStat(Constants.PackageStatus.PACK_STAT90.getCode()); // 已完成
						expressPlatf.setIsSign(Constants.IsSign.IS1.getCode()); // 已经签收
						expressPlatf.setSignTime(DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
						if (expressPlatfService.updateByPrimaryKey(expressPlatf) > 0) {
							PlatfShopOrder pso = new PlatfShopOrder();
							pso.setsOrderId(expressPlatf.getsOrderId());
							// 修改电商平台门店订单状态
							pso.setSubOrderStatus(Constants.SubOrderStatus.SOS14.getCode()); // 已完成
							if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
								pso = platfShopOrderService.selectByPrimaryKey(expressPlatf.getsOrderId());
								if (!StringUtil.isNullOrEmpty(pso.getOrderId())) {
									PlatfOrder po = new PlatfOrder();
									po.setOrderId(pso.getOrderId());
									po.setPayStatus(Constants.PlatfOrderPayStat.PayStat09.getCode()); // 已完成
									platfOrderService.updateByPrimaryKey(po);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("## 定时刷新需要确认收货的数据异常", e);
		}

	}
}
