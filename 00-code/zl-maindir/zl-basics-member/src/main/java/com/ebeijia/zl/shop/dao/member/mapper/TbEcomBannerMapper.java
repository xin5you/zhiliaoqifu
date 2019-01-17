package com.ebeijia.zl.shop.dao.member.mapper;

	import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * Banner信息 Mapper 接口
 *
 * @User J
 * @Date 2018-12-07
 */
@Mapper
public interface TbEcomBannerMapper extends BaseMapper<TbEcomBanner> {

	/**
	 * 根据条件查询banner信息
	 * @param entity
	 * @return
	 */
	List<TbEcomBanner> getBannerList(TbEcomBanner entity);

	/**
	 * 根据排序号查询banner信息
	 * @param sort
	 * @return
	 */
	TbEcomBanner getBannerBySort(Integer sort);
}
