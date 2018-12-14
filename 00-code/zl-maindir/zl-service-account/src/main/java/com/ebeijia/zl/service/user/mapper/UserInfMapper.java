package com.ebeijia.zl.service.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.user.vo.UserInf;

/**
 *
 * 用户信息 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Mapper
public interface UserInfMapper extends BaseMapper<UserInf> {

	/**
	 * 根据手机号查找用户信息 适用于用户注册
	 * @param phoneNo 适用于用户注册
	 * @param userName
	 * @return
	 */
	UserInf getUserInfByPhoneNo(@Param("phoneNo") String phoneNo, @Param("channel") String channel);
	
	/**
	 * 
	* @Function: UserInfMapper.java
	* @Description: 外部渠道号查询用户信息
	*
	* @param:externalId
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 上午9:29:14 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	UserInf getUserInfByExternalId (@Param("externalId") String externalId, @Param("channel") String channel);
	
	
	/**
	 * 根据手机号查找用户信息
	* @Description: 该函数的功能描述
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 上午10:38:23 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	UserInf getUserInfByMobilePhone(@Param("phoneNo") String phoneNo);
}
