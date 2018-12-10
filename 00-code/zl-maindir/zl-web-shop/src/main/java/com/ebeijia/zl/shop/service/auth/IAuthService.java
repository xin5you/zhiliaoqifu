package com.ebeijia.zl.shop.service.auth;


import com.ebeijia.zl.shop.vo.Token;

public interface IAuthService {

    Token phoneLogin(String phone, String pwd);
}
