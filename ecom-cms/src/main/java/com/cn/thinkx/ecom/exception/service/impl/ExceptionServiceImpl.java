package com.cn.thinkx.ecom.exception.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.ExceptionInf;
import com.cn.thinkx.ecom.basics.order.mapper.ExceptionInfMapper;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.exception.service.ExceptionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service("exceptionService")
public class ExceptionServiceImpl implements ExceptionService{

	@Autowired
	private ExceptionInfMapper exceptionInfMapper;
	
	@Override
	public PageInfo<ExceptionInf> getExceptionlistPage(int startNum, int pageSize, ExceptionInf entity) {
		PageHelper.startPage(startNum, pageSize);
		List<ExceptionInf> exceptionList = exceptionInfMapper.getList(entity);
		for (ExceptionInf exceptionInf : exceptionList) {
			exceptionInf.setExceptionType(ExceptionEnum.exceptionType.findByCode(exceptionInf.getExceptionType()).getMsg());
		}
		PageInfo<ExceptionInf> page = new PageInfo<ExceptionInf>(exceptionList);
		return page;
	}

}
