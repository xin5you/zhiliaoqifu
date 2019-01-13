package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * 商品相册表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomGoodsGalleryMapper extends BaseMapper<TbEcomGoodsGallery> {

	/**
	 * 查询商品相册信息（多条件查询）
	 * @param ecomGoodsGallery
	 * @return
	 */
	List<TbEcomGoodsGallery> getGoodsGalleryList(TbEcomGoodsGallery ecomGoodsGallery);

	/**
	 * 根据isDefault查询商品相册信息是否有默认的标识
	 * @param isDefault
	 * @return
	 */
	TbEcomGoodsGallery getGoodsGalleryByIsDefault(String isDefault);

	/**
	 * 根据sort查询商品相册时候已存在该序号值
	 * @param ecomGoodsGallery
	 * @return
	 */
	TbEcomGoodsGallery getGoodsGalleryBySort(TbEcomGoodsGallery ecomGoodsGallery);
}
