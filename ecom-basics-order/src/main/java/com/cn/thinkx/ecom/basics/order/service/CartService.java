package com.cn.thinkx.ecom.basics.order.service;

import java.util.List;


import com.cn.thinkx.ecom.basics.order.domain.Cart;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface CartService extends BaseService<Cart> {

	List<Cart> getCartList(Cart cart);

	/**
	 * 查询购物车货品所属门店
	 * 
	 * @return
	 */
	// List<Cart> getCartEcomCode();

	/**
	 * 查找购物车的列表数据
	 * 
	 * @param cardIds
	 * @return
	 * @throws Exception
	 */
	List<Cart> getCartListByCartIds(List<String> cartIds) throws Exception;
	
	/**
	 * 修改用户的购物车是否被选中
	 * @param memberId
	 * @param isCheck
	 * @throws Exception
	 */
	void updateCartByMemberIdAndCheck(String memberId,String isCheck)throws Exception;
	
	/**
	 * 修改购物车货品是否被选中
	 * @param cartId
	 * @param isCheck
	 * @throws Exception
	 */
	void updateCartByCartIdAndCheck(String cartId,String isCheck)throws Exception;

	/**
	 * 添加购物车时，如果存在，数量加1
	 * 
	 * @return
	 */
	int addCartByCardId(Cart cart);
	
	/**
	 * 会员购物车列表
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	List<Cart> getListByMemberId(String memberId)throws Exception;
	
	/**
	 * 会员购物车 & 结算列表
	 * @param cart
	 * @return
	 * @throws Exception
	 */
	List<Cart> getListByMemberIdAndChange(Cart cart)throws Exception;
	

	List<Cart> getListByCartIds(String[] cartIds)throws Exception;
	
	 /**
	  * 查找会员的加入购物车的货品信息
	  * @param cart
	  * @return
	  */
	Cart getCartProductByMemId(Cart cart);
	
	/**
	 * 添加购物车时，如果存在，数量加1
	 * 
	 * @return
	 */
	int updateCartByCardId(Cart cart);
	
	int deleteCartByCartId(String cartId);
	
	/**
	 * 获取会员的购物车数量
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	int getCartCountByMemberId(String memberId);
}
