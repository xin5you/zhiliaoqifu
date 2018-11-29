package com.cn.thinkx.ecom.exception.service;

import com.cn.thinkx.ecom.basics.order.domain.ExceptionInf;
import com.github.pagehelper.PageInfo;

public interface ExceptionService {

	/**
	 * 查询所有的异常（含分页）
	 * 
	 * @param entity
	 * @return
	 */
	PageInfo<ExceptionInf> getExceptionlistPage(int startNum, int pageSize,ExceptionInf entity);
}
