package com.cn.thinkx.ecom.front.api.cart.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.basics.goods.service.EsettingService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.basics.member.service.MemberAddressService;
import com.cn.thinkx.ecom.basics.order.domain.Cart;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.OrderShipService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.front.api.cart.service.PayOrderService;

@Service
public class PayOrderServiceImpl implements PayOrderService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderProductItemService orderProductItemService;

	@Autowired
	private OrderShipService orderShipService;

	@Autowired
	private PlatfOrderService platfOrderService;

	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Autowired
	private EsettingService settingService;

	@Autowired
	private MemberAddressService memberAddressService;

	@Autowired
	private GoodsProductService goodsProductService;

	@Autowired
	private GoodsService goodsService;

	@Override
	public BaseResult payOrder(HttpServletRequest req) {
		BaseResult<Object> res = new BaseResult<Object>();
		res.setCode(ExceptionEnum.ERROR_CODE);
		res.setMsg(ExceptionEnum.ERROR_MSG);

		PlatfOrder platfOrder = null;
		PlatfShopOrder platfShopOrder = null;
		OrderShip orderShip = null;
		OrderProductItem orderProductItem = null;

		String memberId = req.getParameter("memberId");// 会员id 100008
		Integer orderAmt = 0;// 订单金额
		Integer freightAmt = 0;// 配送金额
		String carIds = req.getParameter("carIds");// 100014,100013,100012
		String addressId = req.getParameter("addressId");// 收获地址code 100006

		try {
			String orderPrimary = platfOrderService.getPrimaryKey();

			List<Cart> list = cartService.getCartListByCartIds(Arrays.asList(carIds.split(",")));
			Map<String, List<Cart>> map = list.stream().collect(Collectors.groupingBy(Cart::getEcomCode));

			for (Map.Entry<String, List<Cart>> entry : map.entrySet()) {
				Esetting setting = settingService.getSettingByEcomCode(entry.getKey());// 检查满减免邮

				Integer shippingFreightPrice = 0;

				Integer payAmt = entry.getValue().stream()
						.mapToInt(e -> Integer.parseInt(e.getGoodsPrice()) * Integer.parseInt(e.getProductNum())).sum();
				if (payAmt < Integer.parseInt(setting.getFullMoney())) {
					shippingFreightPrice = Integer.parseInt(setting.getEcomFreight());
				}
				orderAmt += payAmt;
				freightAmt += shippingFreightPrice;

				platfShopOrder = new PlatfShopOrder();
				platfShopOrder.setOrderId(orderPrimary);
				platfShopOrder.setMemberId(memberId);
				platfShopOrder.setSubOrderStatus(Constants.SubOrderStatus.SOS00.getCode());
				platfShopOrder.setPayAmt(payAmt.toString());
				platfShopOrder.setShippingFreightPrice(shippingFreightPrice.toString());
				platfShopOrder.setEcomCode(entry.getKey());

				int o = platfShopOrderService.insert(platfShopOrder);// 添加二级订单
				if (o != 1) {
					return res;
				}
				/** 添加订单明细 */

				for (Cart c : entry.getValue()) {
					orderProductItem = new OrderProductItem();
					orderProductItem.setsOrderId(platfShopOrder.getsOrderId());
					orderProductItem.setProductId(c.getProductId());
					orderProductItem.setCartId(c.getCartId());
					orderProductItem.setProductPrice(c.getGoodsPrice());
					orderProductItem.setProductNum(c.getProductNum());
					orderProductItem.setProductName(c.getGoodsName());

					int i = orderProductItemService.insert(orderProductItem);
					if (i != 1) {
						return res;
					}
				}
			}

			/** 添加总订单 */
			platfOrder = new PlatfOrder();
			platfOrder.setOrderId(orderPrimary);
			platfOrder.setMemberId(memberId);
			platfOrder.setPayStatus("0");
			platfOrder.setOrderPrice(orderAmt.toString());
			platfOrder.setOrderFreightAmt(freightAmt.toString());

			int i = platfOrderService.insert(platfOrder);

			if (i != 1) {
				return res;
			}

			// 查询会员的默认收货地址
			MemberAddress address = memberAddressService.selectByPrimaryKey(addressId);

			/** 添加订单收货地址 */
			orderShip = new OrderShip();
			orderShip.setOrderId(orderPrimary);
			orderShip.setShipName(address.getUserName());
			orderShip.setShipAddr(address.getAddrDetail());
			orderShip.setShipZipCode(address.getAddZip());
			orderShip.setShipMobile(address.getMobile());
			orderShip.setShipTelephone(address.getTel());
			orderShip.setShipProvinceId(address.getProvinceId());
			orderShip.setShipCityId(address.getCityId());
			orderShip.setShipRegionId(address.getRegionId());

			int s = orderShipService.insert(orderShip);
			if (s != 1) {
				return res;
			}

			res.setCode(ExceptionEnum.SUCCESS_CODE);
			res.setMsg(ExceptionEnum.SUCCESS_MSG);
		} catch (Exception e) {
			logger.error("下单出错啦----->", e);
		}

		return res;
	}
}
