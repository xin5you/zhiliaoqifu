package com.ebeijia.zl.route.controller;


import com.ebeijia.zl.shop.auth.service.IAuthService;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api("用于定义认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    IAuthService IAuthService;

    //这里只有手机和验证码的登录
    @ApiOperation("常规登录")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JsonResult login(@ApiParam("手机号") String phone, @ApiParam("验证码") String pwd) {
        //前端测试用
        return new JsonResult().put("token",new Token("testToken","TT233"));
    }


}
