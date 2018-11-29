package com.cn.thinkx.ecom.basics.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.Cart;
import com.cn.thinkx.ecom.basics.order.mapper.CartMapper;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("cartService")
public class CartServiceImpl extends BaseServiceImpl<Cart> implements CartService {

	@Autowired
	private CartMapper cartMapper;
	
	@Override
	public List<Cart> getCartList(Cart cart) {
		return cartMapper.getCartList(cart);
	}

//	@Override
//	public List<Cart> getCartEcomCode() {
//		return cartMapper.getCartEcomCode();
//	}

	/**
	 * 查找购物车的列表数据
	 * @param cardIds
	 * @return
	 * @throws Exception
	 */
	public List<Cart> getCartListByCartIds(List<String> cartIds) throws Exception{
		return cartMapper.getCartListByCartIds(cartIds);
	}
	
	/**
	 * 修改用户的购物车是否被选中
	 * @param memberId
	 * @param isCheck
	 * @throws Exception
	 */
	public void updateCartByMemberIdAndCheck(String memberId,String isCheck)throws Exception{
		 cartMapper.updateCartByMemberIdAndCheck(memberId,isCheck);
	}
	
	/**
	 * 修改购物车货品是否被选中
	 * @param cartId
	 * @param isCheck
	 * @throws Exception
	 */
	public void updateCartByCartIdAndCheck(String cartId,String isCheck)throws Exception{
		cartMapper.updateCartByCartIdAndCheck(cartId,isCheck);
	}


	@Override
	public int addCartByCardId(Cart cart) {
		return cartMapper.addCartByCardId(cart);
	}
	
	/**
	 * 会员购物车列表
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public List<Cart> getListByMemberId(String memberId)throws Exception{
		return cartMapper.getListByMemberId(memberId);
	}
	
	/**
	 * 会员购物车 & 结算列表
	 * @param cart
	 * @return
	 * @throws Exception
	 */
	public List<Cart> getListByMemberIdAndChange(Cart cart)throws Exception{
		return  cartMapper.getListByMemberIdAndChange(cart);
	}
	
	public List<Cart> getListByCartIds(String[] cartIds)throws Exception{
		return  cartMapper.getListByCartIds(cartIds);
	}
	
	 /**
	  * 查找会员的加入购物车的货品信息
	  * @param cart
	  * @return
	  */
	public Cart getCartProductByMemId(Cart cart){
		return cartMapper.getCartProductByMemId(cart);
	}
	
	/**
	 * 添加购物车时，如果存在，数量加1
	 * 
	 * @return
	 */
	public int updateCartByCardId(Cart cart){
		
		return cartMapper.updateCartByCardId(cart);
	}

	@Override
	public int deleteCartByCartId(String cartId) {
		return cartMapper.deleteCartByCartId(cartId);
	}

	/**
	 * 获取会员的购物车数量
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public	int getCartCountByMemberId(String memberId){
		return cartMapper.getCartCountByMemberId(memberId);
	}
}
