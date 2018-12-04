package com.ebeijia.zl.service.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.user.mapper.UserInfMapper;
import com.ebeijia.zl.service.user.service.IUserInfService;

/**
 *
 * 用户信息 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
public class UserInfServiceImpl extends ServiceImpl<UserInfMapper, UserInf> implements IUserInfService{
	
	@Autowired
	private UserInfMapper userInfMapper;
	
	@Override
	public boolean save(UserInf entity){
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setCreateUser("99999999");
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		entity.setLockVersion(0);
		return super.save(entity);
	}
	
	/**
	 * 
	* @Function: 
	* @Description: 手机号查找用戶信息
	*
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午8:06:50 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public UserInf getUserInfByPhoneNo( String phoneNo, String channel){
		return userInfMapper.getUserInfByPhoneNo(phoneNo, channel);
	}

	/**
	 * 
	* @Function: 
	* @Description: 外部渠道號 查找用戶信息
	*
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午8:07:31 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public UserInf getUserInfByExternalId (String externalId,String channel){
		return userInfMapper.getUserInfByExternalId(externalId, channel);
	}

}
