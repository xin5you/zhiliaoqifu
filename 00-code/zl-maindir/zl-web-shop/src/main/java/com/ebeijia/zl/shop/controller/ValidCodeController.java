package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import com.ebeijia.zl.shop.vo.ChannelOrder;
import com.ebeijia.zl.shop.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api(value = "/valid", description = "用于定义生产验证码相关的接口")
@RequestMapping("/valid")
@RestController
public class ValidCodeController {

    @Autowired
    IValidCodeService validCodeService;

    @ApiOperation("根据手机号获取验证码，返回发送状态"+
            "登录：PHONE_VALID_LOGIN\n" + "支付：PHONE_VALID_PAY")
    @RequestMapping(value = "/phone",method = RequestMethod.POST)
    public JsonResult<Object> phoneValidCode(@RequestParam("phone")String phoneNum, @RequestParam("method") String method){
        validCodeService.checkFrequency(phoneNum,method);
        //验证码方法
        Integer stat = validCodeService.sendPhoneValidCode(phoneNum,method);
        //30秒内只能发送一条验证码
        return new JsonResult<>(stat);
    }

    @ApiOperation("获取一个session口令，避免同页面重复提交")
    @RequestMapping(value = "/session/get",method = RequestMethod.GET)
    public JsonResult<Double> getSession(){
        return new JsonResult<>(validCodeService.getSession());
    }


}
