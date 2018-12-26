package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.GoodsProduct;
	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * 货品(SKU)信息表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomGoodsProductMapper extends BaseMapper<TbEcomGoodsProduct> {

	/**
	 * 查询某个商品的货品
	 *
	 * @param goodsId
	 * @return
	 */
	public List<GoodsProduct> getProductlistByGoodsId(String goodsId);

	/**
	 * 库存列表
	 *
	 * @param pro
	 * @return
	 */
	List<GoodsProduct>getInventoryList(GoodsProduct pro);


	GoodsProduct getGoodsProductByPrimaryKey(String primaryKey);
}
