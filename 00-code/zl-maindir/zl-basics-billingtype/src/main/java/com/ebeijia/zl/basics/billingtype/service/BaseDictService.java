package com.ebeijia.zl.basics.billingtype.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.common.core.domain.BaseDict;
import com.github.pagehelper.PageInfo;


/**
 * 字典信息接口
 * @author Administrator
 *
 */
public interface BaseDictService extends IService<BaseDict> {
	
	List<BaseDict> getBaseDictList(BaseDict baseDict);

	public PageInfo<BaseDict> getBaseDictListPage(int startNum, int pageSize, BaseDict baseDict);
}
