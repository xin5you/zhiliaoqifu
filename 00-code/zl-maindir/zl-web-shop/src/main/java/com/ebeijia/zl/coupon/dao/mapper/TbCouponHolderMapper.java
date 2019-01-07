package com.ebeijia.zl.coupon.dao.mapper;

	import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;
	import org.apache.ibatis.annotations.Select;

	import java.util.List;

/**
 *
 * 记录用户持有的代金券商品 Mapper 接口
 *
 * @User J
 * @Date 2019-01-05
 */
@Mapper
public interface TbCouponHolderMapper extends BaseMapper<TbCouponHolder> {

	@Select("SELECT COUNT(*) amount,h.coupon_id, h.member_id,h.coupon_code,h.price,h.b_id,h.coupon_name,h.active_start_date,h.active_end_date,h.create_time,p.coupon_desc,p.icon_image,p.tag_unit,p.tag_amount\n" +
			"FROM tb_coupon_holder h\n" +
			"LEFT JOIN tb_coupon_product p ON p.coupon_code=h.coupon_code\n" +
			"WHERE h.member_id='${memberId}' and h.b_id='${bId}' and h.trans_stat=0 AND h.data_stat=0\n" +
			"GROUP BY h.coupon_code,h.price;")
	List<TbCouponHolder> listCouponHolder(TbCouponHolder holder);


	@Select("SELECT COUNT(*) amount,h.coupon_id, h.member_id,h.coupon_code,h.price,h.b_id,h.coupon_name,h.active_start_date,h.active_end_date,h.create_time,p.coupon_desc,p.icon_image,p.tag_unit,p.tag_amount\n" +
			"FROM tb_coupon_holder h\n" +
			"LEFT JOIN tb_coupon_product p ON p.coupon_code=h.coupon_code\n" +
			"WHERE h.member_id='${memberId}' and h.coupon_code='${couponCode}' and h.price='${price}' and h.trans_stat=0 AND h.data_stat=0\n" +
			"GROUP BY h.coupon_code;")
	TbCouponHolder getCouponHolder(TbCouponHolder holder);

}
