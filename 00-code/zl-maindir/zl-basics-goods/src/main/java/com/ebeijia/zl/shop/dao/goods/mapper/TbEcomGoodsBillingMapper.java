package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsBilling;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

/**
 *
 * 商品专项账户关联表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomGoodsBillingMapper extends BaseMapper<TbEcomGoodsBilling> {

	/**
	 * 根据goodsId查询商品关联表信息
	 * @param goodsId
	 * @return
	 */
	TbEcomGoodsBilling getGoodsBillingByGoodsId(String goodsId);

}
