package com.cn.thinkx.ecom.front.api.cart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.Esetting;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.service.EsettingService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.basics.order.domain.Cart;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.common.util.StringUtils;
import com.cn.thinkx.ecom.front.api.cart.service.EcomCartService;
import com.cn.thinkx.ecom.front.api.cart.service.PayOrderService;
import com.cn.thinkx.ecom.front.api.member.service.MemberAddressInfService;

@RestController
@RequestMapping(value = "ecom/cart")
public class CartController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private PayOrderService payOrderService;
	
	@Autowired
	private EsettingService esettingService;
	
	@Qualifier("ecomCartService")
	@Autowired
	private EcomCartService ecomCartService;
	

	@Autowired
	private MemberAddressInfService memberAddressService;
	
	@Autowired
	private GoodsProductService goodsProductService;

	@Autowired
	private GoodsService goodsService;
	
	
	/**
	 * 添加购物车（传入参数货品id,数量）
	 * @param req
	 * @param cart
	 * @return
	 */
	@RequestMapping(value = "/addProduct")
	public BaseResult<Object> addProduct(HttpServletRequest request) {
		
		String memberId=StringUtil.nullToString(request.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		
		String productId=request.getParameter("productId");
		String productNum=request.getParameter("productNum");
		if(StringUtils.isEmpty(productNum)){
			productNum="1";
		}
		Cart cart=new Cart();
		cart.setProductId(productId); //货品ID
		cart.setProductNum(productNum); //货品名称
		cart.setMemberId(memberId);
		cart.setIsCheck("0");
		cart.setIsChange("0"); // 状态标识，0：加购物车
		cart.setDataStat("0");
	
		//查询购物车数量
		int cartNum=0;
		BaseResult<Object> result=ecomCartService.addCart(memberId, cart);
			
		if(StringUtil.isNotEmpty(memberId)){
			cartNum=cartService.getCartCountByMemberId(memberId);
		 }
		 result.setObject(cartNum);
		 
		 return result;
	}
	
	
	/**
	 * 立即购买
	 * @param req
	 * @param cart
	 * @return
	 */
	@PostMapping(value = "/purchaseNow")
	public BaseResult<Object>  purchaseNow(HttpServletRequest request) {
		
		String memberId=StringUtil.nullToString(request.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		
		String productId=request.getParameter("productId");
		String productNum=request.getParameter("productNum");
		if(StringUtils.isEmpty(productNum)){
			productNum="1";
		}
		
		Cart cart=new Cart();
		cart.setProductId(productId); //货品ID
		cart.setProductNum(productNum); //货品名称
		cart.setMemberId(memberId);
		cart.setIsCheck("0");
		cart.setIsChange("2"); // 状态标识，2：立即购买
		cart.setDataStat("0");
		BaseResult<Object> result= ecomCartService.addCart(memberId,cart);
		if(!"00".equals(result.getCode())){
			return result;
		}
		return ResultsUtil.success(cart.getCartId());
	}
	
	/**
	 * 购物车选中，全不选,增加商品，减少商品操作
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/cartOperCommit")
	public BaseResult<Object> cartOperCommit(HttpServletRequest request) {
		
		 BaseResult<String>  result=new BaseResult<String>();
		 
		String memberId=StringUtil.nullToString(request.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		
		String checkVal=request.getParameter("checkVal"); //选择事件值
		
		String checkType=request.getParameter("checkType"); // SC,单个复选框，AC:全选 ，SU:修改货品数量,DEL删除
		
		String cartIds=request.getParameter("cartId"); //购物车ID
		String productNun=request.getParameter("productNum"); //购物车货品数量
		

		
		boolean chkboolean=Boolean.parseBoolean(checkVal);
		
		String isCheck="0"; //选择
		
		if(!chkboolean){
			isCheck="1"; //不选择
		}
		
		try {
			
			if("AC".equals(checkType)){//OC:全选
				cartService.updateCartByMemberIdAndCheck(memberId, isCheck);
				
			}else if("SC".equals(checkType) && StringUtils.isNotEmpty(cartIds)){ //SC,单个复选框
				
				if(StringUtils.isNotEmpty(cartIds)){
					cartService.updateCartByCartIdAndCheck(cartIds, isCheck);
				}
			}else if("DEL".equals(checkType) && StringUtils.isNotEmpty(cartIds)){//DEL:删除
				String[] ids=cartIds.split(",");
				for(String cartId:ids){
					cartService.deleteCartByCartId(cartId);
				}
			}else if("VOL".equals(checkType) && StringUtils.isNotEmpty(cartIds)){//VOL:编辑数量
				
				if (Integer.parseInt(productNun) <= 0) {
					return ResultsUtil.error(ExceptionEnum.cartNews.C05.getCode(), ExceptionEnum.cartNews.C05.getMsg());
				}
				
				String[] ids=cartIds.split(",");
				Cart cart=null;
				for(String cartId:ids){
					cart=cartService.selectByPrimaryKey(cartId);
					if(cart !=null){
						GoodsProduct goodsProduct = null;
						Goods goods = null;
						if (!StringUtil.isNullOrEmpty(cart.getProductId())) {
							goodsProduct = goodsProductService.selectByPrimaryKey(cart.getProductId()); // 货品信息
						} else {
							return ResultsUtil.error(ExceptionEnum.cartNews.C01.getCode(), ExceptionEnum.cartNews.C01.getMsg());
						}
						//库存不足
						if(Integer.parseInt(String.valueOf(goodsProduct.getIsStore())) < Integer.parseInt(productNun)){
							return ResultsUtil.error(ExceptionEnum.cartNews.C03.getCode(), ExceptionEnum.cartNews.C03.getMsg());
						}
						if (!StringUtil.isNullOrEmpty(goodsProduct.getGoodsId())) {
							goods = goodsService.selectByPrimaryKey(goodsProduct.getGoodsId()); // 商品信息
						} else {
							return ResultsUtil.error(ExceptionEnum.cartNews.C01.getCode(), ExceptionEnum.cartNews.C01.getMsg());
						}
						if("0".equals(goods.getMarketEnable())){	//0:已下架
							return ResultsUtil.error(ExceptionEnum.cartNews.C04.getCode(), ExceptionEnum.cartNews.C04.getMsg());
						}
						cart.setProductNum(productNun);
						cartService.updateByPrimaryKey(cart); //修改购物车数量
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("**修改用户的购物车状态异常,[{}]**",e);
			return ResultsUtil.error("-1","修改用户的购物车状态异常");
		}

		Map dataMaps=new HashMap<>();
		
		List<Map<String, Object>> cartList=new ArrayList<Map<String, Object>>();
		
		//购物车金额
		int cartsPrice=0;
		try {
				Cart cs=new Cart();
				cs.setMemberId(memberId);
				cs.setIsChange("0");//我的购物车
				//获取我购物车列表
				List<Cart> carts = cartService.getListByMemberIdAndChange(cs);
				
				 cartsPrice=carts.stream()
						.filter(cart ->"0".equals(cart.getIsCheck()) && "1".equals(cart.getMarketEnable())) //获取购物车选中的值 并且没有下架的SPU
						.mapToInt(cart->Integer.parseInt(cart.getGoodsPrice())*Integer.parseInt(cart.getProductNum())).sum();
				
				Map<String, List<Cart>> cartsMap= carts.stream().filter(Cart -> {
		    		//金额转换
					Cart.setGoodsPrice(NumberUtils.RMBCentToYuan(Cart.getGoodsPrice())); 
		    		return true;
				}).collect(Collectors.groupingBy(Cart::getEcomCode));
				
				Map<String,Object> cartsResult=null;
				Esetting setting=null;
				for (Map.Entry<String, List<Cart>> entry : cartsMap.entrySet()) {
					//渠道电商货品
					cartsResult=new HashMap<>();
					cartsResult.put("ecomCarts", entry.getValue());
					//渠道设置
					setting = esettingService.getSettingByEcomCode(entry.getKey());// 检查满减免邮
					cartsResult.put("esetting", setting);
					cartList.add(cartsResult);
				}
		} catch (Exception e) {
			logger.error("**查询购物车总金额异常,[{}]**",e);
			return ResultsUtil.error("-1","用户购物车查询失败");
		}
		
		dataMaps.put("cartsPrice", NumberUtils.RMBCentToYuan(String.valueOf(cartsPrice)));
		dataMaps.put("cartList", cartList);
		return	ResultsUtil.success(dataMaps);
	}
	

	
	/**
	 * 渠道商城我的购物车
	 * @param req
	 * @return
	 */
	@GetMapping("/listCarts")
	public ModelAndView listCarts(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("ecom/cart/listCarts");
		mv.addObject("footType", Constants.FootCodeType.CART.getId());
		List<Map<String, Object>> cartList=new ArrayList<Map<String, Object>>();
			
		//获取会员ID
		String  memberId=StringUtil.nullToString(request.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		//购物车金额
		int cartsPrice=0;
		try {
				Cart cs=new Cart();
				cs.setMemberId(memberId);
				cs.setIsChange("0");//我的购物车
				//获取我购物车列表
				List<Cart> carts = cartService.getListByMemberIdAndChange(cs);
				
				 cartsPrice=carts.stream()
						 .filter(cart ->"0".equals(cart.getIsCheck()) && "1".equals(cart.getMarketEnable())) //获取购物车选中的值 并且没有下架的SPU
						.mapToInt(cart->Integer.parseInt(cart.getGoodsPrice())*Integer.parseInt(cart.getProductNum())).sum();
				
				Map<String, List<Cart>> cartsMap= carts.stream().filter(Cart -> {
		    		//金额转换
					Cart.setGoodsPrice(NumberUtils.RMBCentToYuan(Cart.getGoodsPrice())); 
		    		return true;
				}).collect(Collectors.groupingBy(Cart::getEcomCode));
				
				Map<String,Object> cartsResult=null;
				Esetting setting=null;
				for (Map.Entry<String, List<Cart>> entry : cartsMap.entrySet()) {
					//渠道电商货品
					cartsResult=new HashMap<>();
					cartsResult.put("ecomCarts", entry.getValue());
					//渠道设置
					setting = esettingService.getSettingByEcomCode(entry.getKey());// 检查满减免邮
					cartsResult.put("esetting", setting);
					cartList.add(cartsResult);
				}
		} catch (Exception e) {
			logger.error("###跳转渠道商城分类出错",e);
		}
		
		//查询购物车数量
		int cartNum=0;
		//front web user session
		if(StringUtil.isNotEmpty(memberId)){
			cartNum=cartService.getCartCountByMemberId(memberId);
		}
		
		
		mv.addObject("cartsPrice", NumberUtils.RMBCentToYuan(String.valueOf(cartsPrice)));
		mv.addObject("cartList", cartList);
		mv.addObject("cartNum",cartNum);//购物车数量
		return mv;
	}
	
	
	@RequestMapping(value = "/intoPayOrder")
	public BaseResult<Object> intoPayOrder(HttpServletRequest req) {
		BaseResult<Object> result = payOrderService.payOrder(req);
		return result;
	}
	
	
	/**
	 * 购物车结算页面
	 * @param req
	 * @param cart
	 * @return
	 */
	@RequestMapping(value = "/cartAccounts")
	public ModelAndView cartAccounts(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView("ecom/cart/cartAccounts");
		logger.info("购物车结算-->");
		//获取会员ID
		String  memberId=StringUtil.nullToString(request.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		String isChange=request.getParameter("isChange");
		String cartId=request.getParameter("cartId");
		
		if(StringUtils.isEmpty(isChange)){
			isChange="0";
		}
		String[] cartIds=cartId.split(",");
		
	

		//返回页面Map对象
		Map<String, Map<String,Object>> resutMap=new HashMap<String, Map<String,Object>>();
		
		int ecomOrderAmtTotal=0; //订单总金额
		int ecomFreightAmtTotal=0; //邮费总计
		
		try {
//			Cart cs=new Cart();
//			cs.setMemberId(memberId);
//			cs.setIsChange(isChange);//购物车 & 立即购买
//			cs.setIsCheck("0");//选中购物车
//			cs.setCartId(cartId);//购物车Id
			//获取我购物车列表
			List<Cart> carts = cartService.getListByCartIds(cartIds);
	    	
			//List分组
			Map<String, List<Cart>> cartMaps = carts.stream().filter(Cart->"0".equals(Cart.getIsCheck())).collect(Collectors.groupingBy(t->t.getEcomCode())); 
			
		
			//返回页面Map中的存储Map对象
			Map<String,Object> mapObjects=null;
			
			//查询商城服务设置
			 Esetting  esetting=null;
			for (Map.Entry<String,  List<Cart>> entry : cartMaps.entrySet()) { 
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
					ecomOrderAmt+=ecomFreight; 
				}
				ecomFreightAmtTotal+=ecomFreight;//邮费总计
				ecomOrderAmtTotal+=ecomOrderAmt;	//订单总金额
				
				List<Cart> skuLists=entry.getValue();
				skuLists.stream().filter(Cart -> {
			    		//金额转换
						Cart.setGoodsPrice(NumberUtils.RMBCentToYuan(Cart.getGoodsPrice())); 
			    		return true;
					}).collect(Collectors.groupingBy(t->t.getEcomCode()));
				
				 //页面数据返回
				 mapObjects=new HashMap<>();
				 mapObjects.put("goodsPrice",NumberUtils.RMBCentToYuan(ecomOrderAmt)); //商品金额
				 mapObjects.put("esetting", esetting);
				 mapObjects.put("exemptionState", exemptionState);
				 mapObjects.put("ecomFreight",NumberUtils.RMBCentToYuan(ecomFreight));
				 mapObjects.put("skuLists",skuLists);
				 //
				 resutMap.put(entry.getKey(),mapObjects);
			}
		} catch (Exception e) {
			logger.error("购物车结算：cartAccount error:{}",e);
		}
		//查询会员默认地址
		MemberAddress memberAddress = memberAddressService.getMemberDefAddr(memberId);
		if(memberAddress==null){
			memberAddress=new MemberAddress();
		}
		mv.addObject("memberAddress", memberAddress);
	
		//会员 所有的地址
		MemberAddress memberAddre = new MemberAddress();
		memberAddre.setMemberId(memberId);
		List<MemberAddress> memberAddreList = memberAddressService.getMemberAddressList(memberAddre);
		mv.addObject("memberAddreList", memberAddreList);
		
		mv.addObject("isChange", isChange);
		mv.addObject("cartId", cartId);
		mv.addObject("ecomFreightAmtTotal", NumberUtils.RMBCentToYuan(ecomFreightAmtTotal));
		mv.addObject("ecomOrderAmtTotal",NumberUtils.RMBCentToYuan(ecomOrderAmtTotal));
		mv.addObject("cartList", resutMap);
		return mv;
	}
}
