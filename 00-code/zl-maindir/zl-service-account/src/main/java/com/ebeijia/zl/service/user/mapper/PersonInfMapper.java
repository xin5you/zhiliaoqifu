package com.ebeijia.zl.service.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.user.vo.PersonInf;

/**
 *
 * 用户个人信息 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Mapper
public interface PersonInfMapper extends BaseMapper<PersonInf> {
	
	
	/**
	 * 获取用户个人信息
	 * 
	 * @param externalId 外部用户Id
	 * @return
	 */
	PersonInf getPersonInfByExternalId(@Param("externalId") String externalId, @Param("channel") String channel);

	/**
	 * 根据手机号查找个人信息 适用于用户注册
	 * @param phoneNo
	 * @param channel 渠道标识
	 * @return
	 */
	PersonInf getPersonInfByPhoneNo(@Param("phoneNo") String phoneNo, @Param("channel") String channel);
}
