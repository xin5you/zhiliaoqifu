package com.ebeijia.zl.shop.dao.goods.mapper;

	import com.ebeijia.zl.shop.dao.goods.domain.TbEcomSpecification;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * 商品规格表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomSpecificationMapper extends BaseMapper<TbEcomSpecification> {

	/**
	 * 根据条件查询商品规格信息
	 * @param ecomSpecification
	 * @return
	 */
	List<TbEcomSpecification> getGoodsSpecList(TbEcomSpecification ecomSpecification);
}
