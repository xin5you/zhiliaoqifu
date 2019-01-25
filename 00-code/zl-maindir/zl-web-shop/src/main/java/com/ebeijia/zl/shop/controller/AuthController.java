package com.ebeijia.zl.shop.controller;


import com.ebeijia.zl.shop.service.auth.IAuthService;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @ApiOperation("常规登录")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JsonResult<Token> login(@RequestParam("phone") String phone,@RequestParam("pwd") String pwd, String code) {
        return new JsonResult<>(authService.phoneLogin(phone,pwd,code));
    }

    //使用code登录
    @ApiOperation("微信登录")
    @RequestMapping(value = "/wxlogin",method = RequestMethod.POST)
    public JsonResult<Token> wxLogin(@RequestParam("code") String code) {
        return new JsonResult<>(authService.wxLogin(code));
    }

    //使用code登录
    @TokenCheck()
    @ApiOperation("取消绑定")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public JsonResult<Integer> logout() {
        return new JsonResult<>(authService.logout());
    }
}
