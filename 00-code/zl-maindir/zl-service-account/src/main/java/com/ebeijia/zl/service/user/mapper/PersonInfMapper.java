package com.ebeijia.zl.service.user.mapper;

import org.apache.ibatis.annotations.Mapper;

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

}
