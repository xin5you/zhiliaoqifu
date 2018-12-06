package com.ebeijia.zl.route.controller;


import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.service.member.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    IMemberService memberService;

    @ApiOperation("注册")
    @RequestMapping("/signup")
    public void signUp(){
        //获取手机号
        //获取对应id
        //生产账户
        TbEcomMember member =  memberService.createMember();
    }


}
