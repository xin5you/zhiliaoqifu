package com.ebeijia.zl.shop.service.valid;

public interface IValidCodeService {
    Double getSession();

    Integer sendPhoneValidCode(String phoneNum, String method);

    void checkFrequency(String phoneNum, String method);

    boolean checkValidCode(String method, String phone, String pwd);
}
