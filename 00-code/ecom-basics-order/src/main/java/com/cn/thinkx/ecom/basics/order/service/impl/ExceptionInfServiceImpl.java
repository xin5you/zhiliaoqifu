package com.cn.thinkx.ecom.basics.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.order.domain.ExceptionInf;
import com.cn.thinkx.ecom.basics.order.mapper.ExceptionInfMapper;
import com.cn.thinkx.ecom.basics.order.service.ExceptionInfService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.StringUtil;

@Service("exceptionInfService")
public class ExceptionInfServiceImpl extends BaseServiceImpl<ExceptionInf> implements ExceptionInfService {
	
	@Autowired
	private ExceptionInfMapper exceptionInfMapper;

	/**
	 * 保存异常信息
	 * @param primaryKey  外部主键
	 * @param input_parameter 请求参数
	 * @param exception_type 异常错误类型
	 * @param exception_desc 返回类型
	 */
	public void saveExceptionInf(String primaryKey,
									   String inputParameter,
									   String exceptionType,
									   String exceptionDesc){
		ExceptionInf re=new ExceptionInf();
		re.setPrimaryKey(primaryKey);
		re.setExceptionType(exceptionType);
		re.setInputParameter(inputParameter);
		re.setExceptionDesc(exceptionDesc);
		re.setDataStat("0");
		re.setProcessTimes("0");
		re.setExceptionStatc("0");
		try {
			this.insert(re);
		} catch (Exception e) {
			
		}
		
	}
}
