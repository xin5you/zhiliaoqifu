package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecValues;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

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
	 *     <select id="getGoodsSpecByGoodsId" parameterType="java.lang.String" resultType="SpecValues">
	 *         SELECT
	 *         sv.SPEC_VALUE_ID,
	 *         sv.SPEC_ID,
	 *         sv.SPEC_VALUE,
	 *         sv.SPEC_IMAGE,
	 *         sv.SPEC_ORDER,
	 *         sv.SPEC_TYPE,
	 *         sv.INHERENT_OR_ADD,
	 *         gss.PRODUCT_ID PRODUCT_ID
	 *         FROM
	 *         TB_ECOM_GOODS_SPEC gss
	 *         INNER JOIN TB_ECOM_SPEC_VALUES sv
	 *         ON gss.SPEC_VALUE_ID = sv.SPEC_VALUE_ID
	 *         WHERE gss.GOODS_ID=#{goodsId}
	 *         ORDER BY gss.id DESC
	 *     </select>
	 */
}
