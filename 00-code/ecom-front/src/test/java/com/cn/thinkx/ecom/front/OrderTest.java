package com.cn.thinkx.ecom.front;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.ecom.FrontApp;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.basics.order.service.ExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.OrderExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.OrderShipService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.you163.api.service.YXOrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrontApp.class) // 指定spring-boot的启动类
public class OrderTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CartService cartService;

	@Autowired
	private PlatfOrderService platfOrderService;

	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Autowired
	private OrderShipService orderShipService;

//	@Autowired
//	private OrderGoodsDetailService orderGoodsDetailService;
	
	@Autowired
	private YXOrderService yxOrderService;

	@Autowired
	private OrderExpressPlatfService orderExpressPlatfService;

	@Autowired
	private ExpressPlatfService expressPlatfService;

	@Test
	public void insert() throws Exception {
		
		
		 PlatfShopOrder  platfShopOrder= platfShopOrderService.selectByPrimaryKey("126600000264");
		 
		 yxOrderService.doCreateOrder(platfShopOrder);
		/*
		 * ExpressPlatf ep = new ExpressPlatf(); ep.setExpressCompanyName("小小");
		 * expressPlatfService.insert(ep);
		 */
		/*
		 * OrderExpressPlatf oep = new OrderExpressPlatf(); oep.setPackId("1");
		 * orderExpressPlatfService.insert(oep);
		 */
		/*
		 * OrderGoodsDetail ogd = new OrderGoodsDetail();
		 * ogd.setShopOrderId("111"); OrderGoodsDetailService.insert(ogd);
		 */
		/*
		 * OrderShip os = new OrderShip(); os.setShipOrderId("111");
		 * os.setShipName("袜子"); orderShipService.insert(os);
		 */
		/*
		 * PlatfShopOrder pso = new PlatfShopOrder(); pso.setOrderId("11");
		 * pso.setMemberId("100008"); pso.setPayStatus("0");
		 * pso.setOrderRemark("wazi"); pso.setPayAmt("10");
		 * pso.setEcomCode("40006006"); pso.setShippingFreightPrice("2");
		 * platfShopOrderService.insert(pso);
		 */
		/*
		 * PlatfOrder po = new PlatfOrder(); po.setMemberId("100008");
		 * po.setPayStatus("0"); po.setOrderPrice("12");
		 * po.setOrderFreightAmt("5"); po.setPayType("HKBPAY");
		 * po.setPayAmt("10"); platfOrderService.insert(po);
		 */
		/*
		 * Cart cart = new Cart(); cart.setProductId("11");
		 * cart.setEcomCode("40006006"); cart.setGoodsType("0");
		 * cart.setGoodsNum("1"); cart.setWeight("10");
		 * cart.setSessionId("738206"); cart.setGoodsPrice("2");
		 * cart.setPreferentialPrice("0"); cart.setMemberId("100008");
		 * cart.setIsCheck("0"); cart.setIsChange("0"); cart.setActivityEndTime(
		 * "2018/04/28 14:09:11"); cart.setActivityId("201804281");
		 * cart.setActivityDetail("袜子"); cartService.insert(cart);
		 * System.out.println(cart.getCartId());
		 */
	}

	@Test
	public void find() throws Exception {
		/*
		 * ExpressPlatf ep = expressPlatfService.selectByPrimaryKey("100000");
		 */
		/*
		 * OrderGoodsDetail ogd =
		 * OrderGoodsDetailService.selectByPrimaryKey("100000");
		 */
		/* OrderShip os = orderShipService.selectByPrimaryKey("100000"); */
		/*
		 * PlatfShopOrder pso =
		 * platfShopOrderService.selectByPrimaryKey("100000");
		 */
		/* PlatfOrder po = platfOrderService.selectByPrimaryKey("100000"); */
		/*
		 * Cart cart = cartService.selectByPrimaryKey("100006");
		 * System.out.println(cart.toString());
		 */
		/* List<Cart> cartList = cartService.getCartList(new Cart()); */
		/*
		 * List<PlatfOrder> platfOrderList =
		 * platfOrderService.getPlatfOrderList(new PlatfOrder());
		 */
		/*
		 * List<PlatfShopOrder> platfShopOrderList =
		 * platfShopOrderService.getPlatfShopOrderList(new PlatfShopOrder());
		 */
		/*
		 * List<OrderShip> orderShipList = orderShipService.getOrderShipList(new
		 * OrderShip());
		 */
		/*
		 * List<OrderGoodsDetail> orderGoodsDetailList =
		 * orderGoodsDetailService.getOrderGoodsDetailList(new
		 * OrderGoodsDetail());
		 */
		/*
		 * List<ExpressPlatf> expressPlatfList =
		 * expressPlatfService.getExpressPlatfList(new ExpressPlatf());
		 */
	}

	@Test
	public void update() throws Exception {
		/*
		 * ExpressPlatf ep = expressPlatfService.selectByPrimaryKey("100000");
		 * ep.setExpressCompanyName("小王");
		 * expressPlatfService.updateByPrimaryKey(ep);
		 */
		/*
		 * OrderGoodsDetail ogd =
		 * OrderGoodsDetailService.selectByPrimaryKey("100002");
		 * ogd.setChangeGoodsName("袜子");
		 * OrderGoodsDetailService.updateByPrimaryKey(ogd);
		 */
		/*
		 * OrderShip os = orderShipService.selectByPrimaryKey("100000");
		 * os.setShipAddr("北京"); orderShipService.updateByPrimaryKey(os);
		 */
		/*
		 * PlatfShopOrder pso =
		 * platfShopOrderService.selectByPrimaryKey("100000");
		 * pso.setPayStatus("1"); platfShopOrderService.updateByPrimaryKey(pso);
		 */
		/*
		 * PlatfOrder po = platfOrderService.selectByPrimaryKey("100000");
		 * po.setPayStatus("1"); po.setOrderPrice("11");
		 * platfOrderService.updateByPrimaryKey(po);
		 */
		/*
		 * Cart cart = cartService.selectByPrimaryKey("100006");
		 * cart.setProductId("12"); cart.setGoodsNum("3");
		 * cart.setGoodsPrice("50"); cartService.updateByPrimaryKey(cart);
		 * System.out.println(cart.toString());
		 */
	}

	@Test
	public void delete() throws Exception {
		/* OrderGoodsDetailService.deleteByPrimaryKey("100002"); */
		/* orderShipService.deleteByPrimaryKey("100000"); */
		/* platfShopOrderService.deleteByPrimaryKey("100000"); */
		/* platfOrderService.deleteByPrimaryKey("100001"); */
		/* cartService.deleteByPrimaryKey("100007"); */
	}
	
	/**
	 * 通过会员id查询所有的订单信息
	 * 
	 */
	@Test
	public void getOrder(){
//		try {
//			PlatfOrder pf = new PlatfOrder();
//			pf.setMemberId("100008");
//			List<PlatfOrder> ppfListf = platfOrderService.getPlatfOrderGoodsByMemberId(pf);
//			logger.info("ppfListf------->"+JSON.toJSONString(ppfListf));
//		} catch (Exception e) {
//			logger.info("## 出错了",e);
//		}
		
	}

}
