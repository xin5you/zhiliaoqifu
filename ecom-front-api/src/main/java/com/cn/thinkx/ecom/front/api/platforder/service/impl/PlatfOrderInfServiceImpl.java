package com.cn.thinkx.ecom.front.api.platforder.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.service.ExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.platforder.service.PlatfOrderInfService;

@Service
public class PlatfOrderInfServiceImpl implements PlatfOrderInfService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlatfOrderService platfOrderService;

	@Autowired
	private PlatfShopOrderService platfShopOrderService;
	
	@Autowired
	private ExpressPlatfService expressPlatfService;

	@Override
	public List<PlatfOrder> getPlatfOrderGoodsByMemberId(PlatfOrder entity) {
		List<PlatfOrder> platfOrderList = platfOrderService.getPlatfOrderGoodsByMemberId(entity);
		for (PlatfOrder platfOrder : platfOrderList) {
			platfOrder.setPayStatusValue(Constants.PlatfOrderPayStat.findByCode(platfOrder.getPayStatus()).getValue());
			platfOrder.setPayAmt(NumberUtils.RMBCentToYuan(platfOrder.getPayAmt()));
			platfOrder.setOrderFreightAmt(NumberUtils.RMBCentToYuan(platfOrder.getOrderFreightAmt()));
			List<PlatfShopOrder> platfShopOrderList = platfOrder.getPlatfShopOrder();
			int count = 0;
			for (PlatfShopOrder platfShopOrder : platfShopOrderList) {
				platfShopOrder
						.setEcomCode(Constants.GoodsEcomCodeType.findByCode(platfShopOrder.getEcomCode()).getValue());
				platfShopOrder.setSubOrderStatusValue(
						Constants.SubOrderStatus.findByCode(platfShopOrder.getSubOrderStatus()).getValue());
				platfShopOrder.setPayAmt(NumberUtils.RMBCentToYuan(platfShopOrder.getPayAmt()));
				platfShopOrder
						.setShippingFreightPrice(NumberUtils.RMBCentToYuan(platfShopOrder.getShippingFreightPrice()));
				List<OrderProductItem> orderProductItemList = platfShopOrder.getOrderProductItems();
				count += orderProductItemList.size();
				platfShopOrder.setOrderProductItemsCount(orderProductItemList.size()); // 货品数量
				double productPriceCount = 0;
				for (OrderProductItem orderProductItem : orderProductItemList) {
					productPriceCount += Double.parseDouble(orderProductItem.getProductPrice())
							* Integer.parseInt(orderProductItem.getProductNum());
					orderProductItem.setProductPrice(NumberUtils.RMBCentToYuan(orderProductItem.getProductPrice()));
					//判断订单中的商品信息是否可以申请售后
					if(Constants.SubOrderStatus.SOS14.getCode().equals(platfShopOrder.getSubOrderStatus())){
						ExpressPlatf ep = new ExpressPlatf();
						ep.setsOrderId(platfShopOrder.getsOrderId());
						ep.setoItemId(orderProductItem.getoItemId());
						List<ExpressPlatf> epList = expressPlatfService.getDeliveryTimeByItemIdAndSorderId(ep);
						if(epList != null && epList.size() > 0){
							orderProductItem.setRefundflag("0");	//可以发起售后
						}else{
							orderProductItem.setRefundflag("1");	//不可以发起售后
						}
					}else{
						orderProductItem.setRefundflag("1");	//不可以发起售后
					}
					
				}
				platfShopOrder.setOrderProductPrice(NumberUtils.RMBCentToYuan(String.valueOf(productPriceCount))); // 每个二级订单得所有商品总价
			}
			platfOrder.setPlatfShopOrderGoodsCount(count); // 订单货品总数
		}
		return platfOrderList;
	}

	@Override
	public BaseResult<Object> deletePlatfOrder(String orderId) {
		try {
			if (StringUtil.isNullOrEmpty(orderId)) {
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			if (platfOrderService.deleteByPrimaryKey(orderId) > 0) {// 删除一级订单
				PlatfShopOrder pso = new PlatfShopOrder();
				pso.setOrderId(orderId);
				List<PlatfShopOrder> psoList = platfShopOrderService.getPlatfShopOrderList(pso);
				for (PlatfShopOrder platfShopOrder : psoList) {
					platfShopOrderService.deleteByPrimaryKey(platfShopOrder.getsOrderId()); // 删除二级订单
				}
			}
		} catch (Exception e) {
			logger.error("## 删除订单失败，orderId:[{}]", orderId, e);
		}
		return ResultsUtil.success();
	}

}
