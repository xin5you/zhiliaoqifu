package com.ebeijia.zl.shop.dao.member.mapper;

	import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * 会员信息表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomMemberMapper extends BaseMapper<TbEcomMember> {

	/**
	 * 查询会员信息（高级查询）
	 * @param ecomMember
	 * @return
	 */
	List<TbEcomMember> getMemberInfList(TbEcomMember ecomMember);
}
