package com.ebeijia.zl.service.account.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.account.vo.AccountLog;

/**
 *
 * 账户交易日志 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Mapper
public interface AccountLogMapper extends BaseMapper<AccountLog> {

}
