package com.cn.thinkx.ecom.common.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.ResultsUtil;

/**
 * 统一异常处理
 * 
 * @author pucker
 *
 */
@ControllerAdvice
@ResponseBody
public class BizExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public BaseResult<?> handler(Exception e) {
		if (e instanceof BizHandlerException) {
			BizHandlerException bhe = (BizHandlerException) e;
			return ResultsUtil.error(bhe.getCode(), bhe.getMessage());
		}
		return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
	}
}
