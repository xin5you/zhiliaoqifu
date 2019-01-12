package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecValues;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * 商品规格明细 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomSpecValuesMapper extends BaseMapper<TbEcomSpecValues> {

	/**
	 * 查询商品规格值列表（高级查询）
	 * @param entity
	 * @return
	 */
	List<TbEcomSpecValues> getGoodsSpecValuesList(TbEcomSpecValues entity);

	/**
	 * 根据specOrder查询规格值信息
	 * @param entity
	 * @return
	 */
	TbEcomSpecValues getGoodsSpecValueBySpecOrder(TbEcomSpecValues entity);

	/**
	 * 根据SpecValueName查询规格值信息
	 * @param entity
	 * @return
	 */
	TbEcomSpecValues getGoodsSpecValueBySpecValueName(TbEcomSpecValues entity);
}
