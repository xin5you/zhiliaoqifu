package com.ebeijia.zl.service.user.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
