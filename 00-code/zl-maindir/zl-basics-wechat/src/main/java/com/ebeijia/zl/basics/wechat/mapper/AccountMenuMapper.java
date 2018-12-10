package com.ebeijia.zl.basics.wechat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.wechat.domain.AccountMenu;

/**
 *
 * 微信菜单表 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Mapper
public interface AccountMenuMapper extends BaseMapper<AccountMenu> {
	
	public List<AccountMenu> listForPage(AccountMenu searchEntity);

	public List<AccountMenu> listParentMenu();
	
	public List<AccountMenu> listWxMenus(String gid);

}
