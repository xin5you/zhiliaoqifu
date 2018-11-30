package com.cn.thinkx.ecom.exception.service.impl;

import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.exception.service.ExceptionService;
@Service("exceptionService")
public class ExceptionServiceImpl implements ExceptionService{
/*
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
	}*/

}
