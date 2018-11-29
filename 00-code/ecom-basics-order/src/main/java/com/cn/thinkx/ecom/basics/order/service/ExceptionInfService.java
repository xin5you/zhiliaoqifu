package com.cn.thinkx.ecom.basics.order.service;

import com.cn.thinkx.ecom.basics.order.domain.ExceptionInf;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface ExceptionInfService extends BaseService<ExceptionInf> {

	/**
	 * 保存异常信息
	 * @param primaryKey  外部主键
	 * @param input_parameter 请求参数
	 * @param exception_type 异常错误类型
	 * @param exception_desc 返回类型
	 */
	void saveExceptionInf(String primaryKey,
									   String input_parameter,
									   String exception_type,
									   String exception_desc);
}
