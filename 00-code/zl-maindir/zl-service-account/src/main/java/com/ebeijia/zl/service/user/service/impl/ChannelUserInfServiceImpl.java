package com.ebeijia.zl.service.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.user.vo.ChannelUserInf;
import com.ebeijia.zl.service.user.mapper.ChannelUserInfMapper;
import com.ebeijia.zl.service.user.service.IChannelUserInfService;

/**
 *
 * 渠道用户信息 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class ChannelUserInfServiceImpl extends ServiceImpl<ChannelUserInfMapper, ChannelUserInf> implements IChannelUserInfService{

	@Override
	public boolean save(ChannelUserInf entity){
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setCreateUser("99999999");
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		entity.setLockVersion(0);
		return super.save(entity);
	}
}
