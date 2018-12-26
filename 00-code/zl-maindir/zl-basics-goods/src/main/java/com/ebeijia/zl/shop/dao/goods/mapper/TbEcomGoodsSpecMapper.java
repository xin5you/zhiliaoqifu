package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsSpec;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

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
	 * 	<select id="getGoodsSpecByGoodsId" parameterType="java.lang.String"  resultType="GoodsSpec">
	 *
	 * 		     SELECT DISTINCT s.SPEC_ID,
	 * 				s.SPEC_NAME,
	 * 				sv.SPEC_VALUE_ID,
	 * 				sv.SPEC_VALUE,
	 * 				sv.SPEC_IMAGE ,
	 * 				gs.GOODS_ID
	 * 			FROM
	 * 				TB_ECOM_SPECIFICATION s,
	 * 				TB_ECOM_SPEC_VALUES sv,
	 * 				TB_ECOM_GOODS_SPEC gs
	 * 		WHERE s.SPEC_ID = sv.SPEC_ID
	 * 			AND gs.SPEC_VALUE_ID = sv.SPEC_VALUE_ID
	 * 			AND gs.GOODS_ID=#{goodsId}
	 * 			ORDER BY s.SPEC_ID
	 * 	</select>
	 *
	 */
}
