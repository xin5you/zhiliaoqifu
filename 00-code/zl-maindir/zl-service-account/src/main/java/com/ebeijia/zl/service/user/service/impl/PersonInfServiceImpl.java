package com.ebeijia.zl.service.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.service.user.mapper.PersonInfMapper;
import com.ebeijia.zl.service.user.service.IPersonInfService;

/**
 *
 * 用户个人信息 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
public class PersonInfServiceImpl extends ServiceImpl<PersonInfMapper, PersonInf> implements IPersonInfService{

	@Autowired
	private PersonInfMapper personInfMapper;
	/**
	 * 根据手机号查找用户
	 * @param phoneNo
	 * @return
	 */
	public PersonInf getPersonInfByPhoneNo(String phoneNo){
		QueryWrapper<PersonInf> queryWrapper = new QueryWrapper<PersonInf>();
		queryWrapper.eq("mobile_phone_no", phoneNo);
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		return personInfMapper.selectOne(queryWrapper);
	}
}
