package com.cn.thinkx.ecom.basics.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.order.domain.Cart;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface CartMapper extends BaseDao<Cart> {

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
	List<Cart> getCartListByCartIds(List<String> cartIds);

	/**
	 * 添加购物车时，如果存在，数量加1
	 * 
	 * @return
	 */
	int addCartByCardId(Cart cart);
	
	/**
	 * 会员购物车列表
	 * @param memberId
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	 List<Cart> getListByMemberId(@Param("memberId")String memberId);
	 
		/**
		 * 会员购物车结算列表
		 * @param cart
		 * @return
		 * @throws Exception
		 */
	 List<Cart> getListByMemberIdAndChange(Cart cart);
	 
	 List<Cart>  getListByCartIds(String[] cartIds);
	 
	 
	 /**
	  * 查找会员的加入购物车的货品信息
	  * @param cart
	  * @return
	  */
	 Cart getCartProductByMemId(Cart cart);
	 
	 int updateCartByCardId(Cart cart);
	 
	 int updateCartByMemberIdAndCheck(@Param("memberId")String memberId,@Param("isCheck")String isCheck);
	 
	 int updateCartByCartIdAndCheck(@Param("cartId")String cartId, @Param("isCheck")String isCheck);
	 
	 int deleteCartByCartId(@Param("cartId")String cartId);
	 
	 int getCartCountByMemberId(String memberId);
}
