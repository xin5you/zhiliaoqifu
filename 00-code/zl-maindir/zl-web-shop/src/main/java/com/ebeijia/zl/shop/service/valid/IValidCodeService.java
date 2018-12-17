package com.ebeijia.zl.shop.service.valid;

public interface IValidCodeService {
    Double getSession();

    Integer sendPhoneValidCode(String phoneNum, String method);

}
