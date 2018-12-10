package com.ebeijia.zl.basics.wechat.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.wechat.domain.MpAccount;


/**
 *
 * 微信账户表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface MpAccountService extends IService<MpAccount> {
	
	public MpAccount getByAccount(String account);

	public MpAccount getSingleAccount();

	public List<MpAccount> listForPage(MpAccount searchEntity);

}
