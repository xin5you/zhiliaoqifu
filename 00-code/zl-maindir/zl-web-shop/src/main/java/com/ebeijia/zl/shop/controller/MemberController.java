package com.ebeijia.zl.shop.controller;


import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.service.member.IMemberService;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.MemberInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api(value = "/member", description = "用于定义会员相关接口")
@RequestMapping("/member")
@RestController
public class MemberController {

    @Autowired
    private IMemberService memberService;


    @ApiOperation("注册")
    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public JsonResult<TbEcomMember> signUp(){
        //获取手机号
        //获取对应id
        //生产账户
        TbEcomMember member =  memberService.createMember();
        return new JsonResult<>(member);
    }

    @TokenCheck(force = true)
    @ApiOperation("用户信息查询")
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public JsonResult<MemberInfo> findUser(){

        //获取手机号
        //获取对应id
        //生产账户
        MemberInfo member =  memberService.getMemberInfo();
        return new JsonResult<>(member);
    }

    @ApiImplicitParam(name = "Authorization",value = "Authorization",paramType ="header")
    @TokenCheck(force = true)
    @ApiOperation("新增地址，目前修改也一样调用这个接口")
    @RequestMapping(value = "/address/create",method = RequestMethod.POST)
    public JsonResult newAddress(AddressInfo address, Integer pos){
        Integer state = memberService.newAddress(address,pos);
        JsonResult<Object> result = new JsonResult<>();
        result.setCode(state);
        return result;
    }

    @ApiImplicitParam(name = "Authorization",value = "Authorization",paramType ="header")
    @TokenCheck(force = true)
    @ApiOperation("查询收货地址")
    @RequestMapping(value = "/address/list",method = RequestMethod.GET)
    public JsonResult<AddressInfo> listAddress(){
        AddressInfo address = memberService.listAddress();
        return new JsonResult(address);
    }



}
