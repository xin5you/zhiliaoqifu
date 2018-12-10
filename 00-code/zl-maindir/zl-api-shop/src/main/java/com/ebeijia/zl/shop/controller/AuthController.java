package com.ebeijia.zl.shop.controller;


import com.ebeijia.zl.shop.service.auth.IAuthService;
import com.ebeijia.zl.shop.utils.TokenCheck;
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
 * @User
 * @Date 2018/12/05
 */
@Api(value = "/auth", description = "用于定义认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    IAuthService authService;

    //这里只有手机和验证码的登录
    @TokenCheck
    @ApiOperation("常规登录")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JsonResult<Token> login(@ApiParam("手机号") String phone, @ApiParam("验证码") String pwd) {
        return new JsonResult<>(authService.phoneLogin(phone,pwd));
    }


}
