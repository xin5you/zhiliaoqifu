package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsSpec;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * SKU规格对照表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomGoodsSpecMapper extends BaseMapper<TbEcomGoodsSpec> {

	/**
	 * 根据goodsId和productId查询商品规格关联表信息
	 * @param ecomGoodsSpec
	 * @return
	 */
	TbEcomGoodsSpec getGoodsSpecByGoodsIdAndProductId(TbEcomGoodsSpec ecomGoodsSpec);

	/**
	 * 根据productId查询商品规格关联表信息
	 * @param productId
	 * @return
	 */
	TbEcomGoodsSpec getGoodsSpecByProductId(String productId);

	/**
	 * 根据goodsId查询商品规格关联表信息
	 * @param goodsId
	 * @return
	 */
	List<TbEcomGoodsSpec> getGoodsSpecByGoodsId(String goodsId);
}
