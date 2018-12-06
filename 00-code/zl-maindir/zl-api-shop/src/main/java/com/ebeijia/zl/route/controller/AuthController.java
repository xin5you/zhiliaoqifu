package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api("用于定义认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {


    //这里只有手机和验证码的登录
    @ApiOperation("常规登录")
    @RequestMapping("/login")
    public String login(@ApiParam("手机号") String phone,@ApiParam("验证码") String pwd) {
        return null;
    }



}
