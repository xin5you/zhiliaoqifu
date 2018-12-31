package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.Goods;
	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;
	import org.apache.ibatis.annotations.Param;

	import java.util.List;

/**
 *
 * 商品表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomGoodsMapper extends BaseMapper<TbEcomGoods> {
	// selectGoodsAndDefProductByGoodId selectGoodsByProductId

	/**
	 * 查看商品信息列表
	 * @param goods
	 * @return
	 */
	List<Goods> getGoodsList(Goods goods);


	/**
	 * 查看商品信息
	 * @param goods
	 * @return
	 */
	Goods getGoods(Goods goods);

	/**
	 * 查看当前分类下的所有商品
	 * @param catId 二级分类id
	 * @return
	 */
	List<Goods> getGoodsByCategory(Goods goods);

	/**
	 * 查找商品信息包含默认sku的信息
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	Goods selectGoodsAndDefProductByGoodId(String goodsId) throws Exception;

	Goods selectGoodsByProductId(String productId);

	/**
	 * 查询商品（Spu）信息列表
	 * @param ecomGoods
	 * @return
	 */
	List<TbEcomGoods> getGoodsInfList(TbEcomGoods ecomGoods);

}
