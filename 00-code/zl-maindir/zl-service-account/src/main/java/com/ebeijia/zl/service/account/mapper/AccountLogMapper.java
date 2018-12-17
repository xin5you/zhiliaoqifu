package com.ebeijia.zl.service.account.mapper;

import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.vo.AccountLogVo;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.account.dto.AccountLog;

import java.util.List;

/**
 *
 * 账户交易日志 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Mapper
public interface AccountLogMapper extends BaseMapper<AccountLog> {

    /**
     * 账单查询
     * @param req
     * @return
     */
    List<AccountLogVo> getAccountLogVoList(AccountQueryReqVo req);
}
