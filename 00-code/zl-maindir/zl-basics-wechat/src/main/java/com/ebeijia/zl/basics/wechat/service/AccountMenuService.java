package com.ebeijia.zl.basics.wechat.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.wechat.domain.AccountMenu;


/**
 *
 * 微信菜单表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface AccountMenuService extends IService<AccountMenu> {
	

	public List<AccountMenu> listForPage(AccountMenu searchEntity);

	public List<AccountMenu> listParentMenu();

}
