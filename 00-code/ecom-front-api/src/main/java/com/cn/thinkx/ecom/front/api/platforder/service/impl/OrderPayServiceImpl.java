package com.cn.thinkx.ecom.front.api.platforder.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.basics.goods.service.EsettingService;
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
import com.cn.thinkx.ecom.common.constants.Constants.PlatfOrderPayStat;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.common.util.StringUtils;
import com.cn.thinkx.ecom.front.api.platforder.service.OrderPayService;

@Service
public class OrderPayServiceImpl implements OrderPayService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private EsettingService esettingService;
	
	@Autowired
	private MemberAddressService memberAddressService;
	
	@Autowired
	private PlatfOrderService platfOrderService;
	
	@Autowired
	private PlatfShopOrderService platfShopOrderService;
	
	@Autowired
	private OrderProductItemService orderProductItemService;
	
	@Autowired
	private OrderShipService orderShipService;

	@Override
	public PlatfOrder orderPay(HttpServletRequest req, HttpServletResponse resp) {
		//获取会员ID
		String  memberId=StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		String memberAddressId = req.getParameter("memberAddressId");
		String isChange =req.getParameter("isChange");
		String cartId=req.getParameter("cartId");
		
		if(StringUtils.isEmpty(isChange)){
			isChange="0";
		}
		String[] cartIds=cartId.split(",");
		
		
		int ecomOrderAmtTotal=0; //订单总金额
		int ecomFreightAmtTotal=0; //邮费总计
		PlatfOrder platf = new PlatfOrder();
		PlatfShopOrder shopPlatf = null;
		OrderProductItem item = null;
		OrderShip ship = new OrderShip();
		try {
			String orderPrimary = platfOrderService.getPrimaryKey();
			platf.setOrderId(orderPrimary);
			platf.setMemberId(memberId);
			platf.setPayStatus(PlatfOrderPayStat.PayStat00.getCode());
			
			int i = platfOrderService.insert(platf);
			if (i != 1) {

			}
//			List<Cart> carts = cartService.getListByMemberIdAndChange(parms);
			List<Cart> carts = cartService.getListByCartIds(cartIds);
			//List分组
			Map<String, List<Cart>> cartMaps = carts.stream().filter(cart -> "0".equals(cart.getIsCheck())).collect(Collectors.groupingBy(t -> t.getEcomCode()));
			
			 Esetting  esetting=null;
			for (Map.Entry<String,  List<Cart>> entry : cartMaps.entrySet()) { 
				shopPlatf = new PlatfShopOrder();
				//分组，计算价格总计
				int ecom_goods_price= entry.getValue().stream().mapToInt(cart->Integer.parseInt(cart.getGoodsPrice())*Integer.parseInt(cart.getProductNum())).sum();
				//商城服务设置
				esetting=esettingService.getSettingByEcomCode(entry.getKey());
				//是否包邮
				int exemptionState= ecom_goods_price>=Integer.parseInt(esetting.getFullMoney())?0:1;  //0:包邮，1：不包邮
				
				int ecomOrderAmt=ecom_goods_price; //渠道订单金额
				int  ecomFreight=0; //渠道订单运费
				
				if(exemptionState==1){
					ecomFreight=Integer.parseInt(esetting.getEcomFreight());
				}
				
				shopPlatf.setOrderId(platf.getOrderId());
				shopPlatf.setMemberId(memberId);
				shopPlatf.setEcomCode(entry.getKey());
				shopPlatf.setSubOrderStatus(Constants.SubOrderStatus.SOS00.getCode());
				shopPlatf.setPayAmt(String.valueOf(ecomOrderAmt));
				shopPlatf.setShippingFreightPrice(String.valueOf(ecomFreight));
				
				int p = platfShopOrderService.insert(shopPlatf);//渠道订单
				
				if (p != 1) {

				}
				
				for (Cart cart : entry.getValue()) {
					item = new OrderProductItem();
					item.setsOrderId(shopPlatf.getsOrderId());
					item.setProductId(cart.getProductId());
					item.setCartId(cart.getCartId());
					item.setProductPrice(cart.getGoodsPrice());
					item.setProductNum(cart.getProductNum());
					item.setProductName(cart.getGoodsName());
					int o = orderProductItemService.insert(item);//渠道订单明细
					if (o != 1) {

					}
				}
				
				ecomFreightAmtTotal+=ecomFreight;//邮费总计
				ecomOrderAmtTotal+=ecomOrderAmt;	//订单总金额
			}
			platf.setOrderPrice(String.valueOf(ecomOrderAmtTotal));
			platf.setOrderFreightAmt(String.valueOf(ecomFreightAmtTotal));
			platf.setPayAmt(String.valueOf(ecomOrderAmtTotal+ecomFreightAmtTotal));
			int s = platfOrderService.updateByPrimaryKey(platf);
			
			if(s!=1){
				
			}
			
			MemberAddress addr = memberAddressService.selectByPrimaryKey(memberAddressId);
//			MemberAddress addr = memberAddressService.getMemberDefAddr(memberId);
			
			ship.setOrderId(platf.getOrderId());
			ship.setShipName(addr.getUserName());
			ship.setShipAddr(addr.getAddrDetail());
			ship.setShipZipCode(addr.getAddZip());
			ship.setShipMobile(addr.getMobile());
			ship.setShipTelephone(addr.getTel());
			ship.setShipProvinceId(addr.getProvinceId());
			ship.setShipCityId(addr.getCityId());
			ship.setShipRegionId(addr.getRegionId());
			
			int a = orderShipService.insert(ship);//收货地址
			if(a!=1){
				
			}
			
			for (Cart cart : carts) {
				int c = cartService.deleteCartByCartId(cart.getCartId());//清楚已购商品
				if (c != 1) {

				}
			}
			
			return platf;
		} catch (Exception e) {
			logger.error("生成订单出错",e);
		}		
		return null;
	}
	
	@Override
	public PlatfOrder getPlatfOrderByPrimaryKey(String primaryKey){
		PlatfOrder platf = null;
		try {
			platf = platfOrderService.selectByPrimaryKey(primaryKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return platf;
	}

}
