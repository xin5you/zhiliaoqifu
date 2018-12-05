package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api("用于定义生产验证码相关的接口")
@RestController
@RequestMapping("/vaild")
public class VaildCodeController {

    //根据手机号获取验证码，返回发送状态
    public void phoneVaildCode(String phoneNum){

    }


}
