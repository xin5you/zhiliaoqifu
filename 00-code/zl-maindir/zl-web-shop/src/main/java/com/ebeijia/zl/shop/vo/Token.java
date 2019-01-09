package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于包装账户认证中心返回的令牌信息
 */
@ApiModel("令牌信息")
@Data
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    //    private String userId;
    private MemberInfo info;

    public Token(String token, MemberInfo memberInfo) {
        this.token = token;
        this.info = memberInfo;
    }

}
