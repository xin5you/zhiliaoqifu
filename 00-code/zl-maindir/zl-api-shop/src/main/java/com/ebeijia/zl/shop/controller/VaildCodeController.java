package com.ebeijia.zl.shop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api(value = "/vaild", description = "用于定义生产验证码相关的接口")
@RequestMapping("/vaild")
@RestController
public class VaildCodeController {

    @ApiOperation("根据手机号获取验证码，返回发送状态")
    @RequestMapping(value = "/phone",method = RequestMethod.POST)
    public void phoneVaildCode(@RequestParam("phone")String phoneNum){
        //30秒内只能发送一条验证码
    }



}
