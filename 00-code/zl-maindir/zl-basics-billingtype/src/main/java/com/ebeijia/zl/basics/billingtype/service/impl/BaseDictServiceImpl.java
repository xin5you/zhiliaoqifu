package com.ebeijia.zl.basics.billingtype.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.billingtype.domain.BaseDict;
import com.ebeijia.zl.basics.billingtype.mapper.BaseDictMapper;
import com.ebeijia.zl.basics.billingtype.service.BaseDictService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 字典信息接口
 * @author Administrator
 *
 */
@Service
public class BaseDictServiceImpl extends ServiceImpl<BaseDictMapper, BaseDict> implements BaseDictService{
	
	@Autowired
	private BaseDictMapper baseDictMapper;

	@Override
	public PageInfo<BaseDict> getBaseDictListPage(int startNum, int pageSize, BaseDict baseDict) {
		PageHelper.startPage(startNum, pageSize);
		List<BaseDict> list = getBaseDictList(baseDict);
		PageInfo<BaseDict> page = new PageInfo<BaseDict>(list);
		return page;
	}

	@Override
	public List<BaseDict> getBaseDictList(BaseDict baseDict) {
		return baseDictMapper.getBaseDictList(baseDict);
	}

}
