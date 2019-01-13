package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * 商品详情表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomGoodsDetailMapper extends BaseMapper<TbEcomGoodsDetail> {

	/**
	 * 查询商品详情信息（多条件查询）
	 * @param ecomGoodsDetail
	 * @return
	 */
	List<TbEcomGoodsDetail> getGoodsDetailList(TbEcomGoodsDetail ecomGoodsDetail);

	/**
	 * 根据goodsId查询商品详情信息
	 * @param goodsId
	 * @return
	 */
	TbEcomGoodsDetail getGoodsDetailByGoodsId(String goodsId);
}
