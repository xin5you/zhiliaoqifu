package com.ebeijia.zl.service.user.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
