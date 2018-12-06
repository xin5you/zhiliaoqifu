package com.ebeijia.zl.route.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api("用于定义会员相关接口")
@RestController
@RequestMapping("/member")
public class MemberController {

    @ApiOperation("注册")
    @RequestMapping("/signup")
    public void signUp(){
        //获取手机号
        //获取对应id
        //生产账户
    }


}
