package com.ebeijia.zl.shop.auth.service;


import com.ebeijia.zl.shop.vo.Token;

public interface IAuthService {

    Token phoneLogin(String phone, String pwd);
}
