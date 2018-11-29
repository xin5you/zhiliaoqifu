package com.cn.thinkx.ecom.front.api.cart.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.basics.order.domain.Cart;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.cart.service.EcomCartService;

@Service("ecomCartService")
public class EcomCartServiceImpl implements EcomCartService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CartService cartService;

	@Autowired
	private GoodsProductService goodsProductService;

	@Autowired
	private GoodsService goodsService;

	/**
	 * 
	 */
	public BaseResult<Object> addCart(String membeId, Cart cart) {
		if (StringUtil.isNullOrEmpty(cart))
			return ResultsUtil.error(ExceptionEnum.cartNews.C02.getCode(), ExceptionEnum.cartNews.C02.getMsg());

		if (Integer.parseInt(cart.getProductNum()) < 0) {
			return ResultsUtil.error(ExceptionEnum.cartNews.C05.getCode(), ExceptionEnum.cartNews.C05.getMsg());
		}
		GoodsProduct goodsProduct = null;
		Goods goods = null;
		try {
			if (!StringUtil.isNullOrEmpty(cart.getProductId())) {
				goodsProduct = goodsProductService.selectByPrimaryKey(cart.getProductId()); // 货品信息
			} else {
				return ResultsUtil.error(ExceptionEnum.cartNews.C01.getCode(), ExceptionEnum.cartNews.C01.getMsg());
			}
			if (!StringUtil.isNullOrEmpty(goodsProduct.getGoodsId())) {
				goods = goodsService.selectByPrimaryKey(goodsProduct.getGoodsId()); // 商品信息
			} else {
				return ResultsUtil.error(ExceptionEnum.cartNews.C01.getCode(), ExceptionEnum.cartNews.C01.getMsg());
			}
			if ("0".equals(goods.getMarketEnable())) { // 0:已下架
				return ResultsUtil.error(ExceptionEnum.cartNews.C04.getCode(), ExceptionEnum.cartNews.C04.getMsg());
			}
			
			if ("1".equals(goodsProduct.getProductEnable())){ // 0:可售，1：不可售
				return ResultsUtil.error(ExceptionEnum.cartNews.C04.getCode(), ExceptionEnum.cartNews.C04.getMsg());
			}
			
			// 立即购买，是insert购物车中
			if ("2".equals(cart.getIsChange())) {
				// 库存不足
				if (Integer.parseInt(String.valueOf(goodsProduct.getIsStore())) < Integer.parseInt(String.valueOf(cart.getProductNum()))) {
					return ResultsUtil.error(ExceptionEnum.cartNews.C03.getCode(), ExceptionEnum.cartNews.C03.getMsg());
				}
				cart.setEcomCode(goodsProduct.getEcomCode());
				cart.setProductType("0"); // 0代表商品
				cart.setProductNum(cart.getProductNum()); // 数量
				cart.setWeight(goods.getWeight());// 重量
				cart.setProductPrice(goodsProduct.getGoodsPrice()); // 商品价格
				cart.setMemberId(membeId);
				cartService.insert(cart);
			} else {
				Cart cart1 = cartService.getCartProductByMemId(cart);
				String productNum = "";
				if (cart1 != null) {
					productNum = String.valueOf(Integer.parseInt(cart.getProductNum()) + Integer.parseInt(cart1.getProductNum()));
				} else {
					productNum = cart.getProductNum();
				}
				// 库存不足
				if (Integer.parseInt(String.valueOf(goodsProduct.getIsStore())) < Integer.parseInt(String.valueOf(productNum))) {
					return ResultsUtil.error(ExceptionEnum.cartNews.C03.getCode(), ExceptionEnum.cartNews.C03.getMsg());
				}
				if (cart1 != null) {
					cart1.setProductNum(String.valueOf(Integer.parseInt(cart.getProductNum()) + Integer.parseInt(cart1.getProductNum()))); // 数量
					cartService.updateCartByCardId(cart1);
				} else {
					cart.setEcomCode(goodsProduct.getEcomCode());
					cart.setProductType("0"); // 0代表商品
					cart.setProductNum(cart.getProductNum()); // 数量
					cart.setWeight(goods.getWeight());// 重量
					cart.setProductPrice(goodsProduct.getGoodsPrice()); // 商品价格
					cart.setMemberId(membeId);
					cartService.insert(cart);
				}
			}
		} catch (Exception e) {
			logger.error("## 添加购物车失败，传入参数：[{}],------>会员id：[{}],异常原因：[{}]", cart.toString(), membeId, e);
			return ResultsUtil.error(ExceptionEnum.cartNews.C02.getCode(), ExceptionEnum.cartNews.C02.getMsg());
		}
		return ResultsUtil.success();
	}
}
