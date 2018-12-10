package com.ebeijia.zl.basics.wechat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.wechat.domain.MpAccount;

/**
 *
 * 微信账户表 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Mapper
public interface MpAccountMapper extends BaseMapper<MpAccount> {
	
	public MpAccount getByAccount(String account);
	
	public MpAccount getSingleAccount();

	public List<MpAccount> listForPage(MpAccount searchEntity);

}
