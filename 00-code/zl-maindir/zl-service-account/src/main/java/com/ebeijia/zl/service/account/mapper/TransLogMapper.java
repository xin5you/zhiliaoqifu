package com.ebeijia.zl.service.account.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.account.vo.TransLog;

/**
 *
 * 账户交易流水 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Mapper
public interface TransLogMapper extends BaseMapper<TransLog> {

}
