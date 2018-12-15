package com.ebeijia.zl.shop.utils;

import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.shop.vo.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class WebAdviceHandler {

    Logger logger = LoggerFactory.getLogger(WebAdviceHandler.class);


    /**
     * 全局捕获，传递信使信息
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = AdviceMessenger.class)
    public JsonResult<Object> messengerHandler(AdviceMessenger ex) {
        JsonResult<Object> result = new JsonResult<>();
        result.setCode(ex.getCode());
        result.setMessage(ex.getMsg());
        return result;
    }



    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult<Object> errorHandler(Exception ex) {
        logger.error("Shop-E",ex);
        JsonResult<Object> result = new JsonResult<>();
        result.setCode(500);
        result.setMessage("网络不稳定，请稍后再试");
        return result;
    }

    /**
     * 拦截捕捉自定义异常
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public JsonResult<Object> myErrorHandler(BizException ex) {
        logger.error("Shop-E",ex);
        JsonResult<Object> result = new JsonResult<>();
        result.setCode(ex.getCode());
        result.setMessage(ex.getMsg());
        return result;
    }

}
